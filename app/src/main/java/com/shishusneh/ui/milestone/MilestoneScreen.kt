package com.shishusneh.ui.milestone

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shishusneh.domain.model.MilestoneDomain
import com.shishusneh.domain.model.MilestoneStatus
import com.shishusneh.ui.theme.*

@Composable
fun MilestoneScreen(viewModel: MilestoneViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    // Confetti overlay
    if (state.showConfetti) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(2000)
            viewModel.dismissConfetti()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        // Header — icon replaces emoji
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Outlined.Stars, null,
                Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Milestones",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            "Week ${state.selectedWeek} of 52",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(12.dp))

        // Progress
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                LinearProgressIndicator(
                    progress = { if (state.totalCount > 0) state.achievedCount.toFloat() / state.totalCount else 0f },
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "${state.achievedCount} of ${state.totalCount} achieved",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // Week selector chips
        Row(Modifier.horizontalScroll(rememberScrollState()), Arrangement.spacedBy(8.dp)) {
            state.availableWeeks.forEach { week ->
                FilterChip(
                    selected = state.selectedWeek == week,
                    onClick = { viewModel.selectWeek(week) },
                    label = {
                        Text("W$week", fontWeight = if (state.selectedWeek == week) FontWeight.Bold else FontWeight.Normal)
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // Advisory banner
        if (state.showAdvisory) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(0.3f)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Info, null,
                        Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Some milestones not yet reached. Consider a check-up with your paediatrician.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
        }

        // Confetti celebration
        if (state.showConfetti) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(0.3f)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Celebration, null,
                        Modifier.size(26.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Wonderful! Milestone achieved!",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
        }

        // Milestone cards
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(state.milestones) { milestone ->
                val domainIcon = when (milestone.domain) {
                    MilestoneDomain.MOTOR -> Icons.Outlined.DirectionsRun
                    MilestoneDomain.LANGUAGE -> Icons.Outlined.RecordVoiceOver
                    MilestoneDomain.SOCIAL -> Icons.Outlined.Groups
                    MilestoneDomain.COGNITIVE -> Icons.Outlined.Psychology
                }
                val statusColor = when (milestone.status) {
                    MilestoneStatus.ACHIEVED -> HealthyGreen
                    MilestoneStatus.NOT_YET -> AttentionAmber
                    MilestoneStatus.SKIPPED -> MaterialTheme.colorScheme.onSurfaceVariant
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Domain icon in tonal circle
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(0.25f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(Modifier.fillMaxSize(), Alignment.Center) {
                                    Icon(
                                        domainIcon, null,
                                        Modifier.size(22.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    milestone.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    milestone.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                            listOf(
                                MilestoneStatus.ACHIEVED to "Yes" to Icons.Outlined.CheckCircle,
                                MilestoneStatus.NOT_YET to "Not Yet" to Icons.Outlined.Schedule,
                                MilestoneStatus.SKIPPED to "Skip" to Icons.Outlined.SkipNext
                            ).forEach { (statusLabel, icon) ->
                                val (status, label) = statusLabel
                                FilterChip(
                                    selected = milestone.status == status,
                                    onClick = { viewModel.updateStatus(milestone, status) },
                                    label = {
                                        Text(
                                            label,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (milestone.status == status) FontWeight.SemiBold else FontWeight.Normal
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(icon, null, Modifier.size(16.dp))
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = statusColor.copy(0.15f),
                                        selectedLabelColor = statusColor
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
