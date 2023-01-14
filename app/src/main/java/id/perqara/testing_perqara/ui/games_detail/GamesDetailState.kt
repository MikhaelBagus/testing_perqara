package id.perqara.testing_perqara.ui.games_detail

import id.perqara.testing_perqara.data.model.GamesModel

open class GamesDetailState {
    data class LoadGamesDetail(val data: GamesModel) : GamesDetailState()
}