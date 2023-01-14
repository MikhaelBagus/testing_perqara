package id.perqara.testing_perqara.other.module

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import id.perqara.testing_perqara.data.remote.retrofit.BaseApi
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(ActivityRetainedComponent::class)
object AppModule {
    @Provides
    @Named("baseApi")
    fun provideBaseApi(
        @ApplicationContext context: Context,
    ): Retrofit {
        return BaseApi.getRetrofitInstance()
    }

}