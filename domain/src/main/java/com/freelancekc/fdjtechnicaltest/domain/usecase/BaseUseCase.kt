package com.freelancekc.fdjtechnicaltest.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * Base class for all use cases that return Flow<Result<T>>
 */
abstract class FlowUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    
    protected abstract suspend fun execute(parameters: P): Flow<Result<R>>
    
    suspend operator fun invoke(parameters: P): Flow<Result<R>> {
        return execute(parameters).flowOn(coroutineDispatcher)
    }
}

/**
 * Base class for use cases that don't require parameters
 */
abstract class NoParameterFlowUseCase<R>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    
    protected abstract suspend fun execute(): Flow<Result<R>>
    
    suspend operator fun invoke(): Flow<Result<R>> {
        return execute().flowOn(coroutineDispatcher)
    }
}
