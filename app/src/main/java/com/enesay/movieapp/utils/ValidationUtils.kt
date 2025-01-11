package com.enesay.movieapp.utils

object ValidationUtils {

    fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "Name cannot be empty"
            name.length < 2 -> "Name must be at least 2 characters"
            !name.all { it.isLetter() || it.isWhitespace() } -> "Name can only contain letters and spaces"
            else -> null
        }
    }

    fun validateSurname(surname: String): String? {
        return when {
            surname.isBlank() -> "Surname cannot be empty"
            surname.length < 2 -> "Surname must be at least 2 characters"
            !surname.all { it.isLetter() || it.isWhitespace() } -> "Surname can only contain letters and spaces"
            else -> null
        }
    }

    fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "Email cannot be empty"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email format"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        val errors = mutableListOf<String>()

        if (password.isBlank()) return "Password cannot be empty"

        if (password.length < 8) errors.add("8 characters")
        if (!password.any { it.isUpperCase() }) errors.add("one uppercase letter")
        if (!password.any { it.isLowerCase() }) errors.add("one lowercase letter")
        if (!password.any { it.isDigit() }) errors.add("one digit")
        if (!password.any { "!@#\$%^&+=.".contains(it) }) errors.add("one special character (!@#\$%^&+=.)")

        return if (errors.isEmpty()) null else "Password must contain at least ${errors.joinToString(", ")}"
    }

}