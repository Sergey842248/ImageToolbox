/*
 * ImageToolbox is an image editor for android
 * Copyright (c) 2024 T8RIN (Malik Mukhametzyanov)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * You should have received a copy of the Apache License
 * along with this program.  If not, see <http://www.apache.org/licenses/LICENSE-2.0>.
 */

package com.t8rin.imagetoolbox.feature.settings.presentation.components

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.t8rin.imagetoolbox.core.resources.R
import com.t8rin.imagetoolbox.core.resources.icons.FileReplace
import com.t8rin.imagetoolbox.core.settings.presentation.provider.LocalSettingsState
import com.t8rin.imagetoolbox.core.ui.widget.modifier.ShapeDefaults
import com.t8rin.imagetoolbox.core.ui.widget.preferences.PreferenceRowSwitch
import com.t8rin.imagetoolbox.feature.settings.data.isExternalStorageManager
import com.t8rin.imagetoolbox.feature.settings.data.openManageExternalStorageSettingsIntent

@Composable
fun OverwriteFilesSettingItem(
    onClick: (Boolean) -> Unit,
    shape: Shape = ShapeDefaults.center,
    modifier: Modifier = Modifier.padding(horizontal = 8.dp)
) {
    val settingsState = LocalSettingsState.current
    val context = LocalContext.current

    val manageExternalStorageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        onClick(context.isExternalStorageManager())
    }

    PreferenceRowSwitch(
        shape = shape,
        modifier = modifier,
        onClick = {
            if (!settingsState.overwriteFiles) { // If trying to enable overwriting
                if (!context.isExternalStorageManager()) {
                    context.openManageExternalStorageSettingsIntent()?.let {
                        manageExternalStorageLauncher.launch(it)
                    }
                } else {
                    onClick(true)
                }
            } else { // If trying to disable overwriting
                onClick(false)
            }
        },
        enabled = !settingsState.randomizeFilename && settingsState.hashingTypeForFilename == null,
        title = stringResource(R.string.overwrite_files),
        subtitle = stringResource(R.string.overwrite_files_sub),
        checked = settingsState.overwriteFiles,
        startIcon = Icons.Outlined.FileReplace
    )
}
