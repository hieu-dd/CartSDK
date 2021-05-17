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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cafeinlove14h.cartwithcompose.model.Product
import com.cafeinlove14h.cartwithcompose.model.ProductInfo
import com.google.accompanist.coil.CoilImage

@Composable
fun ProductListItem(product: Product) {
    ConstraintLayout(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(3.dp))
            .padding(2.dp)
            .background(color = Color.White)
            .padding(10.dp)
            )

     {
        val (imageRef, numOfColorRef, color1Ref, color2Ref, color3Ref, nameRef, addProductRef, supplierRetailPriceRef, priceRef) = createRefs()
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
        Circle(color = Color(0xFF000000), modifier = Modifier.constrainAs(color1Ref) {
            top.linkTo(numOfColorRef.top)
            bottom.linkTo(numOfColorRef.bottom)
            start.linkTo(numOfColorRef.end, margin = 5.dp)
        })
        Circle(color = Color(0xFFFFFFFF), modifier = Modifier.constrainAs(color2Ref) {
            top.linkTo(numOfColorRef.top)
            bottom.linkTo(numOfColorRef.bottom)
            start.linkTo(color1Ref.end, margin = 5.dp)
        })
        Circle(color = Color(0xFF8D8D8D), modifier = Modifier.constrainAs(color3Ref) {
            top.linkTo(numOfColorRef.top)
            bottom.linkTo(numOfColorRef.bottom)
            start.linkTo(color2Ref.end, margin = 5.dp)
        })
        Text(
            text = product.productInfo.name,
            color = Color(0xFF000000),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(nameRef) {
                    top.linkTo(numOfColorRef.bottom, margin = 5.dp)
                    start.linkTo(parent.start)
                    height=Dimension.value(52.dp)
                })
        Text(text = "${product.prices[0].sellPrice} vnđ",
            color = Color(0xFFD60303),
            fontSize = 17.sp,
            modifier = Modifier.constrainAs(priceRef) {
                top.linkTo(nameRef.bottom, margin = 5.dp)
                start.linkTo(parent.start)
            })
        Text(
            text = "${product.prices[0].supplierRetailPrice} vnđ",
            color = Color(0xFF858585),
            fontSize = 12.sp,
            modifier = Modifier
                .constrainAs(supplierRetailPriceRef) {
                    top.linkTo(priceRef.bottom, margin = 5.dp)
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
                    top.linkTo(supplierRetailPriceRef.bottom, margin = 5.dp)
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

//@Preview
//@Composable
//fun DefaultPreView(){
//
//}