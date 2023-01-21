package com.example.hideandseek.fake

import com.example.hideandseek.data.repository.ApiRepositoryImpl
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ApiRepositoryTest {
    @Test
    fun apiRepository_getTest_verifyGetTest() = runTest {
        val repository = ApiRepositoryImpl(
            FakeRestApi()
        )
        assertEquals(FakeDataSource.fakeResponseGetTest, repository.getTest())
    }
}