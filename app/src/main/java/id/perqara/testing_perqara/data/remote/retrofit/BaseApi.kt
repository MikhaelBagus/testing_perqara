package id.perqara.testing_perqara.data.remote.retrofit

import android.content.Context
import retrofit2.Retrofit

class BaseApi {
    companion object {
        var INSTANCE: Retrofit? = null
        fun getRetrofitInstance(context: Context): Retrofit {
            if (INSTANCE != null) {
                return INSTANCE!!
            }
            INSTANCE = RetrofitInstance().createRetrofitInstanceWithoutCertificate(
                context,
                "https://api.rawg.io/api/"
            )
            return INSTANCE!!
        }
    }
}