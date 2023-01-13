package id.perqara.testing_perqara.ui.home

import androidx.lifecycle.MutableLiveData
import id.perqara.testing_perqara.data.repository.games.GamesRepository
import id.perqara.testing_perqara.other.wrapper.EventWrapper
import id.perqara.testing_perqara.other.wrapper.PagingRepositoryWrapper

@HiltViewModel
class HomeViewModel @Inject constructor(
    @Named("gamesRepository")
    private val gamesRepository: GamesRepository,
) : BaseViewModel() {
    var gamesCurrentPage = 0
    var gamesTotalPage = 10
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
                    result.totalPage ?: 0,
                    result.currentPage ?: 0
                )
            }
            is PagingRepositoryWrapper.AccountError -> {
                val errorMessage =
                    if (result.message.startsWith("crypto/rsa: verification error")
                        || result.message.startsWith("Not Allowed")
                        || result.message.startsWith("The resource owner or authorization server denied the request")
                        || result.message.startsWith("Unauthenticated")
                    ) {
                        "Anda sudah Login dari lokasi lain.\n" +
                                "Ketuk OK untuk kembali ke halaman Login"
                    } else {
                        result.message
                    }
                stateLiveData.value = HomeState.Logout(errorMessage)
            }
            is PagingRepositoryWrapper.GenericError -> {
                stateLiveData.value = HomeState.MinorError(result.message ?: "")
            }
            is PagingRepositoryWrapper.NetworkError -> {
                stateLiveData.value = HomeState.NetworkError(RETRY_GET_GAMES_LIST)
                eventLiveData.value = EventWrapper.OnNetworkError("Games Page", "Games List")
            }
            is PagingRepositoryWrapper.ServerError -> {
                stateLiveData.value =
                    HomeState.MinorError(result.error ?: "Mohon maaf, telah terjadi kesalahan")
            }
            is PagingRepositoryWrapper.UnknownError -> {
                stateLiveData.value = HomeState.MinorError(result.errorMessage)
            }
        }
    }

    fun resetGamesPage() {
        gamesCurrentPage = 0
        gamesTotalPage = 10
    }
}