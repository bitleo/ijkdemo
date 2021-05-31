package top.bitleo.ijkdemo.mvp.contract

import top.bitleo.ijkdemo.mvp.contract.base.IBaseContract


interface ISplashContract {

    interface View : IBaseContract.View {
         fun showMainPage()
    }

    interface Presenter : IBaseContract.Presenter<View> {
        fun getUser();
    }

}
