package com.izzy2lost.weeu.common.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.izzy2lost.weeu.common.ui.theme.CemuTheme

@Composable
fun ActivityContent(content: @Composable () -> Unit) {
    CemuTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}