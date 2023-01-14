package id.perqara.testing_perqara.other.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import id.perqara.testing_perqara.data.remote.endpoint.GamesService
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(ActivityRetainedComponent::class)
object EndpointModule {
    @Provides
    @Named("gamesService")
    fun provideGamesService(
        @Named("baseApi") baseApi: Retrofit
    ): GamesService =
        baseApi.create(GamesService::class.java)
}