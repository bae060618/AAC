package com.example.aac.favorite

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aac.R
import com.example.aac.ThemeManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var clearButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite_layout)

        recyclerView = findViewById(R.id.favoritesRecyclerView)
        clearButton = findViewById(R.id.btnClearFavorites)

        val themeColor = ThemeManager.getThemeColor(this)
        val rootLayout = findViewById<LinearLayout>(R.id.root_favorite_layout)
        rootLayout.setBackgroundColor(Color.WHITE)
        clearButton.backgroundTintList = ColorStateList.valueOf(themeColor)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }
        loadFavorites(uid)

        clearButton.setOnClickListener {
            deleteAllFavorites(uid)
        }
    }

    private fun loadFavorites(uid: String) {
        db.collection("users").document(uid).collection("favorites")
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<FavoriteItem>()
                for (document in result) {
                    val label = document.getString("label") ?: ""
                    val icon = (document.get("iconResId") as? Long)?.toInt() ?: R.drawable.ic_star_border
                    val docId = document.id
                    itemList.add(FavoriteItem(label, icon, docId))
                }

                val adapter = FavoriteAdapter(itemList) { item ->
                    db.collection("users").document(uid)
                        .collection("favorites").document(item.docId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "삭제됨: ${item.label}", Toast.LENGTH_SHORT).show()
                            loadFavorites(uid) // 새로고침
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "삭제 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }

                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "불러오기 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteAllFavorites(uid: String) {
        db.collection("users").document(uid).collection("favorites")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    document.reference.delete()
                }
                recyclerView.adapter = null
                Toast.makeText(this, "즐겨찾기를 모두 삭제했습니다.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "전체 삭제 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun addFavoriteToFirestore(label: String, iconResId: Int) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val favorite = hashMapOf(
            "label" to label,
            "iconResId" to iconResId
        )

        db.collection("users").document(uid)
            .collection("favorites")
            .add(favorite)
            .addOnSuccessListener {
                Toast.makeText(this, "$label(이)가 즐겨찾기에 추가됨 (Firestore)", Toast.LENGTH_SHORT).show()
                loadFavorites(uid) // 바로 목록 갱신
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "추가 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

