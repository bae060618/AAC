package com.example.aac

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class ProfileFragment : Fragment() {

    private lateinit var profileImage: ImageView
    private lateinit var btnChangeImage: Button
    private lateinit var editNickname: EditText
    private lateinit var btnSaveProfile: Button
    private lateinit var rootLayout: View
    private lateinit var themeColorButtons: List<ImageButton>

    private val PICK_IMAGE_REQUEST = 1001
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_custom, container, false)

        profileImage = view.findViewById(R.id.profileImage)
        btnChangeImage = view.findViewById(R.id.btnChangeImage)
        editNickname = view.findViewById(R.id.editNickname)
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile)
        rootLayout = view.findViewById(R.id.profileRootLayout)

        val redBtn = view.findViewById<ImageButton>(R.id.themeColorRed)
        val blueBtn = view.findViewById<ImageButton>(R.id.themeColorBlue)
        val yellowBtn = view.findViewById<ImageButton>(R.id.themeColorYellow)
        val orangeBtn = view.findViewById<ImageButton>(R.id.themeColorOrange)

        themeColorButtons = listOf(redBtn, blueBtn, yellowBtn, orangeBtn)

        applySavedThemeColor() // ✅ 초기화 이후 호출해야 안전

        redBtn.setOnClickListener { updateThemeColor("#D11222") }
        blueBtn.setOnClickListener { updateThemeColor("#2196F3") }
        yellowBtn.setOnClickListener { updateThemeColor("#FFEB3B") }
        orangeBtn.setOnClickListener { updateThemeColor("#FF9800") }

        btnChangeImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnSaveProfile.setOnClickListener {
            val nickname = editNickname.text.toString()
            if (nickname.isBlank()) {
                showToast("닉네임을 입력해주세요")
            } else {
                ProfilePreferences.saveNickname(requireContext(), nickname)
                showToast("프로필이 저장되었습니다: $nickname")
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageUri?.let { uri ->
                val bitmap = loadBitmapFromUri(uri)
                profileImage.setImageBitmap(bitmap)
            }
        }
    }

    private fun loadBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun saveThemeColor(color: String) {
        val prefs = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("theme_color", color).apply()
    }

    private fun applySavedThemeColor() {
        val prefs = requireContext().getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
        val savedColor = prefs.getString("theme_color", "#FF9800")
        applyThemeColor(savedColor ?: "#FF9800")
    }

    private fun updateThemeColor(color: String) {
        saveThemeColor(color)
        applyThemeColor(color)
        showToast("테마가 변경되었습니다")
    }
    private fun applyThemeColor(hexColor: String) {
        try {
            val color = Color.parseColor(hexColor)
            val activity = requireActivity()

            val toolbar = activity.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
            toolbar?.setBackgroundColor(color)

            val navView = activity.findViewById<NavigationView>(R.id.nav_view)
            val headerView = navView.getHeaderView(0)
            val headerContainer = headerView.findViewById<View>(R.id.header_container)
            headerContainer.setBackgroundColor(color)

            btnChangeImage.setBackgroundColor(color)

        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

}

