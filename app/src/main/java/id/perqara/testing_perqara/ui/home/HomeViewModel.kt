package id.perqara.testing_perqara.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import id.perqara.testing_perqara.data.model.WrapperListModel
import id.perqara.testing_perqara.data.repository.games.GamesRepository
import id.perqara.testing_perqara.other.base.BaseViewModel
import id.perqara.testing_perqara.other.base.Events

class HomeViewModel(private val gamesRepository: GamesRepository) : BaseViewModel() {
    var gamesCurrentPage = 1
    var gamesNext = ""
    var gamesSearch = ""
    var dataWrapperListModel = MutableLiveData<WrapperListModel>()

    fun getGamesList(page: Int, search: String?) {
        event.value = Events(isLoading = true)
        launch {
            gamesRepository.getGamesList(page,
                10,
                search,
                "51d0e291cbd842ebbe3c6f76b04d68d6"
            ) { isSuccess, messages, datas ->
                Log.i("autolog", "datas: ${Gson().toJson(datas)}")
                event.value = Events(isLoading = false, message = messages, isSuccess = isSuccess)
                if (datas != null) {
                    dataWrapperListModel.value = datas
                    gamesCurrentPage = page
                    gamesNext = datas.next.toString()
                    gamesSearch = search.toString()
                }
            }
        }
    }

    fun resetGamesPage() {
        gamesCurrentPage = 1
        gamesNext = ""
        gamesSearch = ""
    }
}