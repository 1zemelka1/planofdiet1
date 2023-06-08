package com.example.planofdiet
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseDatabaseHelper {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.getReference("users")

    fun addUser(name: String, weight: Int, age: Int, height: Int,gender: String) {
        val id = usersRef.push().key // генерация уникального ключа
        val user = User(id, name, weight, age, height, gender)
        usersRef.child(id!!).setValue(user)
    }
}

