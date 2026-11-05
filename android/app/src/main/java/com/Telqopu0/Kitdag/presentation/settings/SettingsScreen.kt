package com.Telqopu0.Kitdag.presentation.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Telqopu0.Kitdag.R
import com.Telqopu0.Kitdag.core.di.AppViewModelFactory
import com.Telqopu0.Kitdag.core.theme.BorderWarm
import com.Telqopu0.Kitdag.core.theme.Danger
import com.Telqopu0.Kitdag.core.theme.OnPrimary
import com.Telqopu0.Kitdag.core.theme.SettingsOverlay
import com.Telqopu0.Kitdag.core.theme.SurfaceCard
import com.Telqopu0.Kitdag.core.theme.SurfaceMuted
import com.Telqopu0.Kitdag.core.theme.TextMuted
import com.Telqopu0.Kitdag.core.theme.TextPrimary
import com.Telqopu0.Kitdag.core.theme.TigerOrange
import com.Telqopu0.Kitdag.core.ui.ScreenScaffold
import com.Telqopu0.Kitdag.core.ui.SecondaryButton
import com.Telqopu0.Kitdag.presentation.dialog.ConfirmResetDialog

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory)
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { onReset() }
    }

    if (state.confirmVisible) {
        ConfirmResetDialog(
            onConfirm = { viewModel.confirmReset() },
            onCancel = { viewModel.cancelReset() }
        )
    }

    ScreenScaffold(
        title = stringResource(R.string.settings_title),
        backDescription = stringResource(R.string.cd_back),
        onBack = onBack,
        overlay = SettingsOverlay,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SettingRow(
                label = stringResource(R.string.setting_sound),
                checked = state.settings.sound,
                onCheckedChange = { viewModel.setSound(it) }
            )
            SettingRow(
                label = stringResource(R.string.setting_vibration),
                checked = state.settings.vibration,
                onCheckedChange = { viewModel.setVibration(it) }
            )
            SettingRow(
                label = stringResource(R.string.setting_fast),
                checked = state.settings.fastAnimations,
                onCheckedChange = { viewModel.setFastAnimations(it) }
            )

            Spacer(modifier = Modifier.height(4.dp))

            SecondaryButton(
                text = stringResource(R.string.reset_progress),
                onClick = { viewModel.askReset() },
                accent = Danger,
                labelColor = Danger
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.settings_footer),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SettingRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val onLabel = stringResource(R.string.state_on)
    val offLabel = stringResource(R.string.state_off)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .semantics { stateDescription = if (checked) onLabel else offLabel },
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, BorderWarm),
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = OnPrimary,
                    checkedTrackColor = TigerOrange,
                    checkedBorderColor = TigerOrange,
                    uncheckedThumbColor = SurfaceCard,
                    uncheckedTrackColor = SurfaceMuted,
                    uncheckedBorderColor = BorderWarm
                )
            )
        }
    }
}
