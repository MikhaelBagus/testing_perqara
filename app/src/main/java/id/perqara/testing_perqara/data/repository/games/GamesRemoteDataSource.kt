package id.perqara.testing_perqara.data.repository.games

import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.data.remote.endpoint.GamesService
import id.perqara.testing_perqara.other.wrapper.PagingRepositoryWrapper
import id.perqara.testing_perqara.other.wrapper.RepositoryWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class GamesRemoteDataSource @Inject constructor(
    @Named("gamesService") private val gamesService: GamesService,
) {
    open suspend fun getGamesList(
        page: Int?,
        page_size: Int?,
        search: String?,
        key: String,
    ): PagingRepositoryWrapper<List<GamesModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = gamesService.getGamesList(
                    page,
                    page_size,
                    search,
                    key,
                ).execute()
                RemoteResultHelper.processPagingRemoteResponse(response)
            } catch (e: Exception) {
                RemoteResultHelper.processPagingRemoteException(e)
            }
        }
    }

    open suspend fun getGamesDetail(
        gamesId: Int,
        key: String
    ): RepositoryWrapper<List<GamesModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = gamesService.getGamesDetail(
                    gamesId,
                    key
                ).execute()
                RemoteResultHelper.processRemoteResponse(response)
            } catch (e: Exception) {
                RemoteResultHelper.processRemoteException(e)
            }
        }
    }
}