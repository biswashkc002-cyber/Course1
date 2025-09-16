package com.kcbiswash.course1.ui.dashboard

import com.google.gson.JsonObject
import com.kcbiswash.course1.data.MainRepository
import com.kcbiswash.course1.data.Result
import com.kcbiswash.course1.network.DashboardResponse
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

class DashboardViewModelTest {
    @Test
    fun dashboard_success() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)

        val repo = mockk<MainRepository>()
        val sample = listOf(JsonObject().apply {
            addProperty("courseCode", "CS101"); addProperty("courseName","Intro"); addProperty("description","d")
        })
        coEvery { repo.dashboard(any()) } returns Result.Success(
            DashboardResponse(entities = sample, entityTotal = sample.size)
        )

        val vm = DashboardViewModel(repo)
        vm.load("topicName")
        advanceUntilIdle()

        assertTrue(vm.state.value is Result.Success)
        Dispatchers.resetMain()
    }
}
