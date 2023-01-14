package id.perqara.testing_perqara.ui.home

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.perqara.testing_perqara.data.repository.games.GamesRepository
import id.perqara.testing_perqara.other.base.BaseViewModel
import id.perqara.testing_perqara.other.wrapper.EventWrapper
import id.perqara.testing_perqara.other.wrapper.PagingRepositoryWrapper
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

    companion object {
        const val RETRY_GET_GAMES_LIST = "retry_get_games_list"
    }

    suspend fun getGamesList(page: Int, search: String?) {
        eventLiveData.value = EventWrapper.OnLoadingShow
        val result = gamesRepository.getGamesList(
            page,
            10,
            search,
            "51d0e291cbd842ebbe3c6f76b04d68d6"
        )
        eventLiveData.value = EventWrapper.OnLoadingDissapear

        when (result) {
            is PagingRepositoryWrapper.Success -> {
                stateLiveData.value = HomeState.LoadGames(
                    result.content,
                    result.next
                )
                gamesCurrentPage = page
                gamesNext = result.next
            }
            is PagingRepositoryWrapper.GenericError -> {
                stateLiveData.value = HomeState.MinorError(result.message ?: "")
            }
            is PagingRepositoryWrapper.NetworkError -> {
                stateLiveData.value = HomeState.NetworkError(RETRY_GET_GAMES_LIST)
                eventLiveData.value = EventWrapper.OnNetworkError("Games Page", "Games List")
            }
            is PagingRepositoryWrapper.ServerError -> {
                stateLiveData.value = HomeState.MinorError(result.error ?: "Mohon maaf, telah terjadi kesalahan")
            }
            is PagingRepositoryWrapper.UnknownError -> {
                stateLiveData.value = HomeState.MinorError(result.errorMessage)
            }
            is PagingRepositoryWrapper.AccountError -> {
                stateLiveData.value = HomeState.MinorError("Mohon maaf, telah terjadi kesalahan")
            }
        }
    }

    fun resetGamesPage() {
        gamesCurrentPage = 1
        gamesNext = ""
    }
}