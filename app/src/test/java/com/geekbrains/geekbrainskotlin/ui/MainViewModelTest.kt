package com.geekbrains.geekbrainskotlin.ui

import com.geekbrains.geekbrainskotlin.data.Repository
import com.geekbrains.geekbrainskotlin.data.errors.NoAuthException
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.data.model.Result
import com.geekbrains.geekbrainskotlin.ui.main.MainViewModel
import io.mockk.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

//    @get:Rule
//    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepository: Repository = mockk()
    private val notesChannel = Channel<Result>()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        coEvery { mockRepository.getNotes() } returns notesChannel
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should call getNotes once`() = runBlocking {
        coVerify(exactly = 1) { mockRepository.getNotes() }
    }

//    @Test
//    fun `should return error`() {
//        var result: Throwable? = null
//        val testData = Throwable("error")
//        viewModel.getViewState().observeForever { result = it?.error }
//        notesChannel.value = Result.Error(testData)
//        assertEquals(result, testData)
//    }
//
//    @Test
//    fun `should return Notes`() {
//        var result: List<Note>? = null
//        val testData = listOf(Note(id = "1"), Note(id = "2"))
//        viewModel.getViewState().observeForever { result = it?.data}
//        notesChannel.value = Result.Success(testData)
//        assertEquals(testData, result)
//    }
//
//    @Test
//    fun `should remove observer`() {
//        viewModel.onCleared()
//        assertFalse(notesChannel.hasObservers())
//    }
//
//    @Test
//    fun `should send exceptio on logout`() {
//        var result: Any? = null
//        viewModel.getViewState().observeForever { result = it?.error }
//        viewModel.logout()
//        assertTrue(result is NoAuthException)
//    }
}