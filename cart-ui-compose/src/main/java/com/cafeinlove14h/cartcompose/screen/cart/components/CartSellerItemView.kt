package com.cafeinlove14h.cartcompose.screen.cart.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cafeinlove14h.cartcompose.extensions.getPrimaryColor
import com.cafeinlove14h.cartcompose.extensions.getTextPrimaryColor
import com.cafeinlove14h.cartcompose.extensions.toColor
import com.cafeinlove14h.cartcompose.utils.CurrencyUtils.formatMoney
import vn.teko.cart.domain.model.ProductInfoEntity

@Composable
fun CartSellerItemView(
    sellerEntity: ProductInfoEntity.SellerEntity,
    price: Double,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)? = null
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp),
            colors = CheckboxDefaults.colors(context.getPrimaryColor().color500.toColor())
        )
        Text(
            text = sellerEntity.displayName ?: "Chưa rõ",
            style = MaterialTheme.typography.subtitle1,
            color = context.getTextPrimaryColor(),
            modifier = Modifier
                .padding(start = 12.dp)
                .fillMaxWidth()
                .weight(1f)
        )
        Text(
            text = formatMoney(price),
            color = context.getPrimaryColor().color600.toColor(),
            style = MaterialTheme.typography.h6
        )
    }
}