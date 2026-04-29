package com.shishusneh.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shishusneh.domain.model.Gender

/**
 * Baby Profile Setup — translated from Stitch "Baby Profile Setup" screen
 *
 * Layout from Stitch HTML:
 * - Step indicator with nurture progress bar
 * - "YOUR CHILD" label + display-lg heading per step
 * - Form fields with rounded-xl shapes
 * - Gradient CTA button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyProfileSetupScreen(
    onProfileCreated: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.setupState.collectAsState()

    LaunchedEffect(state.isComplete) {
        if (state.isComplete) onProfileCreated()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        // Back button
        if (state.currentStep > 0) {
            IconButton(onClick = { viewModel.previousStep() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
        }
        Spacer(Modifier.height(8.dp))

        // === Progress (Stitch: nurture progress bar) ===
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                LinearProgressIndicator(
                    progress = { (state.currentStep + 1) / 4f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Step ${state.currentStep + 1} of 4",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        AnimatedContent(targetState = state.currentStep, label = "step") { step ->
            when (step) {
                0 -> NameStep(state, viewModel)
                1 -> DOBStep(state, viewModel)
                2 -> WeightStep(state, viewModel)
                3 -> GenderStep(state, viewModel)
            }
        }

        Spacer(Modifier.weight(1f))

        // === CTA Button (Stitch: primary, rounded-xl, 56dp height) ===
        Button(
            onClick = { viewModel.nextStep() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(28.dp),
            enabled = !state.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    if (state.currentStep == 3) "Create Profile" else "Continue",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun NameStep(state: ProfileSetupState, vm: OnboardingViewModel) {
    Column {
        Text(
            "YOUR CHILD",
            style = MaterialTheme.typography.labelMedium,
            letterSpacing = 2.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "What's your\nbaby's name?",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(28.dp))
        OutlinedTextField(
            value = state.babyName,
            onValueChange = { vm.updateBabyName(it) },
            label = { Text("Baby's Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.nameError != null,
            supportingText = state.nameError?.let { { Text(it) } },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = state.motherName,
            onValueChange = { vm.updateMotherName(it) },
            label = { Text("Mother's Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.motherNameError != null,
            supportingText = state.motherNameError?.let { { Text(it) } },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DOBStep(state: ProfileSetupState, vm: OnboardingViewModel) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.dobMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis <= System.currentTimeMillis()
        }
    )
    Column {
        Text(
            "BIRTHDAY",
            style = MaterialTheme.typography.labelMedium,
            letterSpacing = 2.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "When was your\nbaby born?",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(28.dp))
        Card(
            modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CalendarMonth, "Calendar",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(14.dp))
                Text(
                    if (state.dobMillis != null) {
                        java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
                            .format(java.util.Date(state.dobMillis))
                    } else "Tap to select date",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        state.dobError?.let {
            Text(
                it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { vm.updateDOB(it) }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel") } }
        ) { DatePicker(state = datePickerState) }
    }
}

@Composable
private fun WeightStep(state: ProfileSetupState, vm: OnboardingViewModel) {
    Column {
        Text(
            "BIRTH WEIGHT",
            style = MaterialTheme.typography.labelMedium,
            letterSpacing = 2.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "How much did\nyour baby weigh?",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "in kilograms (e.g., 2.8)",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(28.dp))
        OutlinedTextField(
            value = state.birthWeightKg,
            onValueChange = { vm.updateBirthWeight(it) },
            label = { Text("Weight (kg)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = state.weightError != null,
            supportingText = state.weightError?.let { { Text(it) } },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            suffix = { Text("kg") }
        )
    }
}

@Composable
private fun GenderStep(state: ProfileSetupState, vm: OnboardingViewModel) {
    Column {
        Text(
            "GENDER",
            style = MaterialTheme.typography.labelMedium,
            letterSpacing = 2.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Baby's gender?",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(28.dp))
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(16.dp)) {
            listOf(
                Triple(Gender.MALE, Icons.Default.Male, "Boy"),
                Triple(Gender.FEMALE, Icons.Default.Female, "Girl")
            ).forEach { (gender, icon, label) ->
                val isSelected = state.gender == gender
                Card(
                    modifier = Modifier.weight(1f).height(140.dp).clickable { vm.updateGender(gender) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceContainerLowest
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (isSelected) 2.dp else 0.dp
                    )
                ) {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                icon, label,
                                Modifier.size(44.dp),
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                label,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
