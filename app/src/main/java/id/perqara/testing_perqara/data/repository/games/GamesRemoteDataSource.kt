package id.perqara.testing_perqara.data.repository.games

import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.data.model.WrapperListModel
import id.perqara.testing_perqara.data.remote.endpoint.GamesService
import id.perqara.testing_perqara.other.wrapper.RepositoryWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

open class GamesRemoteDataSource @Inject constructor(
    @Named("gamesService") private val gamesService: GamesService,
) {
    open suspend fun getGamesList(
        page: Int?,
        page_size: Int?,
        search: String?,
        key: String,
    ): RepositoryWrapper<WrapperListModel> {
        return withContext(Dispatchers.IO) {
            val response = gamesService.getGamesList(page, page_size, search, key).execute()
            RepositoryWrapper.Success(response.body()!!)
        }
    }

    open suspend fun getGamesDetail(
        gamesId: Int,
        key: String
    ): RepositoryWrapper<GamesModel> {
        return withContext(Dispatchers.IO) {
            val response = gamesService.getGamesDetail(gamesId, key).execute()
            RepositoryWrapper.Success(response.body()!!)
        }
    }
}