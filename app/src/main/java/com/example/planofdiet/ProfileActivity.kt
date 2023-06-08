package com.example.planofdiet

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var weightEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var heightEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var exitButton: Button
    private lateinit var Exit: Button
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        nameEditText = findViewById(R.id.editTextName)
        weightEditText = findViewById(R.id.editTextWeight)
        ageEditText = findViewById(R.id.editTextAge)
        heightEditText = findViewById(R.id.editTextHeight)
        saveButton = findViewById(R.id.buttonSave)
        exitButton = findViewById(R.id.buttonExit)
        Exit = findViewById(R.id.Exit)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        saveButton.setOnClickListener {
            saveUserData()
        }

        exitButton.setOnClickListener {
            startActivity(Intent(this, DietActivity::class.java))
            finish()
        }

        Exit.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        loadUserData()
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val userRef = database.child("users").child(userId)

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userData = snapshot.value as? Map<*, *>
                        val name = userData?.get("name") as? String
                        val weight = userData?.get("weight") as? Long
                        val age = userData?.get("age") as? Long
                        val height = userData?.get("height") as? Long

                        nameEditText.setText(name)
                        weightEditText.setText(weight?.toString())
                        ageEditText.setText(age?.toString())
                        heightEditText.setText(height?.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }


    private fun saveUserData() {
        val name = nameEditText.text.toString()
        val weightText = weightEditText.text.toString()
        val ageText = ageEditText.text.toString()
        val heightText = heightEditText.text.toString()

        val weight: Int = try {
            weightText.toInt()
        } catch (e: NumberFormatException) {
            weightEditText.error = "Введите значение от 20 до 200"
            return
        }

        val age: Int = try {
            ageText.toInt()
        } catch (e: NumberFormatException) {
            ageEditText.error = "Введите значение от 1 до 80"
            return
        }

        val height: Int = try {
            heightText.toInt()
        } catch (e: NumberFormatException) {
            heightEditText.error = "Введите значение от 75 до 230"
            return
        }

        val isValid = validateValues(weight, age, height)

        if (isValid) {
            val userId = auth.currentUser?.uid

            val name = nameEditText.text.toString()
            val weight = weightEditText.text.toString().toLongOrNull()
            val age = ageEditText.text.toString().toLongOrNull()
            val height = heightEditText.text.toString().toLongOrNull()

            if (userId != null) {
                val userRef = database.child("users").child(userId)

                val userDataUpdates = HashMap<String, Any>()

                if (!name.isNullOrEmpty()) {
                    userDataUpdates["name"] = name
                }
                if (weight != null) {
                    userDataUpdates["weight"] = weight
                }
                if (age != null) {
                    userDataUpdates["age"] = age
                }
                if (height != null) {
                    userDataUpdates["height"] = height
                }

                userRef.updateChildren(userDataUpdates)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Проверьте поля ввода текста", Toast.LENGTH_SHORT).show()
                    }
            }
            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show()
        } else {
            // Некоторые значения находятся вне допустимого диапазона
            Toast.makeText(this, "Проверьте поля ввода", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateValues(weight: Int, age: Int, height: Int): Boolean {
        var isValid = true

        if (weight < 20 || weight > 200) {
            weightEditText.error = "Введите значение от 20 до 200"
            isValid = false
        }

        if (age < 1 || age > 80) {
            ageEditText.error = "Введите значение от 1 до 80"
            isValid = false
        }

        if (height < 75 || height > 230) {
            heightEditText.error = "Введите значение от 75 до 230"
            isValid = false
        }

        return isValid
    }
}
