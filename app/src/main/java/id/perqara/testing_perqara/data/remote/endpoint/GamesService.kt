package id.perqara.testing_perqara.data.remote.endpoint

import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.data.model.WrapperListModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GamesService {
    @GET("games")
    fun getGamesList(
        @Query("page") page: Int?,
        @Query("page_size") page_size: Int?,
        @Query("search") search: String?,
        @Query("key") key: String,
    ): Observable<Response<WrapperListModel>>

    @GET("games/{games_id}")
    fun getGamesDetail(
        @Path(value = "games_id", encoded = true) gamesId: Int,
        @Query("key") key: String,
    ): Observable<Response<GamesModel>>
}