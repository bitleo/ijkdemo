package top.bitleo.ijkdemo

import android.app.Application
import top.bitleo.ijkdemo.inject.component.AppComponent
import top.bitleo.ijkdemo.inject.component.DaggerAppComponent
import top.bitleo.ijkdemo.inject.module.AppModule
import top.bitleo.ijkdemo.util.NetHelper


class ClientApplication : Application() {

    private var mAppComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        app = this
        mAppComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        NetHelper.INSTANCE.init(this)

    }

    fun getAppComponent(): AppComponent? {
        return mAppComponent
    }

    companion object{
        var app: ClientApplication? = null
        const val SIG_FUNC = "getSignature"
        const val TAG = "ClientApplication"


    }
}
