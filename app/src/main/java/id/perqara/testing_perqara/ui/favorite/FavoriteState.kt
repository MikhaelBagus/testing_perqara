package id.perqara.testing_perqara.ui.favorite

import id.perqara.testing_perqara.data.model.GamesModel

open class FavoriteState {
    data class LoadGamesList(val data: List<GamesModel>, val maxPage: Int, val currentPage: Int) : FavoriteState()
    data class MinorError(val message: String) : FavoriteState()
    data class NetworkError (val errorType: String): FavoriteState()
}