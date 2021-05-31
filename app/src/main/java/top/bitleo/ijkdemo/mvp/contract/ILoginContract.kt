package top.bitleo.ijkdemo.mvp.contract

import top.bitleo.ijkdemo.mvp.contract.base.IBaseContract


interface ILoginContract {

    interface View : IBaseContract.View {
        fun onLoginComplete()
    }

    interface Presenter : IBaseContract.Presenter<View> {
        fun basicLogin(name:String,password:String)

    }

}
