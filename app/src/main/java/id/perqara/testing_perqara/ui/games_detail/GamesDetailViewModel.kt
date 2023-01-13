package id.perqara.testing_perqara.ui.games_detail

import androidx.lifecycle.MutableLiveData
import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.data.repository.games.GamesRepository
import id.perqara.testing_perqara.other.wrapper.EventWrapper
import id.perqara.testing_perqara.other.wrapper.RepositoryWrapper

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

        when (result) {
            is RepositoryWrapper.Success -> {
                stateLiveData.value = GamesDetailState.LoadGamesDetail(
                    result.content
                )
                gamesModel = result.content
            }
            is RepositoryWrapper.GenericError -> {
                stateLiveData.value = GamesDetailState.MinorError(result.message ?: "")
            }
            is RepositoryWrapper.NetworkError -> {
                stateLiveData.value = GamesDetailState.NetworkError
                eventLiveData.value = EventWrapper.OnNetworkError("Games Detail Page", "Games Detail")
            }
            is RepositoryWrapper.ServerError -> {
                stateLiveData.value = GamesDetailState.MinorError(
                    result.error ?: "Mohon maaf, telah terjadi kesalahan"
                )
            }
            is RepositoryWrapper.UnknownError -> {
                stateLiveData.value = GamesDetailState.MinorError(result.errorMessage)
            }
        }
    }
}