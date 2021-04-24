package vn.teko.cart.domain.mock

import vn.teko.cart.domain.model.ProductEntity
import vn.teko.cart.domain.usecase.cart.ProductDataSource

open class ProductDataSourceMock : ProductDataSource {
    override suspend fun getValidProducts(skus: List<String>): List<ProductEntity> {
        TODO("Not yet implemented")
    }
}

class ProductDataSourceMockBuilder() {
    fun build(): ProductDataSource {
        return object : ProductDataSourceMock() {

        }
    }
}

fun buildProductDataSourceMock(block: ProductDataSourceMockBuilder.() -> Unit): ProductDataSource {
    return ProductDataSourceMockBuilder().apply(block).build()
}
