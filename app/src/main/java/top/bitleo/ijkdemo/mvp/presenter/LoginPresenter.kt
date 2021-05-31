package top.bitleo.ijkdemo.mvp.presenter


import top.bitleo.ijkdemo.mvp.contract.ILoginContract
import top.bitleo.ijkdemo.mvp.presenter.base.BasePresenter
import javax.inject.Inject

class LoginPresenter  : BasePresenter<ILoginContract.View>(),
    ILoginContract.Presenter {




    override fun basicLogin(email: String, password: String) {

        mView?.showProgressDialog("正在登陆...")


    }


    companion object{
        const val TAG="LoginPresenter"
    }

}
