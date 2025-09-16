package com.kcbiswash.course1.data

import com.kcbiswash.course1.network.ApiService
import com.kcbiswash.course1.network.DashboardResponse
import com.kcbiswash.course1.network.LoginRequest
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun login(campus: String, username: String, password: String): Result<String> {
        return try {
            if (campus !in listOf("br", "sydney", "footscray")) {
                Result.Error("Campus must be br / sydney / footscray")
            } else {
                val res = api.login(campus, LoginRequest(username, password))
                Result.Success(res.keypass)
            }
        } catch (e: SocketTimeoutException) {
            Result.Error("Request timed out. Please try again.")
        } catch (e: IOException) {
            Result.Error("Network error: ${e.message ?: "check your internet"}")
        } catch (e: Exception) {
            Result.Error(e.message ?: "Login failed")
        }
    }

    suspend fun dashboard(keypass: String): Result<DashboardResponse> {
        return try {
            Result.Success(api.dashboard(keypass))
        } catch (e: SocketTimeoutException) {
            Result.Error("Request timed out. Please pull to refresh.")
        } catch (e: IOException) {
            Result.Error("Network error: ${e.message ?: "check your internet"}")
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to load dashboard")
        }
    }
}
