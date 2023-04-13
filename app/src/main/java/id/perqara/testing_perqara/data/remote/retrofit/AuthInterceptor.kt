package id.perqara.testing_perqara.data.remote.retrofit

import id.perqara.testing_perqara.data.local.SessionPref
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(val sessionPref: SessionPref) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request: Request = original.newBuilder()
//            .header(
//                "Authorization",
//                "Bearer " + sessionPref.tokenLogin()
//            )
//            .method(original.method, original.body)
//            .addHeader("Accept:", "application/json")
            .build()
        return chain.proceed(request)
    }
}