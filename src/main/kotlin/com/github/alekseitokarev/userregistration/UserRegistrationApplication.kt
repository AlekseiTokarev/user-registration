package com.github.alekseitokarev.userregistration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserRegistrationApplication

fun main(args: Array<String>) {
    runApplication<UserRegistrationApplication>(*args)
}
