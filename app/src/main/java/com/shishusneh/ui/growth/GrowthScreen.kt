package com.shishusneh.ui.growth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shishusneh.data.constants.WHOGrowthData
import com.shishusneh.ui.theme.*
import com.shishusneh.R
import androidx.compose.ui.res.stringResource
import java.text.SimpleDateFormat
import java.util.*

/**
 * Growth Chart — with Canvas-based chart, weight/height toggle, and entry delete
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowthScreen(viewModel: GrowthViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    // Track which metric is selected for the chart
    var showWeight by remember { mutableStateOf(true) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.showAddDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, "Add", tint = MaterialTheme.colorScheme.onPrimary)
                Spacer(Modifier.width(8.dp))
                Text("Add Log", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // === Header ===
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    stringResource(R.string.who_standards),
                    style = MaterialTheme.typography.labelMedium,
                    letterSpacing = 2.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    stringResource(R.string.growth_metrics),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    state.profile?.name ?: "Baby",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // === Bento Grid: Weight + Height ===
            item {
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {
                    // Weight card
                    Card(
                        modifier = Modifier.weight(1f).height(160.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(Modifier.padding(20.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Scale, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.7f))
                                Spacer(Modifier.width(6.dp))
                                Text("WEIGHT", style = MaterialTheme.typography.labelSmall, letterSpacing = 1.5.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.7f))
                            }
                            Column {
                                Text(
                                    "${state.entries.lastOrNull()?.weightKg ?: "—"}",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text("kg", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.7f))
                            }
                            state.entries.lastOrNull()?.percentile?.let {
                                Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.1f)) {
                                    Text("${String.format("%.0f", it)}th Percentile", Modifier.padding(horizontal = 12.dp, vertical = 4.dp), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                                }
                            }
                        }
                    }
                    // Height card
                    Card(
                        modifier = Modifier.weight(1f).height(160.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(Modifier.padding(20.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Straighten, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(Modifier.width(6.dp))
                                Text("HEIGHT", style = MaterialTheme.typography.labelSmall, letterSpacing = 1.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Column {
                                Text(
                                    "${state.entries.lastOrNull()?.heightCm ?: "—"}",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text("cm", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // === Chart Area with Weight/Height toggle ===
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().height(320.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                            Text(stringResource(R.string.trajectory), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface)
                            // Weight/Height toggle
                            Row(
                                Modifier.clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.surfaceContainer).padding(4.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (showWeight) MaterialTheme.colorScheme.surface else Color.Transparent,
                                    shadowElevation = if (showWeight) 1.dp else 0.dp,
                                    modifier = Modifier.clickable { showWeight = true }
                                ) {
                                    Text(
                                        stringResource(R.string.weight), Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (showWeight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (!showWeight) MaterialTheme.colorScheme.surface else Color.Transparent,
                                    shadowElevation = if (!showWeight) 1.dp else 0.dp,
                                    modifier = Modifier.clickable { showWeight = false }
                                ) {
                                    Text(
                                        stringResource(R.string.height), Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (!showWeight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(16.dp))

                        if (state.entries.isEmpty()) {
                            Box(Modifier.fillMaxSize(), Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Filled.ShowChart, null, Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary.copy(0.5f))
                                    Spacer(Modifier.height(8.dp))
                                    Text(stringResource(R.string.add_first_measurement), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        } else {
                            // Canvas-based growth chart
                            val primaryColor = MaterialTheme.colorScheme.primary
                            val gridColor = MaterialTheme.colorScheme.outlineVariant.copy(0.3f)
                            val percentileBandColor = MaterialTheme.colorScheme.primaryContainer.copy(0.2f)

                            Canvas(Modifier.fillMaxSize()) {
                                val chartWidth = size.width
                                val chartHeight = size.height
                                val paddingLeft = 40f
                                val paddingBottom = 30f
                                val plotWidth = chartWidth - paddingLeft - 10f
                                val plotHeight = chartHeight - paddingBottom - 10f

                                val entries = state.entries.sortedBy { it.date }
                                val values = entries.map {
                                    if (showWeight) it.weightKg.toFloat() else (it.heightCm ?: 0f)
                                }.filter { it > 0f }

                                if (values.isEmpty()) return@Canvas

                                val minVal = (values.min() * 0.85f).coerceAtLeast(0f)
                                val maxVal = values.max() * 1.15f
                                val range = (maxVal - minVal).coerceAtLeast(1f)

                                // Draw horizontal grid lines
                                val gridSteps = 4
                                for (i in 0..gridSteps) {
                                    val y = 10f + plotHeight - (plotHeight * i / gridSteps)
                                    drawLine(gridColor, Offset(paddingLeft, y), Offset(paddingLeft + plotWidth, y), strokeWidth = 1f)

                                    // Y-axis labels
                                    val labelValue = minVal + range * i / gridSteps
                                    drawContext.canvas.nativeCanvas.drawText(
                                        String.format("%.1f", labelValue),
                                        4f, y + 4f,
                                        android.graphics.Paint().apply {
                                            color = android.graphics.Color.GRAY
                                            textSize = 22f
                                            isAntiAlias = true
                                        }
                                    )
                                }

                                // Draw percentile band (3rd-97th) as a shaded area
                                if (showWeight && state.percentileLines.isNotEmpty()) {
                                    val p3 = state.percentileLines["3rd"]
                                    val p97 = state.percentileLines["97th"]
                                    if (p3 != null && p97 != null && p3.isNotEmpty()) {
                                        val bandPath = Path()
                                        // Forward pass: 97th percentile top
                                        p97.forEachIndexed { idx, (_, weight) ->
                                            val x = paddingLeft + (plotWidth * idx / (p97.size - 1).coerceAtLeast(1))
                                            val y = 10f + plotHeight - ((weight.toFloat() - minVal) / range * plotHeight).coerceIn(0f, plotHeight)
                                            if (idx == 0) bandPath.moveTo(x, y) else bandPath.lineTo(x, y)
                                        }
                                        // Backward pass: 3rd percentile bottom
                                        p3.reversed().forEachIndexed { idx, (_, weight) ->
                                            val origIdx = p3.size - 1 - idx
                                            val x = paddingLeft + (plotWidth * origIdx / (p3.size - 1).coerceAtLeast(1))
                                            val y = 10f + plotHeight - ((weight.toFloat() - minVal) / range * plotHeight).coerceIn(0f, plotHeight)
                                            bandPath.lineTo(x, y)
                                        }
                                        bandPath.close()
                                        drawPath(bandPath, percentileBandColor)
                                    }
                                }

                                // Draw data line
                                val dataPath = Path()
                                val points = mutableListOf<Offset>()
                                values.forEachIndexed { idx, value ->
                                    val x = paddingLeft + (plotWidth * idx / (values.size - 1).coerceAtLeast(1))
                                    val y = 10f + plotHeight - ((value - minVal) / range * plotHeight)
                                    points.add(Offset(x, y))
                                    if (idx == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
                                }
                                drawPath(dataPath, primaryColor, style = Stroke(width = 3f))

                                // Draw data points
                                points.forEach { pt ->
                                    drawCircle(primaryColor, radius = 6f, center = pt)
                                    drawCircle(Color.White, radius = 3f, center = pt)
                                }
                            }
                        }
                    }
                }
            }

            // === Underweight Warning ===
            val latestEntry = state.entries.lastOrNull()
            if (latestEntry?.percentile != null && latestEntry.percentile < 3.0) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Warning, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Baby's weight is below expected. Please visit your health centre.",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            // === History Header ===
            item {
                Spacer(Modifier.height(8.dp))
                Text(stringResource(R.string.history), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface)
            }

            // === History Items with delete ===
            items(state.entries.reversed()) { entry ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        // Date circle badge
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(Modifier.fillMaxSize(), Alignment.Center) {
                                Icon(Icons.Outlined.CalendarToday, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(dateFormat.format(Date(entry.date)), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface)
                            val zone = entry.growthZone ?: ""
                            val zoneColor = when (zone) {
                                "SEVERE_UNDERWEIGHT" -> AlertRed
                                "UNDERWEIGHT" -> AttentionAmber
                                "HEALTHY" -> HealthyGreen
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                            if (zone.isNotEmpty()) {
                                Text(
                                    zone.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = zoneColor,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("${entry.weightKg} kg", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface)
                            entry.heightCm?.let {
                                Text("$it cm", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Spacer(Modifier.width(8.dp))
                        // Delete button
                        IconButton(onClick = { viewModel.deleteEntry(entry) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Delete, "Delete", Modifier.size(18.dp), tint = MaterialTheme.colorScheme.outlineVariant)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }

    // Add Entry Dialog
    if (state.showAddDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideAddDialog() },
            title = { Text("Add Measurement", style = MaterialTheme.typography.headlineSmall) },
            text = {
                Column {
                    OutlinedTextField(
                        value = state.newWeight,
                        onValueChange = { viewModel.updateNewWeight(it) },
                        label = { Text("Weight (kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = state.weightError != null,
                        supportingText = state.weightError?.let { { Text(it) } },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        suffix = { Text("kg") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.newHeight,
                        onValueChange = { viewModel.updateNewHeight(it) },
                        label = { Text("Height (cm) — optional") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        suffix = { Text("cm") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    state.weightWarning?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(it, color = AttentionAmber, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            },
            confirmButton = { TextButton(onClick = { viewModel.addEntry() }) { Text("Add", fontWeight = FontWeight.SemiBold) } },
            dismissButton = { TextButton(onClick = { viewModel.hideAddDialog() }) { Text("Cancel") } }
        )
    }
}
