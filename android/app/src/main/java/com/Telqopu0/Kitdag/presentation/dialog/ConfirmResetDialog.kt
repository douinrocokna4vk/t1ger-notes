package com.Telqopu0.Kitdag.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Telqopu0.Kitdag.R
import com.Telqopu0.Kitdag.core.theme.Danger
import com.Telqopu0.Kitdag.core.theme.SurfaceCard
import com.Telqopu0.Kitdag.core.theme.TextPrimary
import com.Telqopu0.Kitdag.core.theme.TextSecondary

@Composable
fun ConfirmResetDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        shape = RoundedCornerShape(20.dp),
        containerColor = SurfaceCard,
        title = {
            Text(
                text = stringResource(R.string.reset_progress),
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary
            )
        },
        text = {
            Text(
                text = stringResource(R.string.reset_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.reset),
                    style = MaterialTheme.typography.labelLarge,
                    color = Danger
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary
                )
            }
        }
    )
}
