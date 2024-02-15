package com.personal.assignment.model

data class Car(
    val model: String,
    val make: String,
    val car_image: String?,
    val car_price: Int,
    val rating: Double,
    val cons_list: List<String>,
    val pros_list: List<String>
)