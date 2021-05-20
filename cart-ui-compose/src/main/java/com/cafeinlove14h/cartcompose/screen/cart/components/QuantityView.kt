package com.cafeinlove14h.cartcompose.screen.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cafeinlove14h.cartcompose.extensions.toColor
import vn.teko.apollo.extension.getApolloTheme

@Composable
fun QuantityView(
    quantity: Int,
    onIncrease: (Int) -> Unit = {},
    onDecrease: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.small)
            .background(Color.White)
            .border(
                0.5.dp,
                context
                    .getApolloTheme()
                    .getNeutralColor().borderColor.toColor(),
                MaterialTheme.shapes.small
            )
            .width(88.dp)
            .height(32.dp)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .size(16.dp)
                .clickable {
                    onIncrease(quantity + 1)
                }

        )
        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.body2,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            textAlign = TextAlign.Center
        )
        Icon(
            imageVector = Icons.Outlined.Remove,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .size(16.dp)
                .clickable {
                    onDecrease(quantity - 1)
                }
        )
    }
}

@Preview
@Composable
fun DefaultPreview() {
    QuantityView(1)
}