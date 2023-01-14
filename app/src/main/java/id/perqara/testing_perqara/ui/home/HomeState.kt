package id.perqara.testing_perqara.ui.home

import id.perqara.testing_perqara.data.model.GamesModel

open class HomeState {
    data class LoadGames(val data: List<GamesModel>, val next: String) : HomeState()
    data class MinorError(val message: String) : HomeState()
    data class NetworkError (val errorType: String): HomeState()
}