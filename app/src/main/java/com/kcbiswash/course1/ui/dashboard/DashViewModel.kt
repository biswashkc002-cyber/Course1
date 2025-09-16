package com.kcbiswash.course1.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repo: com.kcbiswash.course1.data.MainRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow<com.kcbiswash.course1.data.Result<com.kcbiswash.course1.network.DashboardResponse>>(
            com.kcbiswash.course1.data.Result.Loading
        )

    val state: StateFlow<com.kcbiswash.course1.data.Result<com.kcbiswash.course1.network.DashboardResponse>>
        get() = _state

    fun load(keypass: String) {
        _state.value = com.kcbiswash.course1.data.Result.Loading
        viewModelScope.launch {
            _state.value = repo.dashboard(keypass)
        }
    }

    companion object {
        fun summaryFor(item: JsonObject): String =
            item.entrySet()
                .filter { it.key != "description" }
                .joinToString("\n") { "${it.key}: ${it.value.asStringOrRaw()}" }
                .ifEmpty { "(No summary fields)" }

        fun JsonObject.asFullMap(): List<Pair<String, String>> =
            entrySet().map { it.key to it.value.asStringOrRaw() }

        fun JsonElement.asStringOrRaw(): String =
            if (isJsonPrimitive && asJsonPrimitive.isString) asString else toString()
    }
}
