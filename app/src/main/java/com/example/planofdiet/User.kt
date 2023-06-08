package com.example.planofdiet

class User {
    var id: String? = null
    var name: String? = null
    var weight: Int? = null
    var age: Int? = null
    var height: Int? = null
    var gender: String? = null
    constructor()

    constructor(id: String?, name: String, weight: Int, age: Int, height: Int, gender: String?) {
        this.id = id
        this.name = name
        this.weight = weight
        this.age = age
        this.height = height
        this.gender = gender
    }
}
