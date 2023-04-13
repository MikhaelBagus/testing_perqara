package id.perqara.testing_perqara

import android.app.Application
import android.content.Context
import id.perqara.testing_perqara.other.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        //Realm.init(this)
        appContext = applicationContext
        startKoin {
            androidContext(this@MyApp)
            androidLogger(Level.NONE)
            modules(appModule)
        }
    }

    companion object {
        var appContext: Context? = null
            private set
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }
}