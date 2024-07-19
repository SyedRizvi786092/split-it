package com.project.splitit.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RoundIconButton(modifier: Modifier = Modifier,
                    icon: ImageVector,
                    tint: Color = Color.Black.copy(0.8f),
                    backgroundColor: Color = MaterialTheme.colorScheme.background,
                    elevation: Dp = 4.dp,
                    onClick: () -> Unit) {
    Card(modifier = modifier
        .padding(4.dp)
        .clickable { onClick.invoke() }
        .then(Modifier.size(40.dp)),
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor,
            contentColor = MaterialTheme.colorScheme.onBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)) {
        Icon(imageVector = icon, contentDescription = "Plus or Minus icon",
            modifier = Modifier.fillMaxSize(),
            tint = tint)
    }
}