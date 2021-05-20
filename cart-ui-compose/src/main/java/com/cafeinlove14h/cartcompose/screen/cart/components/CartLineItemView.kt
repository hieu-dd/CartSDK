package com.cafeinlove14h.cartcompose.screen.cart.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cafeinlove14h.cartcompose.extensions.*
import com.cafeinlove14h.cartcompose.utils.CurrencyUtils.formatMoney
import com.google.accompanist.coil.CoilImage
import vn.teko.apollo.extension.getApolloTheme
import vn.teko.cart.domain.model.CartItemEntity

@Composable
fun CartLineItemView(
    lineItem: CartItemEntity,
    onIncrease: (Int) -> Unit = {},
    onDecrease: (Int) -> Unit = {},
    onCheckedChange: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 0.5.dp, context.getNeutralColor().borderColor.toColor())
            .padding(16.dp)
    ) {
        val (selectRef, imageRef, nameRef, skuRef, priceRef, quantityRef, originPriceRef) = createRefs()
        val product = lineItem.product!!

        Checkbox(
            checked = lineItem.isSelected,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp)
                .constrainAs(selectRef) {
                    top.linkTo(imageRef.top)
                    bottom.linkTo(imageRef.bottom)
                    start.linkTo(parent.start)
                },
            colors = CheckboxDefaults.colors(context.getPrimaryColor().color500.toColor())
        )

        CoilImage(
            data = product.productInfo.imageUrl.orEmpty(),
            modifier = Modifier
                .width(84.dp)
                .height(84.dp)
                .border(
                    0.5.dp,
                    context.getNeutralColor().borderColor.toColor(),
                    MaterialTheme.shapes.small
                )
                .padding(1.dp)
                .constrainAs(imageRef) {
                    top.linkTo(parent.top)
                    start.linkTo(selectRef.end, margin = 12.dp)
                },
            contentDescription = ""
        )
        Text(
            text = lineItem.displayName,
            style = MaterialTheme.typography.body1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.constrainAs(nameRef) {
                top.linkTo(parent.top)
                start.linkTo(imageRef.end, margin = 8.dp)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
        )
        Text(
            text = "Sku ${lineItem.sku}",
            style = MaterialTheme.typography.caption,
            color = context.getSecondaryTextColor(),
            modifier = Modifier.constrainAs(skuRef) {
                start.linkTo(nameRef.start)
                top.linkTo(nameRef.bottom)
            }
        )
        Text(
            color = context.getTextPrimaryColor(),
            text = formatMoney(lineItem.price.toDouble()),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(priceRef) {
                start.linkTo(skuRef.start)
                top.linkTo(skuRef.bottom, margin = 12.dp)
            },
        )
        Text(
            text = formatMoney(
                product.prices.firstOrNull()?.supplierRetailPrice?.toDouble() ?: 0.0
            ),
            color = context.getSecondaryTextColor(),
            style = MaterialTheme.typography.caption,
            modifier = Modifier.constrainAs(originPriceRef) {
                top.linkTo(priceRef.bottom)
                start.linkTo(priceRef.start)
            }
        )
        QuantityView(quantity = lineItem.quantity, modifier = Modifier.constrainAs(quantityRef) {
            top.linkTo(priceRef.top)
            bottom.linkTo(originPriceRef.bottom)
            end.linkTo(parent.end)
        }, onIncrease = {
            onIncrease(it)
        }, onDecrease = {
            onDecrease(it)
        })
    }
}

