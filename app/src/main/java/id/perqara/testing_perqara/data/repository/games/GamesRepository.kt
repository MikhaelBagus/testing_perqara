package id.perqara.testing_perqara.data.repository.games

import com.google.gson.GsonBuilder
import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.data.model.WrapperListModel
import id.perqara.testing_perqara.data.remote.endpoint.GamesService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException

interface GamesRepository {
    fun getGamesList(
        page: Int?,
        page_size: Int?,
        search: String?,
        key: String,
        onResult: (isSuccess: Boolean?, messages: String?, datas: WrapperListModel?) -> Unit
    ): Disposable

    fun getGamesDetail(
        gamesId: Int,
        key: String,
        onResult: (isSuccess: Boolean?, messages: String?, datas: GamesModel?) -> Unit
    ): Disposable
}

class GamesRepositoryImpl(private val gamesService: GamesService) : GamesRepository {
    override fun getGamesList(
        page: Int?,
        page_size: Int?,
        search: String?,
        key: String,
        onResult: (isSuccess: Boolean?, messages: String?, datas: WrapperListModel?) -> Unit
    ): Disposable =
        gamesService.getGamesList(page, page_size, search, key).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()
        )
            .subscribe({
                if (it.isSuccessful) {
                    val resp = it.body()
                    if (resp != null) {
                        onResult(true, "${it.message()}", resp)
                    } else onResult(false, "${it.message()}", null)
                } else {
                    val gson = GsonBuilder().create()
                    var pojo = WrapperListModel()
                    try {
                        pojo = gson.fromJson(
                            it.errorBody()?.string(),
                            WrapperListModel::class.java
                        )
                        onResult(false, "", pojo)
                    } catch (e: IOException) {
                        // handle failure at error parse
                    }
                }

            }, {
                onResult(false, "${it.message}", null)
            })

    override fun getGamesDetail(
        gamesId: Int,
        key: String,
        onResult: (isSuccess: Boolean?, messages: String?, datas: GamesModel?) -> Unit
    ): Disposable =
        gamesService.getGamesDetail(gamesId, key).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()
        )
            .subscribe({
                if (it.isSuccessful) {
                    val resp = it.body()
                    if (resp != null) {
                        onResult(true, "${it.message()}", resp)
                    } else onResult(false, "${it.message()}", null)
                } else {
                    val gson = GsonBuilder().create()
                    var pojo = GamesModel()
                    try {
                        pojo = gson.fromJson(
                            it.errorBody()?.string(),
                            GamesModel::class.java
                        )
                        onResult(false, "", pojo)
                    } catch (e: IOException) {
                        // handle failure at error parse
                    }
                }

            }, {
                onResult(false, "${it.message}", null)
            })
}