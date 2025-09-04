package com.example.aijournalcompanion.ui.chart

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import com.example.aijournalcompanion.model.EmotionEntry
import kotlin.math.roundToInt

object DragAndDrop {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PastEmotions(
        history: List<EmotionEntry>,
        listVersion: Int,
        onDelete: (Long) -> Unit,
        modifier: Modifier = Modifier
    ) {
        // Live coordinates for the trash zone (more robust than caching a Rect once)
        var trashCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
        var trashHot by remember { mutableStateOf(false) }

        // Animated trash bar styling
        val trashBg by animateColorAsState(
            targetValue = if (trashHot) MaterialTheme.colorScheme.error else Color.DarkGray,
            label = "trashBg"
        )
        val trashHeight by animateDpAsState(
            targetValue = if (trashHot) 72.dp else 60.dp,
            label = "trashHeight"
        )
        val trashTextColor = if (trashHot) MaterialTheme.colorScheme.onError else Color.White

        Box(modifier = modifier.fillMaxSize()) {

            // Content column leaves dynamic space for the trash bar so items don't sit under it
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = trashHeight + 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                history.forEach { value ->
                    key(listVersion, value.id) {
                        val haptics = LocalHapticFeedback.current

                        var offset by remember { mutableStateOf(Offset.Zero) }
                        var itemSize by remember { mutableStateOf(Size.Zero) }
                        var originInRoot by remember { mutableStateOf(Offset.Zero) }
                        var isDragging by remember { mutableStateOf(false) }
                        var isOverTrash by remember { mutableStateOf(false) }

                        // Animated item styling
                        val scale by animateFloatAsState(
                            targetValue = if (isDragging) 1.02f else 1f,
                            label = "dragScale"
                        )
                        val cardColor by animateColorAsState(
                            targetValue = if (isOverTrash)
                                MaterialTheme.colorScheme.errorContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            label = "cardColor"
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = cardColor,
                            tonalElevation = if (isDragging) 6.dp else 1.dp,
                            shadowElevation = if (isDragging) 8.dp else 1.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                                .graphicsLayer(scaleX = scale, scaleY = scale)
                                .zIndex(if (isDragging) 1f else 0f)
                                .onGloballyPositioned { c ->
                                    itemSize = c.size.toSize()
                                    originInRoot = c.positionInRoot()
                                }
                                .pointerInput(value.id) {
                                    detectDragGestures(
                                        onDragStart = { isDragging = true },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            offset += dragAmount

                                            // Live overlap check using current trash bounds
                                            val topLeft = originInRoot + offset
                                            val draggedRect = Rect(topLeft, itemSize)
                                            val over = trashCoords?.boundsInRoot()?.overlaps(draggedRect) == true
                                            if (over != isOverTrash) {
                                                isOverTrash = over
                                                trashHot = over
                                                if (over) {
                                                    haptics.performHapticFeedback(
                                                        androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                                                    )
                                                }
                                            }
                                        },
                                        onDragEnd = {
                                            val topLeft = originInRoot + offset
                                            val draggedRect = Rect(topLeft, itemSize)
                                            if (trashCoords?.boundsInRoot()?.overlaps(draggedRect) == true) {
                                                onDelete(value.id)
                                            }
                                            offset = Offset.Zero
                                            isDragging = false
                                            isOverTrash = false
                                            trashHot = false
                                        },
                                        onDragCancel = {
                                            offset = Offset.Zero
                                            isDragging = false
                                            isOverTrash = false
                                            trashHot = false
                                        }
                                    )
                                }
                        ) {
                            Column(Modifier.padding(14.dp)) {
                                Text(value.emotion, style = MaterialTheme.typography.titleSmall)
                                Spacer(Modifier.height(2.dp))
                                Text(value.advice, style = MaterialTheme.typography.bodyMedium)
                                Spacer(Modifier.height(2.dp))
                                Text(value.emoji, style = MaterialTheme.typography.bodyMedium)
                                Spacer(Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }

            // Trash bar: hard-pinned to the absolute bottom center of this Box
            // Note: If an ancestor applies imePadding(), that ancestor may shift children upward.
            // Within this file we keep it pinned; prefer disabling Scaffold insets at the screen level if needed.
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(10f)
                    .fillMaxWidth()
                    .height(trashHeight)
                    .background(trashBg)
                    .onGloballyPositioned { coords -> trashCoords = coords },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (trashHot) "Release to delete" else "🗑️  Drag here to delete",
                    color = trashTextColor,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
