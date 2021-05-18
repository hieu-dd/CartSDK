package com.cafeinlove14h.cartcompose.screen.cart.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cafeinlove14h.cartcompose.utils.CurrencyUtils.formatMoney
import com.google.accompanist.coil.CoilImage
import vn.teko.cart.domain.model.CartItemEntity

@Composable
fun CartLineItemView(lineItem: CartItemEntity, onIncrease: (String, Int) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val (imageRef, nameRef, priceRef, quantityRef) = createRefs()
        val product = lineItem.product!!
        CoilImage(
            data = product.productInfo.imageUrl.orEmpty(),
            modifier = Modifier
                .width(84.dp)
                .height(84.dp)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = MaterialTheme.shapes.small
                )
                .padding(1.dp)
                .constrainAs(imageRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
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
            color = MaterialTheme.colors.primary,
            text = formatMoney(lineItem.price.toDouble()),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(priceRef) {
                start.linkTo(nameRef.start)
                top.linkTo(nameRef.bottom, margin = 12.dp)
            }
        )
        QuantityView(quantity = lineItem.quantity, modifier = Modifier.constrainAs(quantityRef) {
            top.linkTo(priceRef.top)
            bottom.linkTo(priceRef.bottom)
            end.linkTo(parent.end)
        }, onIncrease = {
            onIncrease(lineItem.lineItemId, it)
        })
    }
}
