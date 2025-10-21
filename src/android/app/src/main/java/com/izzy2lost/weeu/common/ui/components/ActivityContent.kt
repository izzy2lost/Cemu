package com.izzy2lost.weeu.common.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.izzy2lost.weeu.common.ui.theme.WeeUTheme

@Composable
fun ActivityContent(content: @Composable () -> Unit) {
    WeeUTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}