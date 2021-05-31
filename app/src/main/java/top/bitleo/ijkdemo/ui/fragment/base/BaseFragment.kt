package top.bitleo.ijkdemo.ui.fragment.base

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup


import javax.inject.Inject

import butterknife.ButterKnife
import butterknife.Unbinder
import es.dmoral.toasty.Toasty
import top.bitleo.ijkdemo.ClientApplication
import top.bitleo.ijkdemo.R
import top.bitleo.ijkdemo.inject.component.AppComponent
import top.bitleo.ijkdemo.mvp.contract.base.IBaseContract



abstract class BaseFragment<P : IBaseContract.Presenter<*>> : androidx.fragment.app.Fragment(), IBaseContract.View {

    private val TAG = "BaseFragment"

    @JvmField @Inject
    var mPresenter: P? = null
    private var mProgressDialog: ProgressDialog? = null
    internal var unbinder: Unbinder?= null

    /**
     * fragment for viewpager
     */
    private var isPagerFragment = false
    var isFragmentShowed = false
        private set

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected val appApplication: ClientApplication?
        get() = ClientApplication.app

    protected val appComponent: AppComponent?
        get() = appApplication!!.getAppComponent()

    protected abstract fun setupFragmentComponent(appComponent: AppComponent?)

    protected abstract fun initFragment(savedInstanceState: Bundle?)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //some page contain WebView will make default language changed
//        AppUtils.updateAppLanguage(activity!!)
        val fragmentView = inflater.inflate(layoutId, container, false)
        unbinder = ButterKnife.bind(this, fragmentView)
        initFragment(savedInstanceState)
        if (mPresenter != null) mPresenter!!.onViewInitialized()
        return fragmentView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFragmentComponent(appComponent)

        if (mPresenter != null) mPresenter!!.onRestoreInstanceState(arguments)
        if (mPresenter != null) mPresenter!!.onRestoreInstanceState(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (mPresenter != null) mPresenter!!.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
        if (mPresenter != null) mPresenter!!.detachView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        //        Glide.with(this).onLowMemory() //bitleo to do
    }

    open fun onFragmentShowed() {
        isFragmentShowed = true
    }

    fun onFragmentHided() {
        isFragmentShowed = false
    }

    override fun showProgressDialog(content: String) {
        getProgressDialog(content)
        mProgressDialog!!.show()
    }

    override fun dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        } else {
            //LogUtil.e(TAG,"dismissProgressDialog: can't dismiss a null dialog, must showForRepo dialog first!")
        }
    }

    override fun getProgressDialog(content: String): ProgressDialog {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(activity)
            mProgressDialog!!.setCancelable(false)
        }
        mProgressDialog!!.setMessage(content)
        return mProgressDialog as ProgressDialog
    }

    override  fun showErrorToast(message :String) {
        activity?.let { Toasty.error(it, message).show() };
    }

    override fun showInfoToast(message: String) {
        activity?.let { Toasty.info(it, message).show() };
    }



    fun isPagerFragment(): Boolean {
        return isPagerFragment
    }

    fun setPagerFragment(flag: Boolean): BaseFragment<*> {
        isPagerFragment = flag
        return this
    }

//    override fun showLoginPage() {
//
//        val intent = Intent(activity, LoginActivity::class.java)
//        startActivity(intent)
//    }

    open fun scrollToTop() {

    }

    override fun updateBadge() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    protected fun showOperationTip(@StringRes msgResId: Int) {
//        if (view != null) {
//            val snackbar = Snackbar.make(view!!, msgResId, Snackbar.LENGTH_INDEFINITE)
//                .setAction(R.string.ok) { v ->
//
//                }
//            snackbar.show()
//        }
    }

}
