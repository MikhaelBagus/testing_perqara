package id.perqara.testing_perqara.ui.home

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.perqara.testing_perqara.data.repository.games.GamesRepository
import id.perqara.testing_perqara.other.base.BaseViewModel
import id.perqara.testing_perqara.other.wrapper.EventWrapper
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Named("gamesRepository")
    private val gamesRepository: GamesRepository,
) : BaseViewModel() {
    var gamesCurrentPage = 1
    var gamesNext = ""
    val stateLiveData = MutableLiveData<HomeState>()

    suspend fun getGamesList(page: Int, search: String?) {
        eventLiveData.value = EventWrapper.OnLoadingShow
        val result = gamesRepository.getGamesList(
            page,
            10,
            search,
            "51d0e291cbd842ebbe3c6f76b04d68d6"
        )
        eventLiveData.value = EventWrapper.OnLoadingDissapear

        stateLiveData.value = HomeState.LoadGames(
            result.results.orEmpty(),
            result.next.toString()
        )
        gamesCurrentPage = page
        gamesNext = result.next.toString()
    }

    fun resetGamesPage() {
        gamesCurrentPage = 1
        gamesNext = ""
    }
}