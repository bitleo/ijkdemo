package top.bitleo.ijkdemo

import android.app.Application
import top.bitleo.ijkdemo.util.NetHelper


class ClientApplication : Application() {



    override fun onCreate() {
        super.onCreate()
        app = this
        NetHelper.INSTANCE.init(this)

    }










    companion object{
        var app: ClientApplication? = null
        const val SIG_FUNC = "getSignature"
        const val TAG = "ClientApplication"


    }
}
