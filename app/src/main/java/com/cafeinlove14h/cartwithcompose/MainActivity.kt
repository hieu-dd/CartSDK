package com.cafeinlove14h.cartwithcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.lifecycleScope
import com.cafeinlove14h.cartwithcompose.model.GetDiscoverProductsApiResponse
import com.cafeinlove14h.cartwithcompose.ui.theme.CartWithComposeTheme
import com.google.accompanist.coil.CoilImage
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import vn.teko.cart.android.busmodels.Product
import vn.teko.cart.android.busmodels.fakeProduct
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var client: HttpClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenResumed {
            val products =
                client.post<GetDiscoverProductsApiResponse>("https://discovery.stag.tekoapis.net/api/v1/search") {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    body = Json.parseToJsonElement(
                        """
                            {
                              "pagination": {
                                "pageNumber": 1,
                                "itemsPerPage": 200
                              },
                            	"terminalCode": "vnshop",
                            	"filter": {
                            		"sellerIds": ["1"],
                                    "skus":[]
                            	}
                            }
                        """.trimIndent()
                    )
                }.result.products

            setContent {
                CartWithComposeTheme {
                    LazyColumn() {
                        items(products) { product ->
                            ProductItem(product = product)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 12.dp)
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
        })
    }
}

@Preview
@Composable
fun Test() {
    ProductItem(fakeProduct())
}