package com.eduardosdl.coursestrack.data.repository

import android.util.Log
import com.eduardosdl.coursestrack.data.model.Course
import com.eduardosdl.coursestrack.data.model.Institution
import com.eduardosdl.coursestrack.data.model.Matter
import com.eduardosdl.coursestrack.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import javax.inject.Inject

class CourseRepositoryFirebase(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : CourseRepository {
    override fun createCourse(
        course: Course,
        institution: Institution,
        matter: Matter,
        result: (UiState<Course>) -> Unit
    ) {
        val document = firestore.collection("courses").document()
        val institutionRef = institution.id?.let {
            firestore.collection("institutions").document(it)
        }
        val matterRef = matter.id?.let {
            firestore.collection("matters").document(it)
        }
        val newCourse = course.copy(
            id = document.id,
            userId = auth.uid!!,
            progress = 0,
            institutionRef = institutionRef,
            matterRef = matterRef,
            matterName = matter.name,
            institutionName = institution.name
        )

        document.set(newCourse).addOnSuccessListener {
            result.invoke(UiState.Success(newCourse))
        }.addOnFailureListener { e ->
            result.invoke(UiState.Failure("Houve um erro na crição do curso, tente novamente mais tarde"))
            Log.d("my-app-erros", "firestore error to course course: $e")
        }
    }

    override fun getAllCourses(result: (UiState<List<Course>>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return result(UiState.Failure("User ID not found"))

        firestore.collection("courses")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val courses = mutableListOf<Course>()
                for (document in querySnapshot.documents) {
                    val course = document.toObject(Course::class.java)
                    if (course != null) {
                        courses.add(course)
                    }
                }
                result.invoke(UiState.Success(courses))
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure("Houve um erro ao buscar cursos"))
                Log.d("my-app-erros", "firestore error to get courses: $e ")
            }
    }

    override fun updateCourseProgress(
        course: Course,
        additionalProgress: Long,
        result: (UiState<String>) -> Unit
    ) {
        val courseRef = firestore.collection("courses").document(course.id!!)

        val currentProgress = course.progress ?: 0L
        val newProgress = currentProgress + additionalProgress

        if (course.duration != null && newProgress > course.duration.toLong()) {
            result.invoke(UiState.Failure("O progresso não pode ser maior que a duração do curso"))
            return
        }

        courseRef.update("progress", newProgress)
            .addOnSuccessListener {
                result.invoke(UiState.Success("Progresso atualizado"))
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure("Houve um erro na atualização do progresso, tente novamente mais tarde"))
                Log.d("my-app-erros", "firestore error to update course progress: $e")
            }
    }

    override fun getCourse(courseId: String, result: (UiState<Course>) -> Unit) {
        firestore.collection("courses")
            .document(courseId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    result.invoke(UiState.Success(document.toObject(Course::class.java)!!))
                } else {
                    result.invoke(UiState.Failure("Curso não encontrado"))
                }
            }
            .addOnFailureListener { e ->
                if (e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                    Log.d("my-app-error", "Permission denied to access document")
                } else {
                    Log.d("my-app-error", "Error getting document: $e")
                }
            }
    }

    override fun deleteCourse(course: Course, result: (UiState<String>) -> Unit) {

        val courseRef = firestore.collection("courses").document(course.id!!)

        courseRef.delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("Curso ${course.name} excluído com sucesso"))
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure("Houve um erro ao deletar o curso ${course.name}, tente novamente mais tarde"))
                Log.d("my-app-erros", "firestore error to delete course: $e")
            }
    }

    override fun updateCourse(
        course: Course,
        matter: Matter,
        institution: Institution,
        result: (UiState<Course>) -> Unit
    ) {
        val courseRef = firestore.collection("courses").document(course.id!!)
        val institutionRef = institution.id?.let {
            firestore.collection("institutions").document(it)
        }
        val matterRef = matter.id?.let {
            firestore.collection("matters").document(it)
        }

        val updatedCourse = course.copy(
            institutionRef = institutionRef,
            matterRef = matterRef,
            matterName = matter.name,
            institutionName = institution.name
        )

        courseRef.set(updatedCourse)
            .addOnSuccessListener {
                result.invoke(UiState.Success(updatedCourse))
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure("Houve um erro na atualização do curso, tente novamente mais tarde"))
                Log.d("my-app-erros", "firestore error to update course: $e")
            }
    }

    override fun deleteAllCourses(result: (UiState<String>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return result(UiState.Failure("User ID not found"))

        firestore.collection("courses")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val batch = firestore.batch()
                for (document in querySnapshot.documents) {
                    batch.delete(document.reference)
                }
                batch.commit()
                    .addOnSuccessListener {
                        result.invoke(UiState.Success("Cursos excluidos com sucesso"))
                    }
                    .addOnFailureListener { e ->
                        result.invoke(UiState.Failure("Erro ao excluir os cursos, tente novamente mais tarde"))
                        Log.d("my-app-erros", "Erro ao excluir cursos: $e")
                    }
            }
            .addOnFailureListener { e ->
                result.invoke(UiState.Failure("Erro ao buscar cursos, tente novamente mais tarde"))
                Log.d("my-app-erros", "Erro ao buscar cursos para exclusão: $e")
            }
    }
}