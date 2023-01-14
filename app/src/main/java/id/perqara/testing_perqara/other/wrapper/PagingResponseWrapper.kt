package id.perqara.testing_perqara.other.wrapper

import com.google.gson.annotations.SerializedName

open class PagingResponseWrapper<out T>(
    @SerializedName("results")
    val results: T?,
    @SerializedName("next")
    val next: String?,
)