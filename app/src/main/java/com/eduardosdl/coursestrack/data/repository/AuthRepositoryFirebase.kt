package com.eduardosdl.coursestrack.data.repository

import android.util.Log
import com.eduardosdl.coursestrack.util.UiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryFirebase @Inject constructor(
    val auth: FirebaseAuth,
    val course: CourseRepository,
    val institution: InstitutionRepository,
    val matter: MatterRepository
) : AuthRepository {
    val TAG: String = "AuthRepository"

    override fun registerUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("my-app", "Usuário criado com sucesso: ${auth.uid}")
                    result.invoke(UiState.Success("Registro realizado com sucesso"))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("Falha na criação"))
            }
    }

    override fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Login realizado com sucesso: ${auth.uid}")
                    result.invoke(UiState.Success("Login realizado com sucesso"))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("Falha na autenticação. Verifique email e senha"))
            }
    }

    override fun getSession(result: (id: String?) -> Unit) {
        Log.d("my-app", "Dados do user: ${auth.currentUser}")
        if (auth.currentUser == null) {
            result.invoke(null)
        } else {
            result.invoke(auth.uid)
        }
    }

    override fun logout(result: () -> Unit) {
        auth.signOut()
        result.invoke()
    }

    override suspend fun deleteUser(result: (UiState<String>) -> Unit) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            this.deleteUserData {
                if (it is UiState.Failure) {
                    result.invoke(UiState.Failure(it.error))
                    return@deleteUserData
                }

                currentUser.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            result.invoke(UiState.Success("Conta excluída com sucesso"))
                        } else {
                            result.invoke(UiState.Failure("Falha ao excluir a conta"))
                            Log.d(TAG, "firestore error to delete user")
                        }
                    }
                    .addOnFailureListener { e ->
                        result.invoke(UiState.Failure("Erro ao excluir a conta"))
                        Log.d(TAG, "firestore error to delete user: $e")
                    }
            }
        } else {
            Log.d(TAG, "firestore error to delete user")
            result.invoke(UiState.Failure("Nenhum usuário logado"))
        }
    }

    private suspend fun deleteUserData(result: (UiState<String>) -> Unit) = withContext(Dispatchers.IO) {
        try {
            course.deleteAllCourses { courseResult ->
                if (courseResult is UiState.Failure) {
                    throw Exception(courseResult.error)
                }
            }

            institution.deleteAllInstitutions { institutionResult ->
                if (institutionResult is UiState.Failure) {
                    throw Exception(institutionResult.error)
                }
            }

            matter.deleteAllMatters { matterResult ->
                if (matterResult is UiState.Failure) {
                    throw Exception(matterResult.error)
                }
            }

            result.invoke(UiState.Success("Dados do usuário excluídos com sucesso"))
        } catch (e: Exception) {
            result.invoke(UiState.Failure(e.message ?: "Erro ao excluir dados do usuário"))
        }
    }

}
