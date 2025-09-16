// app/src/main/java/com/kcbiswash/course1/ui/login/LoginViewModel.kt
package com.kcbiswash.course1.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kcbiswash.course1.data.MainRepository
import com.kcbiswash.course1.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: MainRepository
) : ViewModel() {

    private val _state = MutableStateFlow<Result<String>>(Result.Idle)
    val state: StateFlow<Result<String>> = _state

    fun doLogin(campus: String, username: String, password: String) {
        _state.value = Result.Loading
        viewModelScope.launch {
            _state.value = repo.login(campus, username, password)
        }
    }

    /** Call this after navigation so returning to Login won't relaunch Dashboard. */
    fun clearState() {
        _state.value = Result.Idle
    }
}
