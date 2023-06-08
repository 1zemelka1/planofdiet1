package com.example.planofdiet

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var edName: EditText
    private lateinit var edWeight: EditText
    private lateinit var edAge: EditText
    private lateinit var edHeight: EditText
    private lateinit var spSex: Spinner
    private lateinit var btnAddUser: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        edName = findViewById(R.id.edName)
        edWeight = findViewById(R.id.edWeight)
        edAge = findViewById(R.id.edAge)
        edHeight = findViewById(R.id.edHeight)
        spSex = findViewById(R.id.edSex)
        btnAddUser = findViewById(R.id.EndOfRegButton)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        currentUser = firebaseAuth.currentUser

        btnAddUser.setOnClickListener {
            val name = edName.text.toString().trim()
            val weightText = edWeight.text.toString()
            val ageText = edAge.text.toString()
            val heightText = edHeight.text.toString()
            val gender = spSex.selectedItem.toString()

            val weight: Int = try {
                weightText.toInt()
            } catch (e: NumberFormatException) {
                edWeight.error = "Введите значение от 20 до 200"
                return@setOnClickListener
            }

            val age: Int = try {
                ageText.toInt()
            } catch (e: NumberFormatException) {
                edAge.error = "Введите значение от 1 до 80"
                return@setOnClickListener
            }

            val height: Int = try {
                heightText.toInt()
            } catch (e: NumberFormatException) {
                edHeight.error = "Введите значение от 75 до 230"
                return@setOnClickListener
            }

            if (!validateValues(weight, age, height)) {
                return@setOnClickListener
            }

            currentUser?.uid?.let { userId ->
                val user = User(userId, name, weight, age, height, gender)
                val usersRef = firebaseDatabase.reference.child("users")
                usersRef.child(userId).setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Пользователь успешно добавлен", Toast.LENGTH_SHORT).show()
                        clearFields()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Ошибка в добавлении данных пользователя", Toast.LENGTH_SHORT).show()
                    }
            }
            val intent = Intent(this, DietActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateValues(weight: Int, age: Int, height: Int): Boolean {
        var isValid = true

        if (weight < 20 || weight > 200) {
            edWeight.error = "Введите значение от 20 до 200"
            isValid = false
        }

        if (age < 1 || age > 80) {
            edAge.error = "Введите значение от 1 до 80"
            isValid = false
        }

        if (height < 75 || height > 230) {
            edHeight.error = "Введите значение от 75 до 230"
            isValid = false
        }

        return isValid
    }

    private fun clearFields() {
        edName.text.clear()
        edWeight.text.clear()
        edAge.text.clear()
        edHeight.text.clear()
    }
}
