package id.perqara.testing_perqara.helpers

import id.perqara.testing_perqara.other.wrapper.RepositoryWrapper
import id.perqara.testing_perqara.other.wrapper.ResponseWrapper
import java.io.IOException

object RemoteResultHelper {
    fun <T> processRemoteResponse(response: Response<ResponseWrapper<T>>): RepositoryWrapper<T> {
        return if (response.isSuccessful && response.body() != null) {
            RepositoryWrapper.Success(response.body()!!.data!!)
        } else if (response.isSuccessful && response.body() == null) {
            RepositoryWrapper.ServerError(
                response.code(),
                "Mohon maaf, telah terjadi kesalahan"
            )
        } else {
            val errorResponse =
                Gson().fromJson(response.errorBody()?.string(), ErrorResponseWrapper::class.java)
            val errorMessage: String = errorResponse.error?.message ?: ""
            when {
                errorMessage.startsWith("Not Allowed") -> {
                    RepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("Unauthenticated") -> {
                    RepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("not membership/membership is expired") -> {
                    RepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("crypto/rsa: verification error") -> {
                    RepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("The resource owner or authorization server denied the request") -> {
                    RepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("Out of range") -> {
                    RepositoryWrapper.GenericError(response.code(), errorMessage)
                }
                else -> {
                    RepositoryWrapper.GenericError(response.code(), errorMessage)
                }
            }
        }
    }

    fun <T> processV2RemoteResponse(response: Response<ResponseWrapper<T>>): RepositoryWrapper<T> {
        return if (response.isSuccessful && response.body() != null) {
            RepositoryWrapper.Success(response.body()!!.data!!)
        } else if (response.isSuccessful && response.body() == null) {
            RepositoryWrapper.ServerError()
        } else {
            val errorResponse =
                Gson().fromJson(response.errorBody()?.string(), ErrorV2ResponseWrapper::class.java)
            val errorMessage: String = errorResponse.message ?: ""
            when {
                errorMessage.startsWith("Not Allowed") -> {
                    RepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("Unauthenticated") -> {
                    RepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("not membership/membership is expired") -> {
                    RepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("crypto/rsa: verification error") -> {
                    RepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("The resource owner or authorization server denied the request") -> {
                    RepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("Out of range") -> {
                    RepositoryWrapper.GenericError(response.code(), errorMessage)
                }
                response.code() == 401 -> {
                    RepositoryWrapper.AccountError(errorMessage)
                }
                else -> {
                    RepositoryWrapper.GenericError(response.code(), errorMessage)
                }
            }
        }
    }

    fun <T> processPagingRemoteResponse(response: Response<PagingResponseWrapper<T>>): PagingRepositoryWrapper<T> {
        return if (response.isSuccessful && response.body() != null) {
            PagingRepositoryWrapper.Success(
                response.body()!!.data!!,
                response.body()?.current_page,
                response.body()?.total_page
            )
        } else if (response.isSuccessful && response.body() == null) {
            PagingRepositoryWrapper.ServerError(
                response.code(),
                "Mohon maaf, telah terjadi kesalahan"
            )
        } else {
            val errorResponse =
                Gson().fromJson(response.errorBody()?.string(), ErrorResponseWrapper::class.java)
            val errorMessage: String = errorResponse.error?.message ?: ""
            when {
                errorMessage.startsWith("Not Allowed") -> {
                    PagingRepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("Unauthenticated") -> {
                    PagingRepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("not membership/membership is expired") -> {
                    PagingRepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("crypto/rsa: verification error") -> {
                    PagingRepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("The resource owner or authorization server denied the request") -> {
                    PagingRepositoryWrapper.AccountError(errorMessage)
                }
                errorMessage.startsWith("Out of range") -> {
                    PagingRepositoryWrapper.GenericError(response.code(), errorMessage)
                }
                response.code() == 401 -> {
                    PagingRepositoryWrapper.AccountError(errorMessage)
                }
                else -> {
                    PagingRepositoryWrapper.GenericError(response.code(), errorMessage)
                }
            }
        }
    }

    fun <T> processRemoteException(exception: Exception): RepositoryWrapper<T> {
        return when (exception) {
            is HttpException -> {
                RepositoryWrapper.NetworkError
            }
            is IOException -> {
                RepositoryWrapper.NetworkError
            }
            else -> {
                RepositoryWrapper.UnknownError("Mohon maaf telah terjadi kesalahan")
            }
        }
    }

    fun <T> processPagingRemoteException(exception: Exception): PagingRepositoryWrapper<T> {
        return when (exception) {
            is HttpException -> {
                PagingRepositoryWrapper.NetworkError
            }
            is IOException -> {
                PagingRepositoryWrapper.NetworkError
            }
            else -> {
                PagingRepositoryWrapper.UnknownError("Mohon maaf telah terjadi kesalahan")
            }
        }
    }
}