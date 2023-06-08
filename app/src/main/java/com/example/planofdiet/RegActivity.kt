package com.example.planofdiet
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        auth = Firebase.auth

        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)

        val signUpButton: Button = findViewById(R.id.sign_up_button)
        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (email.isEmpty()) {
                emailEditText.error = "email обязателен"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Пароль обязателен"
                passwordEditText.requestFocus()
                return@setOnClickListener
            }

           auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Пользователь успешно создан")
                        val user = auth.currentUser
                        startMainActivity()
                    } else {
                        Log.w(TAG, "Ошибка в создании пользователя", task.exception)
                        when (task.exception) {
                            is FirebaseAuthUserCollisionException -> {
                                emailEditText.error = "email уже существует"
                                emailEditText.requestFocus()
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                emailEditText.error = "Неверный email"
                                emailEditText.requestFocus()
                            }
                            else -> {
                                Toast.makeText(baseContext, "Ошибка регистрации",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "RegActivity"
    }
}
