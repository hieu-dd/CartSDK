package vn.teko.cart.core.utils.response.sample

import vn.teko.cart.core.utils.response.builder.identityResponse


fun sample500TokenResponse() = identityResponse{
    statusCode  = 500_001
    error = "Unexpected error occur"
}