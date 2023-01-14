package id.perqara.testing_perqara.data.model

import com.google.gson.annotations.SerializedName

data class PublishersGamesModel(
    @SerializedName("id") var id : Int? = 0,
    @SerializedName("name") var name : String? = ""
)