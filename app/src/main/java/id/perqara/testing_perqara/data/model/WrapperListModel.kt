package id.perqara.testing_perqara.data.model

import com.google.gson.annotations.SerializedName

data class WrapperListModel(
    @SerializedName("next") var next : String? = "",
    @SerializedName("results") var results : List<GamesModel>? = null,
)