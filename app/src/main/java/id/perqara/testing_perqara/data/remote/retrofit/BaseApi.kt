package id.perqara.testing_perqara.data.remote.retrofit

import android.content.Context

class BaseApi {
    companion object {
        var INSTANCE: Retrofit? = null
        fun getRetrofitInstance(context: Context): Retrofit {
            if (INSTANCE != null) {
                return INSTANCE!!
            }
            INSTANCE = RetrofitInstance().createRetrofitInstanceGateway(
                context,
                "https://api.rawg.io/api/"
            )
            return INSTANCE!!
        }
    }
}