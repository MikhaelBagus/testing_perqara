package id.perqara.testing_perqara.other.module

import id.perqara.testing_perqara.data.remote.endpoint.GamesService
import id.perqara.testing_perqara.data.repository.games.GamesRemoteDataSource

@Module
@InstallIn(ActivityRetainedComponent::class)
object DataSourceModule {
    @Provides
    @Named("gamesRemoteDataSource")
    fun provideGamesRemoteDataSource(
        @Named("gamesService") service: GamesService
    ): GamesRemoteDataSource =
        GamesRemoteDataSource(service)
}