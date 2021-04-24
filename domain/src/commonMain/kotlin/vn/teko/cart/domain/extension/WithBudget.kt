package vn.teko.cart.domain.extension

import vn.teko.cart.domain.constants.BUDGET_STATUS_ACTIVE
import vn.teko.cart.domain.model.WithBudget

/**
 * Whether a benefit with budget is valid
 *
 * This will check if the budget is still active and not out of budget
 */
internal fun WithBudget.isBudgetValid(): Boolean {
    return (budgetStatus == null || budgetStatus == BUDGET_STATUS_ACTIVE) && (outOfBudget == null || !outOfBudget!!)
}

internal fun List<WithBudget>.isValid(): Boolean {
    return this.all { it.isBudgetValid() }
}