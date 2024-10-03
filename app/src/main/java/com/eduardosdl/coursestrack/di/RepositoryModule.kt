package com.eduardosdl.coursestrack.di

import com.eduardosdl.coursestrack.data.repository.AuthRepository
import com.eduardosdl.coursestrack.data.repository.AuthRepositoryFirebase
import com.eduardosdl.coursestrack.data.repository.CourseRepository
import com.eduardosdl.coursestrack.data.repository.CourseRepositoryFirebase
import com.eduardosdl.coursestrack.data.repository.InstitutionRepository
import com.eduardosdl.coursestrack.data.repository.InstitutionRepositoryFirebase
import com.eduardosdl.coursestrack.data.repository.MatterRepository
import com.eduardosdl.coursestrack.data.repository.MatterRepositoryFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        course: CourseRepository,
        institution: InstitutionRepository,
        matter: MatterRepository
    ): AuthRepository {
        return AuthRepositoryFirebase(auth, course, institution, matter)
    }

    @Provides
    @Singleton
    fun provideMatterRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): MatterRepository {
        return MatterRepositoryFirebase(firestore, auth)
    }

    @Provides
    @Singleton
    fun provideInstitutionRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): InstitutionRepository {
        return InstitutionRepositoryFirebase(firestore, auth)
    }

    @Provides
    @Singleton
    fun provideCourseRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): CourseRepository {
        return CourseRepositoryFirebase(firestore, auth)
    }

}