package id.perqara.testing_perqara.other.module

import id.perqara.testing_perqara.data.repository.games.GamesRemoteDataSource
import id.perqara.testing_perqara.data.repository.games.GamesRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {
    @Provides
    @Named("gamesRepository")
    fun provideGamesRepository(
        @Named("gamesRemoteDataSource") gamesRemoteDataSource: GamesRemoteDataSource
    ): GamesRepository = GamesRepository(gamesRemoteDataSource)
}