package com.github.alekseitokarev.userregistration.api

import com.github.alekseitokarev.userregistration.domain.User

data class UserDetailsResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String
) {
    var address: AddressDto? = null
}

class AddressDto(
    val line1: String,
    var line2: String?,
    val city: String,
    val state: String,
    val zip: String
)

data class UserDto(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val email: String
) {
    fun toUser() = User(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email
    )
}

fun convertToUserDto(user: User): UserDto =
    UserDto(
        id = user.id,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email
    )

fun convertToUserDetailsResponse(user: User): UserDetailsResponse {
    val response = UserDetailsResponse(
        id = user.id!!,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email
    )
    val address = user.addresses.firstOrNull() ?: return response
    response.address = AddressDto(
        line1 = address.line1,
        line2 = address.line2,
        city = address.city,
        state = address.state,
        zip = address.zip,
    )
    return response
}

