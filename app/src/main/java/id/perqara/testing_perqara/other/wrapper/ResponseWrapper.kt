package id.perqara.testing_perqara.other.wrapper

data class ResponseWrapper<out T>(
    @SerializedName("error")
    val error: Boolean? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("page_size")
    val page_size: Int? = null,
    @SerializedName("current_page")
    val current_page: Int? = null,
    @SerializedName("next_page")
    val next_page: Int? = null,
    @SerializedName("total_page")
    val total_page: Int? = null,
)