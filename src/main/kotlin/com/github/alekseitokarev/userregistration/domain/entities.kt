package com.github.alekseitokarev.userregistration.domain

class User(
    var id: Long?,
    val firstName: String,
    val lastName: String,
    val email: String
) {
    var addresses: MutableList<Address> = ArrayList()
}

class Address(
    var id: Long?,
    val line1: String,
    var line2: String?,
    val city: String,
    val state: String,
    val zip: String
)
