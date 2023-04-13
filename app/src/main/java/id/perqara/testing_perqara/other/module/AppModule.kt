package id.perqara.testing_perqara.other.module

import id.perqara.testing_perqara.MainViewModel
import id.perqara.testing_perqara.data.local.SessionPref
import id.perqara.testing_perqara.data.remote.endpoint.GamesService
import id.perqara.testing_perqara.data.remote.retrofit.ApiService
import id.perqara.testing_perqara.data.remote.retrofit.AuthInterceptor
import id.perqara.testing_perqara.data.remote.retrofit.OkHttpClientFactory
import id.perqara.testing_perqara.data.repository.games.GamesRepository
import id.perqara.testing_perqara.data.repository.games.GamesRepositoryImpl
import id.perqara.testing_perqara.ui.games_detail.GamesDetailViewModel
import id.perqara.testing_perqara.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { GamesDetailViewModel(get()) }
    viewModel { MainViewModel() }

    single<GamesRepository> { GamesRepositoryImpl(get()) }
    single { SessionPref.instance(androidContext()) }
    single { ApiService.createService(GamesService::class.java, get(), "https://api.rawg.io/api/") }
    single { OkHttpClientFactory.create(interceptors = arrayOf(AuthInterceptor(get())), showDebugLog = true) }
}