package com.openeyeshost.swenproject

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF7C4DFF),
    secondary = androidx.compose.ui.graphics.Color(0xFF00BFA5),
    tertiary = androidx.compose.ui.graphics.Color(0xFFFFC107)
)

private val LightColorScheme = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF5E35B1),
    secondary = androidx.compose.ui.graphics.Color(0xFF00A884),
    tertiary = androidx.compose.ui.graphics.Color(0xFFFFC107)
)

@Composable
fun HotelManagerTheme(content: @Composable () -> Unit) {
    val colorScheme = LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = androidx.compose.material3.Typography(),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                content()
            }
        }
    )
}
