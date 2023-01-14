package id.perqara.testing_perqara.helpers

import com.google.gson.Gson
import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.other.wrapper.*
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

object RemoteResultHelper {
    fun <T> processPagingRemoteResponse(response: Response<PagingResponseWrapper<T>>): PagingRepositoryWrapper<T> {
        return if (response.isSuccessful && response.body() != null) {
            PagingRepositoryWrapper.Success(
                response.body()!!.results!!,
                response.body()?.next.toString(),
            )
        } else if (response.isSuccessful && response.body() == null) {
            PagingRepositoryWrapper.ServerError(
                response.code(),
                "Mohon maaf, telah terjadi kesalahan"
            )
        } else {
            val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponseWrapper::class.java)
            val errorMessage: String = errorResponse.error?.toString() ?: ""
            when {
                response.code() == 401 -> {
                    PagingRepositoryWrapper.AccountError(errorMessage)
                }
                else -> {
                    PagingRepositoryWrapper.GenericError(response.code(), errorMessage)
                }
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