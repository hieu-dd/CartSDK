package vn.teko.cart.domain.mock

import vn.teko.cart.domain.usecase.cart.ImportCart

class ImportCartMockBuilder() {
    fun build(): ImportCart {
        return object : ImportCart(productDataSource = buildProductDataSourceMock { }) {}
    }
}

fun buildImportCartMock(block: ImportCartMockBuilder.() -> Unit): ImportCart {
    return ImportCartMockBuilder().apply(block).build()
}