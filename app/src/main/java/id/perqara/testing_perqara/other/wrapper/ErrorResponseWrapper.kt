package id.perqara.testing_perqara.other.wrapper

data class ErrorResponseWrapper(
    @SerializedName("error")
    val error: Error? = null,
){
    data class Error(
        @SerializedName("code")
        val code: String? = null,
        @SerializedName("message")
        val message: String? = null,
    )
}