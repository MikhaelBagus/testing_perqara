package id.perqara.testing_perqara.other.wrapper

sealed class RepositoryWrapper<out T> {
    data class Success<out T>(val content: T) : RepositoryWrapper<T>()
    data class GenericError(val code: Int? = null, val message: String? = null) :
        RepositoryWrapper<Nothing>()
    data class ServerError(val code: Int? = null, val error: String? = null) :
        RepositoryWrapper<Nothing>()
    data class AccountError(val message: String) : RepositoryWrapper<Nothing>()
    data class UnknownError(val errorMessage: String) : RepositoryWrapper<Nothing>()
    object NetworkError : RepositoryWrapper<Nothing>()
}