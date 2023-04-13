package id.perqara.testing_perqara.ui.games_detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.data.repository.games.GamesRepository
import id.perqara.testing_perqara.other.base.BaseViewModel
import id.perqara.testing_perqara.other.base.Events

class GamesDetailViewModel(private val gamesRepository: GamesRepository) : BaseViewModel() {
    var gamesModel: GamesModel? = null
    var gamesId: Int? = 0
    var dataGamesModel = MutableLiveData<GamesModel>()

    fun getGamesDetail(gamesId: Int) {
        event.value = Events(isLoading = true)
        launch {
            gamesRepository.getGamesDetail(gamesId,
                "51d0e291cbd842ebbe3c6f76b04d68d6"
            ) { isSuccess, messages, datas ->
                Log.i("autolog", "datas: ${Gson().toJson(datas)}")
                event.value = Events(isLoading = false, message = messages, isSuccess = isSuccess)
                if (datas != null) {
                    dataGamesModel.value = datas
                }
            }
        }
    }
}