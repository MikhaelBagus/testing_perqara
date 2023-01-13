package id.perqara.testing_perqara.other.wrapper

open class PagingResponseWrapper<out T>(
    @SerializedName("error")
    val error: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: T?,
    @SerializedName("page_size")
    val page_size: Int?,
    @SerializedName("current_page")
    val current_page: Int?,
    @SerializedName("next_page")
    val next_page: Int?,
    @SerializedName("total_page")
    val total_page: Int?,
)