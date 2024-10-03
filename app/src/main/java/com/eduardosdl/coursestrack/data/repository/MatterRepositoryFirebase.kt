package com.eduardosdl.coursestrack.data.repository

import android.util.Log
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class MatterRepositoryFirebase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : MatterRepository {

    override fun getAllMattersByUser(result: (UiState<List<Matter>>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return result(UiState.Failure("User ID not found"))

        firestore.collection("matters")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val matters = mutableListOf<Matter>()
                for (document in querySnapshot.documents) {
                    val matter = document.toObject(Matter::class.java)
                    if (matter != null) {
                        matters.add(matter)
                    }
                }
                result(UiState.Success(matters))
            }
            .addOnFailureListener { e ->
                result(UiState.Failure(e.message ?: "Failed to get matters"))
                Log.d("my-app-errors", "Firestore error: $e")
            }
    }

    override fun createMatter(name: String, result: (UiState<Matter>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return result(UiState.Failure("User ID not found"))
        val document = firestore.collection("matters").document()
        val matter = Matter(
            id = document.id,
            name = name,
            userId = userId
        )

        document
            .set(matter)
            .addOnSuccessListener {
                result(UiState.Success(matter))
            }
            .addOnFailureListener { e ->
                result(UiState.Failure(e.message ?: "Failed to create matter"))
                Log.d("my-app-errors", "Firestore error: $e")
            }
    }

    override fun deleteMatter(matterId: String, result: (UiState<String>) -> Unit) {
        firestore.collection("matters").document(matterId)
            .delete()
            .addOnSuccessListener {
                result(UiState.Success("Matter deleted successfully"))
            }
            .addOnFailureListener { e ->
                result(UiState.Failure("Failed to delete matter"))
                Log.d("my-app-errors", "Firestore error to delete matter: $e")
            }
    }

    override fun updateMatterName(matter: Matter, newName: String, result: (UiState<String>) -> Unit) {
        if (matter.id != null) {
            firestore.collection("matters").document(matter.id)

                .update("name", newName)
                .addOnSuccessListener {
                    result(UiState.Success("Matter name updated successfully"))
                }
                .addOnFailureListener { e ->
                    result(UiState.Failure("Failed to update matter name"))
                    Log.d("my-app-errors", "Firestore error to update matter name: $e")
                }
        } else {
            result(UiState.Failure("Matter ID not found"))
        }
    }

    override fun deleteAllMatters(result: (UiState<String>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return result.invoke(UiState.Failure("User ID não encontrado"))

        firestore.collection("matters")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val batch = firestore.batch()
                for (document in querySnapshot.documents) {
                    batch.delete(document.reference)
                }
                batch.commit()
                    .addOnSuccessListener {
                        result.invoke(UiState.Success("Matérias excluidas com sucesso"))
                    }
                    .addOnFailureListener { e ->
                        result.invoke(UiState.Failure("Erro ao matérias os cursos, tente novamente mais tarde"))
                        Log.d("my-app-erros", "Erro ao excluir matérias: $e")
                    }
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure("Erro ao buscar matérias, tente novamente mais tarde"))
                Log.d("my-app-erros", "Erro ao buscar matérias para exclusão: $e")
            }
    }
}
