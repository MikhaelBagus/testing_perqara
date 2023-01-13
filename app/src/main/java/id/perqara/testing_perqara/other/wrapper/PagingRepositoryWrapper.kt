package id.perqara.testing_perqara.other.wrapper

sealed class PagingRepositoryWrapper<out T> {
    data class Success<out T>(val content: T, val currentPage: Int?, val totalPage: Int?) : PagingRepositoryWrapper<T>()
    data class GenericError(val code: Int? = null, val message: String? = null) :
        PagingRepositoryWrapper<Nothing>()

    data class ServerError(val code: Int? = null, val error: String? = null) :
        PagingRepositoryWrapper<Nothing>()
    data class AccountError(val message: String) : PagingRepositoryWrapper<Nothing>()
    data class UnknownError(val errorMessage: String) : PagingRepositoryWrapper<Nothing>()
    object NetworkError : PagingRepositoryWrapper<Nothing>()
}