package id.perqara.testing_perqara.data.repository.games

import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.other.wrapper.PagingRepositoryWrapper
import id.perqara.testing_perqara.other.wrapper.RepositoryWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class GamesRepository @Inject constructor(
    @Named("gamesRemoteDataSource") private val gamesRemoteDataSource: GamesRemoteDataSource
) {
    suspend fun getGamesList(
        page: Int?,
        page_size: Int?,
        search: String?,
        key: String
    ): PagingRepositoryWrapper<List<GamesModel>> {
        return withContext(Dispatchers.IO) {
            val remoteResult = gamesRemoteDataSource.getGamesList(page)
            remoteResult
        }
    }

    suspend fun getGamesDetail(
        gamesId: Int,
        key: String
    ): RepositoryWrapper<List<GamesModel>> {
        return withContext(Dispatchers.IO) {
            val remoteResult = gamesRemoteDataSource.getGamesDetail(gamesId)
            remoteResult
        }
    }
}