package id.perqara.testing_perqara.data.repository.games

import id.perqara.testing_perqara.data.model.GamesModel
import id.perqara.testing_perqara.data.model.WrapperListModel
import id.perqara.testing_perqara.other.wrapper.RepositoryWrapper
import javax.inject.Inject
import javax.inject.Named

open class GamesRepository @Inject constructor(
    @Named("gamesRemoteDataSource") private val gamesRemoteDataSource: GamesRemoteDataSource
) {
    suspend fun getGamesList(
        page: Int?,
        page_size: Int?,
        search: String?,
        key: String
    ): RepositoryWrapper<WrapperListModel> {
        return gamesRemoteDataSource.getGamesList(page, page_size, search, key)
    }

    suspend fun getGamesDetail(
        gamesId: Int,
        key: String
    ): RepositoryWrapper<GamesModel> {
        return gamesRemoteDataSource.getGamesDetail(gamesId, key)
    }
}