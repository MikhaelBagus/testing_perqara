package id.perqara.testing_perqara.other.module

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