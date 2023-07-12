package com.demo.tenantandroidapp

data class Rider(
    val id: String,
    val name: String,
    val phone: String,
    val address: String,
    val location: Location
)

data class Location(val lat: Double, val long: Double)

