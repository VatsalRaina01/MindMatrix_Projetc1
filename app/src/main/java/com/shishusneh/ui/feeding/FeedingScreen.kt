package com.shishusneh.ui.feeding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shishusneh.domain.model.BreastSide
import com.shishusneh.domain.model.FeedType
import com.shishusneh.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Feeding Guide — translated from Stitch "Feeding Guide" screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedingScreen(
    onNavigateToChat: () -> Unit,
    viewModel: FeedingViewModel = hiltViewModel()
) {
    val state by viewModel.feedingState.collectAsState()
    val dateFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.showAddFeedingDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, "Add", tint = MaterialTheme.colorScheme.onPrimary)
                Spacer(Modifier.width(8.dp))
                Text("Log Feed", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // === Header ===
            item {
                Spacer(Modifier.height(16.dp))
                Text(
                    "NUTRITION",
                    style = MaterialTheme.typography.labelMedium,
                    letterSpacing = 2.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Feeding Guide",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // === Today's Count Badge ===
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.LocalDrink, null,
                            Modifier.size(28.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                "${state.todayCount} feedings today",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "Tap + to log a new feeding",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // === Daily Tip ===
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Lightbulb, null,
                                Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "TODAY'S INSIGHT",
                                style = MaterialTheme.typography.labelMedium,
                                letterSpacing = 2.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            state.tipTitleEn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            state.dailyTipEn,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // === Ask AI CTA ===
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onNavigateToChat() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Box(
                        Modifier.fillMaxWidth().background(
                            Brush.horizontalGradient(
                                listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primaryContainer)
                            )
                        )
                    ) {
                        Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.AutoMirrored.Filled.Chat, null,
                                Modifier.size(28.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.width(14.dp))
                            Column {
                                Text("Ask the AI Guide", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimary)
                                Text("Get answers to your feeding questions", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary.copy(0.8f))
                            }
                        }
                    }
                }
            }

            // === Recent Feedings ===
            if (state.recentLogs.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Text("Recent Feedings", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface)
                }

                items(state.recentLogs) { log ->
                    val typeIcon: ImageVector = when (log.feedType) {
                        FeedType.BREAST -> Icons.Filled.PregnantWoman
                        FeedType.FORMULA -> Icons.Filled.LocalDrink
                        FeedType.SOLID -> Icons.Filled.RiceBowl
                    }
                    val typeLabel = when (log.feedType) {
                        FeedType.BREAST -> "Breastfeed${log.side?.let { " (${it.name.lowercase()})" } ?: ""}"
                        FeedType.FORMULA -> "Formula"
                        FeedType.SOLID -> "Solid Food"
                    }
                    val detail = buildString {
                        log.durationMins?.let { append("${it} min") }
                        log.quantityMl?.let {
                            if (isNotEmpty()) append(" • ")
                            append("${it} ml")
                        }
                        log.foodDescription?.let {
                            if (isNotEmpty()) append(" • ")
                            append(it)
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(Modifier.padding(14.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(0.25f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(Modifier.fillMaxSize(), Alignment.Center) {
                                    Icon(typeIcon, null, Modifier.size(22.dp), tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(typeLabel, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                if (detail.isNotEmpty()) {
                                    Text(detail, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text(dateFormat.format(Date(log.timestamp)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            }
                            IconButton(onClick = { viewModel.deleteFeedingLog(log) }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Delete, "Delete", Modifier.size(18.dp), tint = MaterialTheme.colorScheme.outlineVariant)
                            }
                        }
                    }
                }
            }

            // === FAQ Section ===
            item {
                Spacer(Modifier.height(8.dp))
                Text("Common Questions", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface)
            }

            items(state.mythBusters) { myth ->
                var expanded by remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                            Text(myth.questionEn, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f).padding(end = 8.dp))
                            Icon(if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        if (expanded) {
                            Spacer(Modifier.height(10.dp))
                            Text(myth.answerEn, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, lineHeight = 22.sp)
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.MedicalServices, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.outline)
                                Spacer(Modifier.width(6.dp))
                                Text("AI-generated guidance — not a medical prescription.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            }
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }

    // === Add Feeding Dialog ===
    if (state.showAddDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideAddFeedingDialog() },
            title = { Text("Log Feeding", style = MaterialTheme.typography.headlineSmall) },
            text = {
                Column {
                    Text("Type", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                        listOf(
                            Triple(FeedType.BREAST, Icons.Filled.PregnantWoman, "Breast"),
                            Triple(FeedType.FORMULA, Icons.Filled.LocalDrink, "Formula"),
                            Triple(FeedType.SOLID, Icons.Filled.RiceBowl, "Solid")
                        ).forEach { (type, icon, label) ->
                            val isSelected = state.selectedFeedType == type
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.updateFeedType(type) },
                                label = { Text(label) },
                                leadingIcon = { Icon(icon, null, Modifier.size(16.dp)) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    if (state.selectedFeedType == FeedType.BREAST) {
                        Text("Side", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                            listOf(BreastSide.LEFT to "Left", BreastSide.RIGHT to "Right", BreastSide.BOTH to "Both").forEach { (side, label) ->
                                FilterChip(selected = state.selectedSide == side, onClick = { viewModel.updateSide(side) }, label = { Text(label) }, shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }

                    if (state.selectedFeedType != FeedType.SOLID) {
                        OutlinedTextField(value = state.durationMins, onValueChange = { viewModel.updateDuration(it) }, label = { Text("Duration (min)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp), suffix = { Text("min") })
                        Spacer(Modifier.height(8.dp))
                    }

                    if (state.selectedFeedType != FeedType.BREAST) {
                        OutlinedTextField(value = state.quantityMl, onValueChange = { viewModel.updateQuantity(it) }, label = { Text("Quantity (ml)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp), suffix = { Text("ml") })
                        Spacer(Modifier.height(8.dp))
                    }

                    if (state.selectedFeedType == FeedType.SOLID) {
                        OutlinedTextField(value = state.foodDescription, onValueChange = { viewModel.updateFoodDescription(it) }, label = { Text("Food description") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))
                        Spacer(Modifier.height(8.dp))
                    }

                    OutlinedTextField(value = state.notes, onValueChange = { viewModel.updateFeedingNotes(it) }, label = { Text("Notes (optional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))
                }
            },
            confirmButton = { TextButton(onClick = { viewModel.addFeedingLog() }) { Text("Save", fontWeight = FontWeight.SemiBold) } },
            dismissButton = { TextButton(onClick = { viewModel.hideAddFeedingDialog() }) { Text("Cancel") } }
        )
    }
}
