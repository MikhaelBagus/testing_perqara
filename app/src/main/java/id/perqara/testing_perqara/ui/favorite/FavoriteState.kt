package id.perqara.testing_perqara.ui.favorite

import id.perqara.testing_perqara.data.model.GamesModel

open class FavoriteState {
    data class LoadGames(val data: List<GamesModel>, val next: String) : FavoriteState()
}