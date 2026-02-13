package com.example.examen08_02.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel() : ViewModel() {
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isLoginEnabled = MutableLiveData<Boolean>()
    val isLoginEnabled: LiveData<Boolean> = _isLoginEnabled

    private fun validCredentials() {
        _isLoginEnabled.value = _password.value.length > 7 &&
                Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
    }


    fun onLoginChange(newMail: String, newPass: String) {
        _email.value = newMail
        _password.value = newPass
        validCredentials()
    }
}