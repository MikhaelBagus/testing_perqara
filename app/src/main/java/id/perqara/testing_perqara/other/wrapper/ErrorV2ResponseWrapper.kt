package id.perqara.testing_perqara.other.wrapper

data class ErrorV2ResponseWrapper(
    @SerializedName("error")
    val error: Boolean? = null,
    @SerializedName("message")
    val message: String? = null
)