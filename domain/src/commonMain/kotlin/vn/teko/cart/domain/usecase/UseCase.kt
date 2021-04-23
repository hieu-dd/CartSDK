package vn.teko.cart.domain.usecase

import vn.teko.cart.domain.exception.Error
import vn.teko.cart.domain.util.Result
import vn.teko.cart.domain.util.suspendableResult

/**
 * Abstract class for a Use Case.
 * This abstraction represents an execution unit for different use cases (this means that any use
 * case in the application should implement this contract).
 */
abstract class UseCase<in Input : UseCase.Params, out Output : Any> {

    abstract suspend fun run(params: Input): Output

    suspend operator fun invoke(params: Input): Result<Output> = suspendableResult {
        params.selfValidate()?.let { error -> throw error }
        run(params)
    }

    abstract class Params {
        open val validators: List<Validator> = listOf()

        open fun selfValidate(): Error? {
            for (validator in validators) {
                when (val error = validator.validate()) {
                    null -> continue
                    else -> return error
                }
            }

            return null
        }
    }

    class EmptyParams : Params()
}