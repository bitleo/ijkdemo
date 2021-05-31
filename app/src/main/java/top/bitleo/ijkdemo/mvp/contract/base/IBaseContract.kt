package top.bitleo.ijkdemo.mvp.contract.base


import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle


/**
 * Created by shadow
 * Time: 2019/5/14
 */
interface IBaseContract {

    interface View {
        fun showProgressDialog(content: String)

        fun dismissProgressDialog()

        fun getProgressDialog(content: String): ProgressDialog

        //fun showLoginPage()

        fun showErrorToast(message: String)

        fun showInfoToast(message: String)

        fun updateBadge();
    }

    interface Presenter<V : IBaseContract.View> {

        val context: Context?

        fun onSaveInstanceState(outState: Bundle?)

        fun onRestoreInstanceState(outState: Bundle?)

        fun attachView(view: V)

        fun detachView()

        /**
         * view initialized, you can init view data
         */
        fun onViewInitialized()
    }

}
