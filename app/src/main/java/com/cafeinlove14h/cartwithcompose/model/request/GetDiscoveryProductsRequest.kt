package com.cafeinlove14h.cartwithcompose.model.request

import kotlinx.serialization.Serializable

@Serializable
data class GetDiscoveryProductsRequest(val skus: String)