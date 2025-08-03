package com.texthip.thip.data.model.base

fun <T> BaseResponse<T>.handleBaseResponse(): Result<T?> {
    return if (isSuccess) {
        Result.success(this.response)
    } else {
        Result.failure(
            ThipApiFailureException(
                code = this.code,
                message = this.message
            )
        )
    }
}

data class ThipApiFailureException(
    val code: Int,
    override val message: String
) : Throwable()