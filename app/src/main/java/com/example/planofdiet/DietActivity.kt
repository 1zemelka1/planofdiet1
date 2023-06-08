package com.example.planofdiet

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DietActivity : AppCompatActivity() {
    private lateinit var user: User
    private lateinit var ChangeGarnirB: Button
    private lateinit var ChangeMainB: Button
    private lateinit var ChangeDrinkB: Button
    private lateinit var ChangeGarnirL: Button
    private lateinit var ChangeMainL: Button
    private lateinit var ChangeDrinkL: Button
    private lateinit var ChangeDop: Button
    private lateinit var ChangeGarnirD: Button
    private lateinit var ChangeMainD: Button
    private lateinit var ChangeDrinkD: Button

    private lateinit var GarnirB: TextView
    private lateinit var MainB: TextView
    private lateinit var DrinkB: TextView
    private lateinit var TotalB: TextView
    private lateinit var GarnirL: TextView
    private lateinit var MainL: TextView
    private lateinit var DrinkL: TextView
    private lateinit var TotalL: TextView
    private lateinit var DopL: TextView
    private lateinit var GarnirD: TextView
    private lateinit var MainD: TextView
    private lateinit var DrinkD: TextView
    private lateinit var TotalD: TextView

    private lateinit var buttonProfile: Button
    private lateinit var caloriesTextView: TextView
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var dietActivityModel: DietActivityModel
    private lateinit var breakfastRef: DatabaseReference
    private lateinit var databaseReference: DatabaseReference


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        ChangeGarnirB = findViewById(R.id.changeGarnirB)
        ChangeMainB = findViewById(R.id.ChangeMainB)
        ChangeDrinkB = findViewById(R.id.changeDrinkB)
        ChangeGarnirL = findViewById(R.id.ChangeGarnirL)
        ChangeMainL = findViewById(R.id.ChangeMainL)
        ChangeDrinkL = findViewById(R.id.ChangeDrinkL)
        ChangeDop = findViewById(R.id.ChangeDop)
        ChangeGarnirD = findViewById(R.id.ChangeGarnirD)
        ChangeMainD = findViewById(R.id.ChangeMainD)
        ChangeDrinkD = findViewById(R.id.ChangeDrinkD)

        GarnirB = findViewById(R.id.GarnirB)
        MainB = findViewById(R.id.MainB)
        DrinkB = findViewById(R.id.DrinkB)
        TotalB = findViewById(R.id.TotalB)
        GarnirL = findViewById(R.id.GarnirL)
        MainL = findViewById(R.id.MainL)
        DrinkL = findViewById(R.id.DrinkL)
        TotalL = findViewById(R.id.TotalL)
        DopL = findViewById(R.id.DopL)
        GarnirD = findViewById(R.id.GarnirD)
        MainD = findViewById(R.id.MainD)
        DrinkD = findViewById(R.id.DrinkD)
        TotalD = findViewById(R.id.TotalD)

        buttonProfile = findViewById(R.id.buttonProfile)
        caloriesTextView = findViewById(R.id.howMuchCallory)

        databaseReference = FirebaseDatabase.getInstance().reference
        database = FirebaseDatabase.getInstance()
        userReference = database.reference.child("users")
        auth = FirebaseAuth.getInstance()
        dietActivityModel = DietActivityModel()
        breakfastRef = database.reference.child("dishes").child("breakfast").child("garnish")

        setupOnClickListeners()

        buttonProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        ChangeGarnirB.setOnClickListener {
            generateRandomGarnir()
        }

        ChangeDop.setOnClickListener {
            generateRandomDop()
        }

        ChangeGarnirL.setOnClickListener {
            generateRandomGarnirL()
        }

        ChangeGarnirD.setOnClickListener {
            generateRandomGarnirD()
        }

        ChangeMainB.setOnClickListener {
            generateRandomMain()
        }

        ChangeMainD.setOnClickListener {
            generateRandomMainD()
        }

        ChangeMainL.setOnClickListener {
            generateRandomMainL()
        }

        ChangeDrinkB.setOnClickListener {
            generateRandomDrinkB()
        }

        ChangeDrinkL.setOnClickListener {
            generateRandomDrinkL()
        }

        ChangeDrinkD.setOnClickListener {
            generateRandomDrinkD()
        }



        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userQuery = userReference.child(userId)
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            val calories = dietActivityModel.calculateCalories(user)
                            val totalcalories = "$calories калорий."
                            caloriesTextView.text = totalcalories.toString()

                            val totalCaloriesB = dietActivityModel.calculateTotalCalories(calories, 0.25)
                            val totalCaloriesL = dietActivityModel.calculateTotalCalories(calories, 0.5)
                            val totalCaloriesD = dietActivityModel.calculateTotalCalories(calories, 0.25)

                            TotalB.text = "Итого: $totalCaloriesB Ккл"
                            TotalL.text = "Итого: $totalCaloriesL Ккл"
                            TotalD.text = "Итого: $totalCaloriesD Ккл"
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

    }

    private fun generateRandomGarnir(calories: Int, garnishList: List<Garnish>): String {
        val randomGarnish = dietActivityModel.getRandomGarnishWithCalories(calories, garnishList)
        randomGarnish?.let {
            val dishCalories = it.calories
            val dishGrams = dietActivityModel.calculateDishGrams(calories, dishCalories)
            return "${it.name}, ${dishGrams.toInt()} гр"
        }
        return ""
    }

    private fun generateRandomDop(calories: Int, garnishList: List<Garnish>): String {
        val randomGarnish = dietActivityModel.getRandomGarnishWithCalories(calories, garnishList)
        randomGarnish?.let {
            val dishCalories = it.calories
            val dishGrams = dietActivityModel.calculateMainDopGrams(calories, dishCalories)
            return "${it.name}, ${dishGrams.toInt()} мл"
        }
        return ""
    }

    private fun generateRandomDrink(calories: Int, garnishList: List<Garnish>): String {
        val randomGarnish = dietActivityModel.getRandomGarnishWithCalories(calories, garnishList)
        randomGarnish?.let {
            val dishCalories = it.calories
            val dishGrams = dietActivityModel.calculateDrink(calories, dishCalories)
            return "${it.name}, 250 мл"
        }
        return ""
    }

    private fun generateRandomDop() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userQuery = databaseReference.child("users").child(userId)
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            val calories = dietActivityModel.calculateCalories(user)
                            val totalCalories = "$calories калорий."
                            caloriesTextView.text = totalCalories

                            databaseReference.child("dops")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val garnishList = mutableListOf<Garnish>()
                                            for (childSnapshot in dataSnapshot.children) {
                                                val garnish = childSnapshot.getValue(Garnish::class.java)
                                                garnish?.let { garnishList.add(it) }
                                            }

                                            val garnirText = generateRandomDop(calories, garnishList)
                                            DopL.text = garnirText
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun generateRandomDrinkB() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userQuery = databaseReference.child("users").child(userId)
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            val calories = dietActivityModel.calculateCalories(user)
                            val totalCalories = "$calories калорий."
                            caloriesTextView.text = totalCalories

                            databaseReference.child("drinks")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val garnishList = mutableListOf<Garnish>()
                                            for (childSnapshot in dataSnapshot.children) {
                                                val garnish = childSnapshot.getValue(Garnish::class.java)
                                                garnish?.let { garnishList.add(it) }
                                            }

                                            val garnirText = generateRandomDrink(calories, garnishList)
                                            DrinkB.text = garnirText
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun generateRandomDrinkL() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userQuery = databaseReference.child("users").child(userId)
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            val calories = dietActivityModel.calculateCalories(user)
                            val totalCalories = "$calories калорий."
                            caloriesTextView.text = totalCalories

                            databaseReference.child("drinks")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val garnishList = mutableListOf<Garnish>()
                                            for (childSnapshot in dataSnapshot.children) {
                                                val garnish = childSnapshot.getValue(Garnish::class.java)
                                                garnish?.let { garnishList.add(it) }
                                            }

                                            val garnirText = generateRandomDrink(calories, garnishList)
                                            DrinkL.text = garnirText
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun generateRandomDrinkD() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userQuery = databaseReference.child("users").child(userId)
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            val calories = dietActivityModel.calculateCalories(user)
                            val totalCalories = "$calories калорий."
                            caloriesTextView.text = totalCalories

                            databaseReference.child("drinks")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val garnishList = mutableListOf<Garnish>()
                                            for (childSnapshot in dataSnapshot.children) {
                                                val garnish = childSnapshot.getValue(Garnish::class.java)
                                                garnish?.let { garnishList.add(it) }
                                            }

                                            val garnirText = generateRandomDrink(calories, garnishList)
                                            DrinkD.text = garnirText
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun generateRandomMainL(calories: Int, garnishList: List<Garnish>): String {
        val randomGarnish = dietActivityModel.getRandomGarnishWithCalories(calories, garnishList)
        randomGarnish?.let {
            val dishCalories = it.calories
            val dishGrams = dietActivityModel.calculateMainDishGrams(calories, dishCalories)
            return "${it.name}, ${dishGrams.toInt()} гр"
        }
        return ""
    }

    private fun generateRandomGarnir() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userQuery = databaseReference.child("users").child(userId)
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            val calories = dietActivityModel.calculateCalories(user)
                            val totalCalories = "$calories калорий."
                            caloriesTextView.text = totalCalories

                            databaseReference.child("dishes").child("breakfast").child("garnish")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val garnishList = mutableListOf<Garnish>()
                                            for (childSnapshot in dataSnapshot.children) {
                                                val garnish = childSnapshot.getValue(Garnish::class.java)
                                                garnish?.let { garnishList.add(it) }
                                            }

                                            val garnirText = generateRandomGarnir(calories, garnishList)
                                            GarnirB.text = garnirText
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun generateRandomGarnirL() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userQuery = databaseReference.child("users").child(userId)
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            val calories = dietActivityModel.calculateCalories(user)
                            val totalCalories = "$calories калорий."
                            caloriesTextView.text = totalCalories

                            databaseReference.child("dishes").child("lunch").child("garnish")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val garnishList = mutableListOf<Garnish>()
                                            for (childSnapshot in dataSnapshot.children) {
                                                val garnish = childSnapshot.getValue(Garnish::class.java)
                                                garnish?.let { garnishList.add(it) }
                                            }

                                            val garnirText = generateRandomGarnir(calories, garnishList)
                                            GarnirL.text = garnirText
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun generateRandomGarnirD() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userQuery = databaseReference.child("users").child(userId)
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            val calories = dietActivityModel.calculateCalories(user)
                            val totalCalories = "$calories калорий."
                            caloriesTextView.text = totalCalories

                            databaseReference.child("dishes").child("dinner").child("garnish")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val garnishList = mutableListOf<Garnish>()
                                            for (childSnapshot in dataSnapshot.children) {
                                                val garnish = childSnapshot.getValue(Garnish::class.java)
                                                garnish?.let { garnishList.add(it) }
                                            }

                                            val garnirText = generateRandomGarnir(calories, garnishList)
                                            GarnirD.text = garnirText
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun generateRandomMain() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userQuery = databaseReference.child("users").child(userId)
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            val calories = dietActivityModel.calculateCalories(user)
                            val totalCalories = "$calories калорий."
                            caloriesTextView.text = totalCalories

                            databaseReference.child("dishes").child("breakfast").child("meat")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val garnishList = mutableListOf<Garnish>()
                                            for (childSnapshot in dataSnapshot.children) {
                                                val garnish = childSnapshot.getValue(Garnish::class.java)
                                                garnish?.let { garnishList.add(it) }
                                            }

                                            val garnirText = generateRandomGarnir(calories, garnishList)
                                            MainB.text = garnirText
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun generateRandomMainL() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userQuery = databaseReference.child("users").child(userId)
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            val calories = dietActivityModel.calculateCalories(user)
                            val totalCalories = "$calories калорий."
                            caloriesTextView.text = totalCalories

                            databaseReference.child("dishes").child("lunch").child("meat")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val garnishList = mutableListOf<Garnish>()
                                            for (childSnapshot in dataSnapshot.children) {
                                                val garnish = childSnapshot.getValue(Garnish::class.java)
                                                garnish?.let { garnishList.add(it) }
                                            }

                                            val garnirText = generateRandomMainL(calories, garnishList)
                                            MainL.text = garnirText
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun generateRandomMainD() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userQuery = databaseReference.child("users").child(userId)
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            val calories = dietActivityModel.calculateCalories(user)
                            val totalCalories = "$calories калорий."
                            caloriesTextView.text = totalCalories

                            databaseReference.child("dishes").child("dinner").child("meat")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            val garnishList = mutableListOf<Garnish>()
                                            for (childSnapshot in dataSnapshot.children) {
                                                val garnish = childSnapshot.getValue(Garnish::class.java)
                                                garnish?.let { garnishList.add(it) }
                                            }

                                            val garnirText = generateRandomGarnir(calories, garnishList)
                                            MainD.text = garnirText
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun setupOnClickListeners() {
        generateRandomGarnir()
        generateRandomDop()
        generateRandomGarnirL()
        generateRandomGarnirD()
        generateRandomMain()
        generateRandomMainD()
        generateRandomMainL()
        generateRandomDrinkB()
        generateRandomDrinkL()
        generateRandomDrinkD()
    }

}