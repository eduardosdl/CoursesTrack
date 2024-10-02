package com.eduardosdl.coursestrack.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Course(
    val id: String? = null,
    val name: String = "",
    val userId: String = "",
    val durationType: String = "",
    val duration: Long? = null,
    val progress: Long? = null,
    val institutionName: String? = null,
    val matterName: String? = null,
    val institutionRef: @RawValue DocumentReference? = null,
    val matterRef: @RawValue DocumentReference? = null
): Parcelable
