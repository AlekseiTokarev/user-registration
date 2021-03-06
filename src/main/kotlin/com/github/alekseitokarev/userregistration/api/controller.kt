package com.github.alekseitokarev.userregistration.api

import com.github.alekseitokarev.userregistration.dao.UserRepository
import com.github.alekseitokarev.userregistration.domain.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/users")
class UserController(val userRepository: UserRepository) {

    @GetMapping("/{id}/details")
    fun getUserDetails(@PathVariable id: Long): ResponseEntity<UserDetailsResponse> {
        val user: User = userRepository.findById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(convertToUserDetailsResponse(user))
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Any> {
        userRepository.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/existsByEmail")
    fun isUserExists(@RequestParam email: String): ResponseEntity<Map<String, Boolean>> {
        val userFound: Boolean = userRepository.existByEmail(email)
        val response = mapOf("userExist" to userFound)
        return if (userFound) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity(response, HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Updates a user if exists, otherwise creates a new one(with provided id if passed).
     */
    @PostMapping
    fun upsertUser(@RequestBody request: UserDto): ResponseEntity<UserDto> {
        val upsertedUser: User = if (request.id == null) {
            userRepository.insert(request.toUser())
        } else {
            userRepository.upsert(request.toUser())
        }
        return ResponseEntity.created(URI.create("/api/users/${upsertedUser.id}"))
            .body(convertToUserDto(upsertedUser))
    }

    @PostMapping("/{id}/address")
    fun updateAddress(@PathVariable id: Long, @RequestBody request: AddressDto): ResponseEntity<UserDetailsResponse> {
        val user = userRepository.updateAddress(id, request.toAddress())
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(convertToUserDetailsResponse(user))
    }

    //TODO List of users
    @GetMapping
    fun getUsers(@RequestParam(required = false, defaultValue = "50") limit: Long,
                 @RequestParam(required = false, defaultValue = "0") offset: Long): ResponseEntity<List<UserDto>> {
        //should be a pretty trivial select with limit/offset
        return ResponseEntity.ok(emptyList())
    }

}






