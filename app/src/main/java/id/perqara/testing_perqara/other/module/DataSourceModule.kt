package id.perqara.testing_perqara.other.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import id.perqara.testing_perqara.data.remote.endpoint.GamesService
import id.perqara.testing_perqara.data.repository.games.GamesRemoteDataSource
import javax.inject.Named

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