package com.cafeinlove14h.cartwithcompose.ui.screen.productlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cafeinlove14h.cartwithcompose.model.Product
import com.google.accompanist.coil.CoilImage

@Composable
fun ProductItem(product: Product, onAddItem: (sku: String) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color.White)
            .padding(top = 12.dp)
            .clickable { onAddItem(product.productInfo.sku) }
    ) {
        val (image, tvName, tvSku) = createRefs()

        CoilImage(
            data = product.productInfo.imageUrl.orEmpty(),
            contentDescription = product.productInfo.name,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            contentScale = ContentScale.Crop
        )
        Text(
            text = product.productInfo.name, modifier = Modifier
                .constrainAs(tvName) {
                    start.linkTo(image.end, margin = 10.dp)
                    top.linkTo(image.top)
                    end.linkTo(parent.end)
                    width = Dimension.preferredWrapContent
                }
        )
        Text(text = product.productInfo.sku, modifier = Modifier.constrainAs(tvSku) {
            start.linkTo(tvName.start)
            top.linkTo(tvName.bottom)
            width = Dimension.preferredWrapContent
        })
    }
}
