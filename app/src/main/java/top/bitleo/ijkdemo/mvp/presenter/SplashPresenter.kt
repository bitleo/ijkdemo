package top.bitleo.ijkdemo.mvp.presenter



import top.bitleo.ijkdemo.mvp.contract.ISplashContract
import top.bitleo.ijkdemo.mvp.presenter.base.BasePresenter
import javax.inject.Inject

class SplashPresenter : BasePresenter<ISplashContract.View>(),
    ISplashContract.Presenter  {

//    private var authUser: AuthUser? = null
    private var isMainPageShowed = false

    override fun getUser() {

    }


    private fun showMain() {
        if (!isMainPageShowed) {
            isMainPageShowed = true
            mView?.showMainPage()
        }
    }

}