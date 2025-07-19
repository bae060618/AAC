package com.example.aac

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editConfirmPassword: EditText
    private lateinit var editNickname: EditText
    private lateinit var btnRegister: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        auth = FirebaseAuth.getInstance()

        editEmail = findViewById(R.id.editEmail)
        editPassword = findViewById(R.id.editPassword)
        editConfirmPassword = findViewById(R.id.editConfirmPassword)
        editNickname = findViewById(R.id.editNickname)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()
            val confirm = editConfirmPassword.text.toString()
            val nickname = editNickname.text.toString()

            when {
                email.isBlank() || password.isBlank() || confirm.isBlank() || nickname.isBlank() -> {
                    Toast.makeText(this, "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                password != confirm -> {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                ProfilePreferences.saveNickname(this, nickname)
                                Toast.makeText(this, "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "회원가입 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }
}

