package id.perqara.testing_perqara.other.module

import android.content.Context

@Module
@InstallIn(ActivityRetainedComponent::class)
object AppModule {
    @Provides
    @Named("baseApi")
    fun provideBaseApi(
        @ApplicationContext context: Context,
    ): Retrofit {
        return BaseApi.getRetrofitInstance(context)
    }

}