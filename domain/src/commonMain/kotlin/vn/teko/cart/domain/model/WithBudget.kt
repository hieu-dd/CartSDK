package vn.teko.cart.domain.model


interface WithBudget {
    val budgetStatus: String?

    val outOfBudget: Boolean?
}