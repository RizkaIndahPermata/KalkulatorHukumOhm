package com.rizkaindah0043.kalkulatorhukumohm.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rizkaindah0043.kalkulatorhukumohm.R
import com.rizkaindah0043.kalkulatorhukumohm.navigation.Screen
import com.rizkaindah0043.kalkulatorhukumohm.ui.theme.KalkulatorHukumOhmTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.about_application),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
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


@SuppressLint("StringFormatMatches")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContent(modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val options = listOf(
        stringResource(id = R.string.voltage),
        stringResource(id = R.string.current),
        stringResource(id = R.string.resistance)
    )
    var selectedOption by rememberSaveable { mutableStateOf(options[0]) }
    var firstInput by rememberSaveable { mutableStateOf("") }
    var firstInputError by rememberSaveable { mutableStateOf(false) }
    var secondInput by rememberSaveable { mutableStateOf("") }
    var secondInputError by rememberSaveable { mutableStateOf(false) }
    var result by rememberSaveable { mutableStateOf("") }

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
                IconPicker(isError = firstInputError, unit = unit)
            },
            isError = firstInputError,
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
                IconPicker(isError = secondInputError, unit = unit)
            },
            isError = secondInputError,
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
            val finalUnit = if (result != context.getString(R.string.invalid_input)) unit else ""

            Text(
                text = "$result $finalUnit",
                style = MaterialTheme.typography.titleLarge,
            )
            Button(onClick = {
                val numericResult = result.filter { it.isDigit() || it == ',' || it == '.' }
                    .replace(',', '.')
                    .toFloatOrNull() ?: 0f
                val unitResult = when (selectedOption) {
                    context.getString(R.string.voltage) -> "V"
                    context.getString(R.string.current) -> "A"
                    context.getString(R.string.resistance) -> "Ω"
                    else -> ""
                }
                val formattedResult = context.getString(R.string.share_template, numericResult) + " $unitResult"

                shareData(context, formattedResult)
            },
                modifier = Modifier.padding(top = 25.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.share))
            }
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
fun IconPicker(isError: Boolean, unit: String){
    if (isError){
        Icon(imageVector = Icons.Filled.Warning, contentDescription = stringResource(R.string.cannot_empty), tint = MaterialTheme.colorScheme.error)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError){
        Text(text = stringResource(R.string.cannot_empty), color = MaterialTheme.colorScheme.error)
    }
}

private fun shareData(context: Context, message: String){
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    KalkulatorHukumOhmTheme {
        MainScreen(rememberNavController())
    }
}
