package id.perqara.testing_perqara.data.remote.retrofit

import android.util.Base64
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.util.*
import java.util.concurrent.TimeUnit

object OkHttpClientFactory {
    private const val DEFAULT_MAX_REQUEST = 30

    fun create(interceptors: Array<Interceptor>, showDebugLog: Boolean): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .readTimeout(80, TimeUnit.SECONDS)
            .connectTimeout(90, TimeUnit.SECONDS)
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .retryOnConnectionFailure(true)

        interceptors.forEach {
            builder.addInterceptor(it)
        }

        if (showDebugLog) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor).build()
        }
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = DEFAULT_MAX_REQUEST
        builder.dispatcher(dispatcher)

        return builder.build()
    }
}

//Disini untuk ganti base URL
//https://hera-dev.perqara.com/
val specialFun by lazy {
    String(
        Base64.decode(
            "aHR0cHM6Ly9oZXJhLWRldi5wZXJxYXJhLmNvbS8=", // Dev
            Base64.DEFAULT
        )
    )
}

const val MULTIPART_FORM_DATA = "multipart/form-data"

fun createRequestBody(s: String): RequestBody {
    return s.toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
}