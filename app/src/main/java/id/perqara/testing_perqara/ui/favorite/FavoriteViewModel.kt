package id.perqara.testing_perqara.ui.favorite

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import id.perqara.testing_perqara.data.repository.games.GamesRepository
import id.perqara.testing_perqara.other.base.BaseViewModel
import id.perqara.testing_perqara.other.wrapper.EventWrapper
import id.perqara.testing_perqara.other.wrapper.RepositoryWrapper
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    @Named("gamesRepository")
    private val gamesRepository: GamesRepository,
) : BaseViewModel() {
    var gamesCurrentPage = 1
    var gamesNext = ""
    val stateLiveData = MutableLiveData<FavoriteState>()

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
            is RepositoryWrapper.Success -> {
                stateLiveData.value = FavoriteState.LoadGames(
                    result.content.results.orEmpty(),
                    result.content.next.toString()
                )
                gamesCurrentPage = page
                gamesNext = result.content.next.toString()
            }
            is RepositoryWrapper.GenericError -> {
                stateLiveData.value = FavoriteState.MinorError(result.message ?: "")
            }
            is RepositoryWrapper.NetworkError -> {
                stateLiveData.value = FavoriteState.NetworkError("")
                eventLiveData.value = EventWrapper.OnNetworkError("Favorite Page", "Favorite List")
            }
            is RepositoryWrapper.ServerError -> {
                stateLiveData.value = FavoriteState.MinorError(result.error ?: "Mohon maaf, telah terjadi kesalahan")
            }
            is RepositoryWrapper.UnknownError -> {
                stateLiveData.value = FavoriteState.MinorError(result.errorMessage)
            }
        }
    }

    fun resetGamesPage() {
        gamesCurrentPage = 1
        gamesNext = ""
    }
}