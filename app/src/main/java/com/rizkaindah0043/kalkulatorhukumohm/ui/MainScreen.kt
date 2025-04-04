package com.rizkaindah0043.kalkulatorhukumohm.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rizkaindah0043.kalkulatorhukumohm.R
import com.rizkaindah0043.kalkulatorhukumohm.ui.theme.KalkulatorHukumOhmTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding))
    }
}

@SuppressLint("StringFormatMatches")
fun calculate(selectedOption: String, firstInput: String, secondInput: String, context: Context): String {
    val firstValue = firstInput.toFloatOrNull()
    val secondValue = secondInput.toFloatOrNull()

    return if (firstValue != null && secondValue != null) {
        when (selectedOption) {
            context.getString(R.string.voltage) -> {
                val voltage = firstValue * secondValue
                context.getString(R.string.result, voltage)
            }
            context.getString(R.string.current) -> {
                val current = firstValue / secondValue
                context.getString(R.string.result, current)
            }
            context.getString(R.string.resistance) -> {
                val resistance = firstValue / secondValue
                context.getString(R.string.result, resistance)
            }
            else -> context.getString(R.string.invalid_input)
        }
    } else {
        context.getString(R.string.invalid_input)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(
        stringResource(id = R.string.voltage),
        stringResource(id = R.string.current),
        stringResource(id = R.string.resistance)
    )
    var selectedOption by remember { mutableStateOf(options[0]) }
    var firstInput by remember { mutableStateOf("") }
    var firstInputError by remember { mutableStateOf(false) }
    var secondInput by remember { mutableStateOf("") }
    var secondInputError by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.Ohm_intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                label = { Text(stringResource(id = R.string.choose_variable)) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = stringResource(id = R.string.choose_variable)
                    )
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOption = option
                            expanded = false
                            firstInput = ""
                            secondInput = ""
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val (firstLabel, secondLabel) = when (selectedOption) {
            stringResource(id = R.string.voltage) ->
                stringResource(id = R.string.enter_current) to stringResource(id = R.string.enter_resistance)
            stringResource(id = R.string.current) ->
                stringResource(id = R.string.enter_voltage) to stringResource(id = R.string.enter_resistance)
            stringResource(id = R.string.resistance) ->
                stringResource(id = R.string.enter_voltage) to stringResource(id = R.string.enter_current)
            else -> "" to ""
        }

        OutlinedTextField(
            value = firstInput,
            onValueChange = { firstInput = it },
            label = { Text(firstLabel) },
            supportingText = { ErrorHint(firstInputError) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            trailingIcon = {
                val unit = getUnitFromLabel(firstLabel)
                if (unit.isNotEmpty()) Text(unit)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = secondInput,
            onValueChange = { secondInput = it },
            label = { Text(secondLabel) },
            supportingText = { ErrorHint(secondInputError) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            trailingIcon = {
                val unit = getUnitFromLabel(secondLabel)
                if (unit.isNotEmpty()) Text(unit)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                firstInputError = firstInput.isBlank()
                secondInputError = secondInput.isBlank()
                if (firstInputError || secondInputError) return@Button

                result = calculate(selectedOption, firstInput, secondInput, context)
            },
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.calculate))
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (result.isNotEmpty()) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp
            )
            val unit = when (selectedOption) {
                stringResource(R.string.voltage) -> "V"
                stringResource(R.string.current) -> "A"
                stringResource(R.string.resistance) -> "Ω"
                else -> ""
            }

            Text(
                text = "$result $unit",
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

@Composable
fun getUnitFromLabel(label: String): String {
    return when (label) {
        stringResource(R.string.enter_voltage) -> "V"
        stringResource(R.string.enter_current) -> "A"
        stringResource(R.string.enter_resistance) -> "Ω"
        else -> ""
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError){
        Text(text = stringResource(R.string.invalid_input))
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    KalkulatorHukumOhmTheme {
        MainScreen()
    }
}
