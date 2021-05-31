package top.bitleo.ijkdemo.mvp.presenter

import top.bitleo.ijkdemo.mvp.contract.IAboutContract
import top.bitleo.ijkdemo.mvp.presenter.base.BasePresenter
import javax.inject.Inject


class AboutPresenter @Inject constructor() : BasePresenter<IAboutContract.View>(),
    IAboutContract.Presenter  {

}