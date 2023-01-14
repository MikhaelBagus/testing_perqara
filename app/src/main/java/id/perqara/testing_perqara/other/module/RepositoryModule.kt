package id.perqara.testing_perqara.other.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import id.perqara.testing_perqara.data.repository.games.GamesRemoteDataSource
import id.perqara.testing_perqara.data.repository.games.GamesRepository
import javax.inject.Named

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {
    @Provides
    @Named("gamesRepository")
    fun provideGamesRepository(
        @Named("gamesRemoteDataSource") gamesRemoteDataSource: GamesRemoteDataSource
    ): GamesRepository = GamesRepository(gamesRemoteDataSource)
}