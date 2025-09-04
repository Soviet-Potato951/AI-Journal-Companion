@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.example.aijournalcompanion.ui.components

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun HelpDialog() {
    var show by remember { mutableStateOf(false) }

    Button(onClick = { show = true }) {
        Text("Open Help")
    }

    if (!show) return

    BackHandler(enabled = true) { show = false }

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            usePlatformDefaultWidth = false, // we’ll control width/height
            dismissOnBackPress = true,
            dismissOnClickOutside = false // avoid accidental dismiss while scrolling
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.45f)) // dim the world
                // absorb clicks so they don't hit the underlying screen
                .clickable(enabled = true, onClick = { /* consume */ }, indication = null, interactionSource = remember { MutableInteractionSource() })
        ) {
            AnimatedVisibility(
                visible = show,
                enter = fadeIn(tween(160)) + scaleIn(tween(220, easing = FastOutSlowInEasing), initialScale = 0.98f),
                exit = fadeOut(tween(120)) + scaleOut(tween(120), targetScale = 0.98f),
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 16.dp,
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .fillMaxHeight(0.85f)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Title bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Help & FAQs",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(onClick = { show = false }) {
                                Text("Close")
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            val mUrl = "file:///android_asset/help.html"
                            AndroidView(
                                factory = { context ->
                                    WebView(context).apply {
                                        layoutParams = ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.MATCH_PARENT
                                        )
                                        webViewClient = WebViewClient()
                                        loadUrl(mUrl)
                                    }
                                },
                                update = { it.loadUrl(mUrl) },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
