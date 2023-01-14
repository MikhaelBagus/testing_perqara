package id.perqara.testing_perqara.data.remote.retrofit

import retrofit2.Retrofit

class BaseApi {
    companion object {
        var INSTANCE: Retrofit? = null
        fun getRetrofitInstance(): Retrofit {
            if (INSTANCE != null) {
                return INSTANCE!!
            }
            INSTANCE = RetrofitInstance().createRetrofitInstanceWithoutCertificate(
                "https://api.rawg.io/api/"
            )
            return INSTANCE!!
        }
    }
}