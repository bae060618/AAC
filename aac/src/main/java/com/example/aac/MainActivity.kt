package com.example.aac

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var editId: EditText
    private lateinit var editPw: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnLoginGoogle: Button
    private lateinit var signUpText: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val RC_SIGN_IN = 9001
    private val REQUEST_CODE_POST_NOTIFICATIONS = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val themeColor = ThemeManager.getThemeColor(this)

        editId = findViewById(R.id.editId)
        editPw = findViewById(R.id.editPw)
        btnLogin = findViewById(R.id.btnLogin)
        btnLoginGoogle = findViewById(R.id.btnLogin_Google)
        signUpText = findViewById(R.id.signUpText)
        auth = FirebaseAuth.getInstance()

        btnLogin.setBackgroundColor(themeColor)
        btnLoginGoogle.setBackgroundColor(themeColor)

        val btnTestNotification: Button = findViewById(R.id.btnTestNotification)
        btnTestNotification.setBackgroundColor(themeColor)
        btnTestNotification.setOnClickListener {
            sendTestNotification()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_CODE_POST_NOTIFICATIONS
                )
            }
        }

        createNotificationChannel()
        scheduleDailyNotification(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btnLogin.setOnClickListener {
            val userId = editId.text.toString()
            val password = editPw.text.toString()

            if (userId.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(userId, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "로그인 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }


        btnLoginGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        signUpText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google 로그인 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "구글 로그인 성공!", Toast.LENGTH_SHORT).show()

                    val user = FirebaseAuth.getInstance().currentUser
                    val uid = user?.uid

                    if (uid != null) {
                        val db = FirebaseFirestore.getInstance()

                        val book = hashMapOf(
                            "title" to "사용자별 책",
                            "author" to user.displayName,
                            "year" to 2025
                        )

                        db.collection("users")
                            .document(uid)
                            .collection("books")
                            .add(book)
                            .addOnSuccessListener { docRef ->
                                Log.d("Firestore", "사용자 $uid 문서 추가됨: ${docRef.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Firestore", "문서 추가 실패", e)
                            }
                    }

                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Firebase 인증 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "daily_emotion",
                "Daily Emotion Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "매일 감정 기록 알림"
            }

            val manager = getSystemService(NotificationManager::class.java)
            if (manager.getNotificationChannel("daily_emotion") == null) {
                manager.createNotificationChannel(channel)
            }
        }
    }

    private fun scheduleDailyNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun sendTestNotification() {
        val intent = Intent(this, EmotionSelectionActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, "daily_emotion")
            .setSmallIcon(R.drawable.ic_happy)
            .setContentTitle("오늘의 감정 체크")
            .setContentText("오늘 하루 기분은 어땠나요?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(9999, builder.build())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "알림 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
                sendTestNotification()
            } else {
                Toast.makeText(this, "알림 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


