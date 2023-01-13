package id.perqara.testing_perqara.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.perqara.testing_perqara.data.repository.games.GamesRepository
import id.perqara.testing_perqara.other.wrapper.EventWrapper
import id.perqara.testing_perqara.other.wrapper.PagingRepositoryWrapper
import id.perqara.testing_perqara.ui.home.HomeState

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    @Named("gamesRepository")
    private val gamesRepository: GamesRepository,
) : BaseViewModel() {
    var gamesCurrentPage = 0
    var gamesTotalPage = 10
    val stateLiveData = MutableLiveData<FavoriteState>()

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
                stateLiveData.value = FavoriteState.LoadGames(
                    result.content,
                    result.totalPage ?: 0,
                    result.currentPage ?: 0
                )
            }
            is PagingRepositoryWrapper.GenericError -> {
                stateLiveData.value = FavoriteState.MinorError(result.message ?: "")
            }
            is PagingRepositoryWrapper.NetworkError -> {
                stateLiveData.value = FavoriteState.NetworkError(RETRY_GET_GAMES_LIST)
                eventLiveData.value = EventWrapper.OnNetworkError("Games Page", "Games List")
            }
            is PagingRepositoryWrapper.ServerError -> {
                stateLiveData.value = FavoriteState.MinorError(result.error ?: "Mohon maaf, telah terjadi kesalahan")
            }
            is PagingRepositoryWrapper.UnknownError -> {
                stateLiveData.value = FavoriteState.MinorError(result.errorMessage)
            }
        }
    }

    fun resetGamesPage() {
        gamesCurrentPage = 0
        gamesTotalPage = 10
    }
}