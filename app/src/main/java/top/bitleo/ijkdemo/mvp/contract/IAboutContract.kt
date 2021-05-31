package top.bitleo.ijkdemo.mvp.contract

import top.bitleo.ijkdemo.mvp.contract.base.IBaseContract

interface IAboutContract {

    interface View : IBaseContract.View {

    }

    interface Presenter : IBaseContract.Presenter<View> {

    }

}
