package id.perqara.testing_perqara.data.model

data class GamesModel(
    @SerializedName("id") var id : String? = null,
    @SerializedName("name") var name : String? = null
)