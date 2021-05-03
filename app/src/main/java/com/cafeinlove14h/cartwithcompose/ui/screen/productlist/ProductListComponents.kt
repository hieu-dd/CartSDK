package com.cafeinlove14h.cartwithcompose.ui.screen.productlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cafeinlove14h.cartwithcompose.model.Product
import com.cafeinlove14h.cartwithcompose.model.ProductInfo
import com.google.accompanist.coil.CoilImage

@Composable
fun ProductListItem(product: Product) {
    ConstraintLayout(
        modifier = Modifier
            .padding(2.dp)
            .background(color = Color.White)
            .padding(10.dp)

    ) {
        val (imageRef, numOfColorRef, color1Ref, color2Ref, color3Ref, nameRef, addProductRef, priceRef) = createRefs()
        CoilImage(
            data = product.productInfo.imageUrl ?: "",
            contentDescription = "",
            modifier = Modifier
                .constrainAs(imageRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(170.dp)
                }
        )
        Text(text = "3 MÀU", modifier = Modifier
            .constrainAs(numOfColorRef) {
                top.linkTo(imageRef.bottom, margin = 5.dp)
                start.linkTo(parent.start)
            })
        Circle(color = Color(0xFFE91E63), modifier = Modifier.constrainAs(color1Ref) {
            top.linkTo(numOfColorRef.top)
            bottom.linkTo(numOfColorRef.bottom)
            start.linkTo(numOfColorRef.end, margin = 5.dp)
        })
        Circle(color = Color(0xFF2196F3), modifier = Modifier.constrainAs(color2Ref) {
            top.linkTo(numOfColorRef.top)
            bottom.linkTo(numOfColorRef.bottom)
            start.linkTo(color1Ref.end, margin = 5.dp)
        })
        Circle(color = Color(0xFFFFEB3B), modifier = Modifier.constrainAs(color3Ref) {
            top.linkTo(numOfColorRef.top)
            bottom.linkTo(numOfColorRef.bottom)
            start.linkTo(color2Ref.end, margin = 5.dp)
        })
        Text(
            text = product.productInfo.name,
            color = Color(0xFFC96D50),
            modifier = Modifier.constrainAs(nameRef) {
                top.linkTo(numOfColorRef.bottom, margin = 5.dp)
                start.linkTo(parent.start)
            })
        Text(
            text = "${product.prices[0].sellPrice} vnđ",
            color = Color(0xFFBB7A7A),
            modifier = Modifier.constrainAs(priceRef) {
                top.linkTo(nameRef.bottom, margin = 5.dp)
                start.linkTo(parent.start, margin = 5.dp)
            },
            textDecoration = TextDecoration.LineThrough
        )

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            ),
            modifier = Modifier
                .clip(shape = RoundedCornerShape(3.dp))
                .background(color = Color(0xFFE60E0E))
                .padding(2.dp)
                .clip(shape = RoundedCornerShape(2.dp))
                .constrainAs(addProductRef) {
                    top.linkTo(priceRef.bottom, margin = 5.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
        ) {
            Text(text = "thêm vào giỏ")
        }
    }
}

@Composable
fun Circle(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(10.dp)
            .height(10.dp)
            .clip(shape = RoundedCornerShape(5.dp))
            .background(color)

    )
}