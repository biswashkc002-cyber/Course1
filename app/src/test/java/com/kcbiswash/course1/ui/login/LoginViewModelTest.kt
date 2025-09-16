package com.kcbiswash.course1.ui.login

import com.kcbiswash.course1.data.MainRepository
import com.kcbiswash.course1.data.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginViewModelTest {
    @Test
    fun login_success() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val repo = mockk<MainRepository>()
        coEvery { repo.login(any(), any(), any()) } returns Result.Success("topicName")

        val vm = LoginViewModel(repo)
        vm.doLogin("br", "Alice", "12345678")
        advanceUntilIdle()

        assertTrue(vm.state.value is Result.Success)
        Dispatchers.resetMain()
    }
}
