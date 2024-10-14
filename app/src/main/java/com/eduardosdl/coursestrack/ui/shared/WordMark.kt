package com.eduardosdl.coursestrack.ui.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.eduardosdl.coursestrack.R
import com.eduardosdl.coursestrack.ui.theme.soraFontFamily

@Composable
fun WordMark() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append("Courses")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("Track")
                }
            },
            fontFamily = soraFontFamily,
            fontSize = 36.sp
        )
        Text(
            text = stringResource(R.string.courses_track_slogan),
            style = MaterialTheme.typography.bodySmall
        )
    }
}