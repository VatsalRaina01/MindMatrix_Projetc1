package com.shishusneh.ui.vaccination

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shishusneh.domain.model.VaccinationStatus
import com.shishusneh.ui.theme.*
import com.shishusneh.R
import androidx.compose.ui.res.stringResource
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccinationScreen(viewModel: VaccinationViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val tabs = listOf(stringResource(R.string.all), stringResource(R.string.upcoming), stringResource(R.string.completed))

    // === Date Picker Dialog for marking vaccine as done ===
    if (state.showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { viewModel.dismissDatePicker() },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { date ->
                            viewModel.confirmMarkDone(date)
                        }
                    }
                ) {
                    Text("Confirm", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDatePicker() }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        "Date Administered",
                        Modifier.padding(start = 24.dp, top = 24.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                headline = {
                    Text(
                        "When was this vaccine given?",
                        Modifier.padding(start = 24.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.Vaccines, null,
                Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Immunisation Calendar",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(Modifier.height(16.dp))

        // Progress card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
            ) {
                Column(Modifier.padding(20.dp).fillMaxWidth()) {
                    Text(
                        stringResource(R.string.vaccination_progress),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(0.8f)
                    )
                    Spacer(Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { if (state.totalCount > 0) state.completedCount.toFloat() / state.totalCount else 0f },
                        modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                        color = MaterialTheme.colorScheme.onPrimary,
                        trackColor = MaterialTheme.colorScheme.onPrimary.copy(0.2f)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "${state.completedCount} of ${state.totalCount} completed",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Tab row
        TabRow(
            selectedTabIndex = state.selectedTab,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabs.forEachIndexed { i, title ->
                Tab(
                    selected = state.selectedTab == i,
                    onClick = { viewModel.selectTab(i) },
                    text = {
                        Text(title, fontWeight = if (state.selectedTab == i) FontWeight.SemiBold else FontWeight.Normal)
                    },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        val filtered = when (state.selectedTab) {
            1 -> state.vaccinations.filter { it.status != VaccinationStatus.DONE }
            2 -> state.vaccinations.filter { it.status == VaccinationStatus.DONE }
            else -> state.vaccinations
        }

        if (filtered.isEmpty() && state.selectedTab == 2 && state.completedCount == state.totalCount && state.totalCount > 0) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.Celebration, null,
                        Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("All vaccines completed!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Great job protecting your little one!", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filtered) { vax ->
                    val statusColor = when (vax.status) {
                        VaccinationStatus.DONE -> HealthyGreen
                        VaccinationStatus.OVERDUE -> AlertRed
                        VaccinationStatus.PENDING -> InfoBlue
                    }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Status icon
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = statusColor.copy(0.12f),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(Modifier.fillMaxSize(), Alignment.Center) {
                                    when (vax.status) {
                                        VaccinationStatus.DONE -> Icon(Icons.Default.CheckCircle, null, tint = statusColor, modifier = Modifier.size(24.dp))
                                        VaccinationStatus.OVERDUE -> Icon(Icons.Default.Warning, null, tint = statusColor, modifier = Modifier.size(24.dp))
                                        VaccinationStatus.PENDING -> Icon(Icons.Default.Schedule, null, tint = statusColor, modifier = Modifier.size(24.dp))
                                    }
                                }
                            }
                            Spacer(Modifier.width(14.dp))
                            Column(Modifier.weight(1f)) {
                                Text(vax.vaccineName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                                Text(vax.disease, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${vax.ageLabel} • Dose ${vax.doseNumber}/${vax.totalDoses}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                                // Show administered date for completed vaccines
                                if (vax.status == VaccinationStatus.DONE && vax.administeredDate != null) {
                                    Text(
                                        "Given: ${dateFormat.format(Date(vax.administeredDate))}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = HealthyGreen
                                    )
                                } else {
                                    Text(
                                        "Due: ${dateFormat.format(Date(vax.targetDate))}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            if (vax.status != VaccinationStatus.DONE) {
                                // Opens date picker instead of immediately marking done
                                FilledTonalButton(
                                    onClick = { viewModel.requestMarkDone(vax.id) },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.4f)
                                    )
                                ) { Text("Done", fontWeight = FontWeight.SemiBold) }
                            } else {
                                TextButton(onClick = { viewModel.markAsPending(vax.id) }) {
                                    Text("Undo", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
