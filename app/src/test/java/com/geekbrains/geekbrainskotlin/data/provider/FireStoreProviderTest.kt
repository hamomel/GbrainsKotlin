package com.geekbrains.geekbrainskotlin.data.provider

import com.geekbrains.geekbrainskotlin.data.errors.NoAuthException
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.data.model.Result
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class FireStoreProviderTest {

    private val mockDb = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockCollection = mockk<CollectionReference>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()
    private val testNotes = listOf(Note(id = "1"), Note(id = "2"), Note(id = "3"))

    private val provider: FireStoreProvider = FireStoreProvider(mockAuth, mockDb)

    @Before
    fun setUp() {
        clearMocks(mockCollection, mockDocument1, mockDocument2, mockDocument3)

        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns ""
        every { mockDb.collection(any()).document(any()).collection(any()) } returns mockCollection
        every { mockDocument1.toObject(Note::class.java) } returns testNotes[0]
        every { mockDocument2.toObject(Note::class.java) } returns testNotes[1]
        every { mockDocument3.toObject(Note::class.java) } returns testNotes[2]
    }

    @Test
    fun `should throw if no auth`() = runBlocking {
        var result: Any? = null
        every { mockAuth.currentUser } returns null

        result = async(Dispatchers.Default) {
            provider.subscribeToAllNotes().consumeEach {
                return@async (it as Result.Error).error
            }
        }.await()

        assertTrue(result is NoAuthException)
    }

    @Ignore
    @Test
    fun `subscribeAllNotes return notes`() = runBlocking {
        var result: Any
        val slot = slot<EventListener<QuerySnapshot>>()
        val mockSnapshot = mockk<QuerySnapshot>()

        every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)
        every { mockCollection.addSnapshotListener(capture(slot)) } returns mockk()

        result = async(Dispatchers.Default) {
            provider.subscribeToAllNotes().consumeEach {
                return@async (it as Result.Success<List<Note>>).data
            }
        }.await()

        slot.captured.onEvent(mockSnapshot, null)

        assertEquals(testNotes, result)
    }

    @Ignore
    @Test
    fun `subscribeAllNotes return error`() = runBlocking {
        var result: Throwable? = null
        val slot = slot<EventListener<QuerySnapshot>>()
        val testError = mockk<FirebaseFirestoreException>()

        every { mockCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllNotes().consumeEach { result = (it as? Result.Error)?.error }

        slot.captured.onEvent(null, testError)

        assertNotNull(result)
        assertEquals(testError, result)
    }

    @Test
    fun `saveNote calls document set`() = runBlocking {
        val mockDocumentReference: DocumentReference = mockk(relaxed = true)
        every { mockCollection.document(testNotes[0].id) } returns mockDocumentReference
        provider.saveNote(testNotes[0])

        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }

    @Test
    fun `saveNote return Note`() = runBlocking {
        val mockDocumentReference: DocumentReference = mockk()
        val slot = slot<OnSuccessListener<in Void>>()
        var result: Note? = null

        every { mockCollection.document(testNotes[0].id) } returns mockDocumentReference
        every { mockDocumentReference.set(testNotes[0]).addOnSuccessListener(capture(slot)) } returns mockk()

        result = provider.saveNote(testNotes[0])
        slot.captured.onSuccess(null)

        assertNotNull(result)
        assertEquals(testNotes[0], result)
    }
}