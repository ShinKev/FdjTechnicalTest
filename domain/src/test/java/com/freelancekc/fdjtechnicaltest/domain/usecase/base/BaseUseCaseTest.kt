package com.freelancekc.fdjtechnicaltest.domain.usecase.base

import app.cash.turbine.test
import com.freelancekc.fdjtechnicaltest.domain.usecase.FlowUseCase
import com.freelancekc.fdjtechnicaltest.domain.usecase.NoParameterFlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class BaseUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `FlowUseCase executes on correct dispatcher`() = runTest {
        // Given
        val testData = "test data"
        val useCase = TestFlowUseCase(testData)

        // When & Then
        useCase("input").test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(testData, result.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `NoParameterFlowUseCase executes on correct dispatcher`() = runTest {
        // Given
        val testData = "test data"
        val useCase = TestNoParameterFlowUseCase(testData)

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals(testData, result.getOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `FlowUseCase handles exceptions properly`() = runTest {
        // Given
        val exception = RuntimeException("Test exception")
        val useCase = TestFlowUseCaseWithException(exception)

        // When & Then
        useCase("input").test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            awaitComplete()
        }
    }

    @Test
    fun `NoParameterFlowUseCase handles exceptions properly`() = runTest {
        // Given
        val exception = RuntimeException("Test exception")
        val useCase = TestNoParameterFlowUseCaseWithException(exception)

        // When & Then
        useCase().test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            awaitComplete()
        }
    }

    private class TestFlowUseCase(
        private val testData: String
    ) : FlowUseCase<String, String>() {
        override suspend fun execute(parameters: String): Flow<Result<String>> {
            return flowOf(Result.success(testData))
        }
    }

    private class TestNoParameterFlowUseCase(
        private val testData: String
    ) : NoParameterFlowUseCase<String>() {
        override suspend fun execute(): Flow<Result<String>> {
            return flowOf(Result.success(testData))
        }
    }

    private class TestFlowUseCaseWithException(
        private val exception: Exception
    ) : FlowUseCase<String, String>() {
        override suspend fun execute(parameters: String): Flow<Result<String>> {
            return flowOf(Result.failure(exception))
        }
    }

    private class TestNoParameterFlowUseCaseWithException(
        private val exception: Exception
    ) : NoParameterFlowUseCase<String>() {
        override suspend fun execute(): Flow<Result<String>> {
            return flowOf(Result.failure(exception))
        }
    }
}
