package com.eduardosdl.coursestrack.data.repository

import android.util.Log
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class InstitutionRepositoryFirebase(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : InstitutionRepository {
    override fun getAllInstitutionsByUser(result: (UiState<List<Institution>>) -> Unit) {
        firestore.collection("institutions")
            .whereEqualTo("userId", auth.uid!!)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val institutions = mutableListOf<Institution>()
                for (document in querySnapshot.documents) {
                    val institution = document.toObject(Institution::class.java)
                    if (institution != null) {
                        institutions.add(institution)
                    }
                }
                result.invoke(UiState.Success(institutions))
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure("Houve um erro na consulta das matérias"))
                Log.d("my-app-erros", "firestore error to get institution: $e")
            }
    }

    override fun createInstitution(
        name: String,
        result: (UiState<Institution>) -> Unit
    ) {
        val document = firestore.collection("institutions").document()
        val newInstitution = Institution(document.id, name, auth.uid!!)

        document.set(newInstitution)
            .addOnSuccessListener {
                result.invoke(UiState.Success(newInstitution))
            }.addOnFailureListener { e ->
                result.invoke(UiState.Failure("Houve um erro na criação da matéria"))
                Log.d("my-app-erros", "firestore error to course institution: $e")
            }
    }

    override fun deleteInstitution(institutionId: String, result: (UiState<String>) -> Unit) {
        val institutionRef = firestore.collection("institutions").document(institutionId)
        institutionRef.delete()
            .addOnSuccessListener {
                result(UiState.Success("Institution deleted successfully"))
            }
            .addOnFailureListener { e ->
                result(UiState.Failure("Failed to delete institution"))
                Log.d("my-app-errors", "Firestore error to delete institution: $e")
            }
    }

    override fun updateInstitutionName(
        institution: Institution,
        newName: String,
        result: (UiState<String>) -> Unit
    ) {
        if (institution.id != null) {
            firestore.collection("institutions").document(institution.id)
                .update("name", newName)
                .addOnSuccessListener {
                    result(UiState.Success("Institution name updated successfully"))
                }
                .addOnFailureListener { e ->
                    result(UiState.Failure("Failed to update institution name"))
                    Log.d("my-app-errors", "Firestore error to update institution name: $e")
                }
        } else {
            result(UiState.Failure("Institution ID not found"))
        }
    }

    override fun deleteAllInstitutions(result: (UiState<String>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return result.invoke(UiState.Failure("User ID não encontrado"))

        firestore.collection("institutions")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val batch = firestore.batch()
                for (document in querySnapshot.documents) {
                    batch.delete(document.reference)
                }
                batch.commit()
                    .addOnSuccessListener {
                        result.invoke(UiState.Success("Instituições excluidas com sucesso"))
                    }
                    .addOnFailureListener { e ->
                        result.invoke(UiState.Failure("Erro ao excluir os instituições, tente novamente mais tarde"))
                        Log.d("my-app-erros", "Erro ao excluir instituições: $e")
                    }
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure("Erro ao buscar instituições, tente novamente mais tarde"))
                Log.d("my-app-erros", "Erro ao buscar instituições para exclusão: $e")
            }
    }
}