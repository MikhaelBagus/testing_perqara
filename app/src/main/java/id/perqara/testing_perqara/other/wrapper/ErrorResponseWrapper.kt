package id.perqara.testing_perqara.other.wrapper

import com.google.gson.annotations.SerializedName

data class ErrorResponseWrapper(
    @SerializedName("error")
    val error: Boolean? = null,
    @SerializedName("message")
    val message: String? = null,
)