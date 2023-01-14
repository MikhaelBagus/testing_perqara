package id.perqara.testing_perqara.data.model

import com.google.gson.annotations.SerializedName

data class GamesModel(
    @SerializedName("id") var id : Int? = 0,
    @SerializedName("name") var name : String? = "",
    @SerializedName("description") var description : String? = "",
    @SerializedName("released") var released : String? = "",
    @SerializedName("background_image") var background_image : String? = "",
    @SerializedName("rating") var rating : Double? = 0.0,
    @SerializedName("publishers") var publishers : List<PublishersGamesModel>? = null,
    @SerializedName("playtime") var playtime : Int? = 0,
)