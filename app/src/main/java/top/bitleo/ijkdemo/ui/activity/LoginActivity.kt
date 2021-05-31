package top.bitleo.ijkdemo.ui.activity

import top.bitleo.ijkdemo.R
import top.bitleo.ijkdemo.inject.component.AppComponent
import top.bitleo.ijkdemo.inject.component.DaggerActivityComponent
import top.bitleo.ijkdemo.mvp.contract.ILoginContract
import top.bitleo.ijkdemo.mvp.presenter.LoginPresenter
import top.bitleo.ijkdemo.ui.activity.base.BaseActivity


class LoginActivity : BaseActivity<LoginPresenter>(), ILoginContract.View{


    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerActivityComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(this)
    }

    override fun settingContentView() {
        mPresenter?.attachView(this)
        setContentView(R.layout.activity_login)
    }

    override fun initActivity() {

    }

    override fun onLoginComplete() {

    }


}