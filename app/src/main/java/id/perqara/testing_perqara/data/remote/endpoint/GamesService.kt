package id.perqara.testing_perqara.data.remote.endpoint

import id.perqara.testing_perqara.data.model.GamesModel

interface GamesService {
    @GET("games")
    fun getGamesList(
        @Query("page") page: Int?,
        @Query("page_size") page_size: Int?,
        @Query("search") search: String?,
        @Query("key") key: String,
    ): Call<PagingResponseWrapper<List<GamesModel>>>

    @GET("games/{games_id}")
    fun getGamesDetail(
        @Path(value = "games_id", encoded = true) gamesId: Int,
        @Query("key") key: String,
    ): Call<ResponseWrapper<GamesModel>>
}