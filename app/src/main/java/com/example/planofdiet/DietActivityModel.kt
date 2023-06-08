package com.example.planofdiet

class DietActivityModel {

    fun calculateCalories(user: User): Int {
        return if (user.gender == "Мужской") {
            (88.362 + (13.397 * user.weight!!) + (4.799 * user.height!!) - (5.677 * user.age!!)).toInt()
        } else {
            (447.593 + (9.247 * user.weight!!) + (3.098 * user.height!!) - (4.330 * user.age!!)).toInt()
        }
    }



    fun calculateTotalCalories(calories: Int, percentage: Double): Int {
        return (calories * percentage).toInt()
    }

    fun calculateDishGrams(calories: Int, dishCalories: Int): Double {
        return ((calories * 0.1) / dishCalories) * 100
    }

    fun calculateMainDishGrams(calories: Int, dishCalories: Int): Double {
        return ((calories * 0.25) / dishCalories) * 100
    }

    fun calculateMainDopGrams(calories: Int, dishCalories: Int): Double {
        return ((calories * 0.15) / dishCalories) * 100
    }

    fun calculateDrink(calories: Int, dishCalories: Int): Double {
        return ((calories * 0.05) / dishCalories) * 100
    }

    fun getRandomGarnishWithCalories(calories: Int, garnishList: List<Garnish>): Garnish? {
        val filteredGarnishList = garnishList.filter { it.calories <= calories }
        return filteredGarnishList.randomOrNull()
    }
}