package com.enesay.movieapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.enesay.movieapp.utils.ValidationUtils
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterPageValidationTest {

    @Test
    fun validate_name_returns_error_when_name_is_empty() {
        val result = ValidationUtils.validateName("")
        assertEquals("Name cannot be empty", result)
    }

    @Test
    fun validate_name_returns_error_when_name_is_less_than_2_characters() {
        val result = ValidationUtils.validateName("A")
        assertEquals("Name must be at least 2 characters", result)
    }

    @Test
    fun validate_name_returns_error_when_name_contains_invalid_characters() {
        val result = ValidationUtils.validateName("John123")
        assertEquals("Name can only contain letters and spaces", result)
    }

    @Test
    fun validate_name_returns_null_for_valid_name() {
        val result = ValidationUtils.validateName("John Doe")
        assertEquals(null, result)
    }

    @Test
    fun validate_surname_returns_error_when_surname_is_empty() {
        val result = ValidationUtils.validateSurname("")
        assertEquals("Surname cannot be empty", result)
    }

    @Test
    fun validate_surname_returns_error_when_surname_is_less_than_2_characters() {
        val result = ValidationUtils.validateSurname("B")
        assertEquals("Surname must be at least 2 characters", result)
    }

    @Test
    fun validate_surname_returns_error_when_surname_contains_invalid_characters() {
        val result = ValidationUtils.validateSurname("Smith123")
        assertEquals("Surname can only contain letters and spaces", result)
    }

    @Test
    fun validate_surname_returns_null_for_valid_surname() {
        val result = ValidationUtils.validateSurname("Smith")
        assertEquals(null, result)
    }

    @Test
    fun validate_email_returns_error_when_email_is_empty() {
        val result = ValidationUtils.validateEmail("")
        assertEquals("Email cannot be empty", result)
    }

    @Test
    fun validate_email_returns_error_when_email_is_invalid() {
        val result = ValidationUtils.validateEmail("invalid_email")
        assertEquals("Invalid email format", result)
    }

    @Test
    fun validate_email_returns_null_for_valid_email() {
        val result = ValidationUtils.validateEmail("test@example.com")
        assertEquals(null, result)
    }

    @Test
    fun validate_password_returns_error_when_password_is_empty() {
        val result = ValidationUtils.validatePassword("")
        assertEquals("Password cannot be empty", result)
    }

    @Test
    fun validate_password_returns_error_when_password_does_not_meet_requirements() {
        val result = ValidationUtils.validatePassword("Pass12")
        assertEquals("Password must contain at least 8 characters, one special character (!@#\$%^&+=.)", result)
    }

    @Test
    fun validate_password_returns_error_for_missing_uppercase_and_special_character() {
        val result = ValidationUtils.validatePassword("password1")
        assertEquals("Password must contain at least one uppercase letter, one special character (!@#\$%^&+=.)", result)
    }

    @Test
    fun validate_password_returns_null_for_valid_password() {
        val result = ValidationUtils.validatePassword("Password1!")
        assertEquals(null, result)
    }
}

