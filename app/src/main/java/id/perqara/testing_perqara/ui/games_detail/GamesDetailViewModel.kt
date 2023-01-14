package id.perqara.testing_perqara.ui.games_detail

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.data.repository.games.GamesRepository
import id.perqara.testing_perqara.other.base.BaseViewModel
import id.perqara.testing_perqara.other.wrapper.EventWrapper
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class GamesDetailViewModel @Inject constructor(
    @Named("gamesRepository")
    private val gamesRepository: GamesRepository,
) : BaseViewModel() {
    val stateLiveData = MutableLiveData<GamesDetailState>()
    var gamesModel: GamesModel? = null

    var gamesId: Int? = 0

    suspend fun getGamesDetail(gamesId: Int) {
        eventLiveData.value = EventWrapper.OnLoadingShow
        val result = gamesRepository.getGamesDetail(gamesId, "51d0e291cbd842ebbe3c6f76b04d68d6")
        eventLiveData.value = EventWrapper.OnLoadingDissapear

        stateLiveData.value = GamesDetailState.LoadGamesDetail(
            result
        )
        gamesModel = result
    }
}