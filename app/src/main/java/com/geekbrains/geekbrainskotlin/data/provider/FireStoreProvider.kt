package com.geekbrains.geekbrainskotlin.data.provider

import android.util.Log
import com.geekbrains.geekbrainskotlin.data.errors.NoAuthException
import com.geekbrains.geekbrainskotlin.data.model.Note
import com.geekbrains.geekbrainskotlin.data.model.Result
import com.geekbrains.geekbrainskotlin.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreProvider(private val firebaseAuth: FirebaseAuth,
                        private val db: FirebaseFirestore) : RemoteDataProvider {

    private val TAG = "${FireStoreProvider::class.java.simpleName} :"

    private val currentUser
        get() = firebaseAuth.currentUser

    private fun getUserNotesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override fun subscribeToAllNotes(): ReceiveChannel<Result> =
            Channel<Result>(Channel.CONFLATED).apply {
                var registration: ListenerRegistration? = null

                try {
                    registration =
                            getUserNotesCollection().addSnapshotListener { snapshot, e ->
                                val value = e?.let {
                                    Result.Error(it)
                                } ?: snapshot?.let {
                                    val notes = it.documents.map {
                                        it.toObject(Note::class.java)
                                    }
                                    Result.Success(notes)
                                }

                                value?.let { offer(it) }
                            }
                } catch (e: Throwable) {
                    offer(Result.Error(e))
                }

                invokeOnClose { registration?.remove() }
            }

    override suspend fun saveNote(note: Note): Note = suspendCoroutine { continuation ->
        try {
            getUserNotesCollection().document(note.id)
                    .set(note).addOnSuccessListener {
                        Log.d(TAG, "Note $note is saved")
                        continuation.resume(note)
                    }.addOnFailureListener {
                        Log.d(TAG, "Error saving note $note, message: ${it.message}")
                        continuation.resumeWithException(it)
                    }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun getNoteById(id: String): Note =
            suspendCoroutine { continuation ->
                try {
                    getUserNotesCollection().document(id).get()
                            .addOnSuccessListener {
                                continuation.resume(it.toObject(Note::class.java)!!)
                            }.addOnFailureListener {
                                continuation.resumeWithException(it)
                            }
                } catch (e: Throwable) {
                    continuation.resumeWithException(e)
                }
            }

    override suspend fun getCurrentUser(): User? = suspendCoroutine { continuation ->
        currentUser?.let {
            User(it.displayName ?: "", it.email ?: "")
        }.let {
            continuation.resume(it)
        }
    }

    override suspend fun deleteNote(noteId: String): Unit = suspendCoroutine { continuation ->
        getUserNotesCollection().document(noteId).delete()
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
    }
}

