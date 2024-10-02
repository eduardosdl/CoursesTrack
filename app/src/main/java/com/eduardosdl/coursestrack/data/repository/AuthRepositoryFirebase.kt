package com.eduardosdl.coursestrack.data.repository

import android.util.Log
import com.eduardosdl.coursestrack.util.UiState
import com.google.firebase.auth.FirebaseAuth

class AuthRepositoryFirebase (val auth: FirebaseAuth): AuthRepository {
    val TAG: String = "AuthRepository"

    override fun registerUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        auth.createUserWithEmailAndPassword(email,password)
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
}
