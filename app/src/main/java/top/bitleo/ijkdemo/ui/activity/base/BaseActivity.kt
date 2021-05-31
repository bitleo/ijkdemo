package top.bitleo.ijkdemo.ui.activity.base


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import es.dmoral.toasty.Toasty
import top.bitleo.ijkdemo.inject.component.AppComponent
import top.bitleo.ijkdemo.mvp.contract.base.IBaseContract
import top.bitleo.ijkdemo.ClientApplication
import top.bitleo.ijkdemo.ui.activity.LoginActivity
import top.bitleo.ijkdemo.ui.activity.SplashActivity
import javax.inject.Inject
//import es.dmoral.toasty.Toasty


abstract class BaseActivity<P : IBaseContract.Presenter<*>>(): AppCompatActivity() ,IBaseContract.View{

    @JvmField @Inject
    var mPresenter: P ? = null

    private var mProgressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if ( this.javaClass != SplashActivity::class.java
            && this.javaClass != LoginActivity::class.java
        ) {
            finishAffinity()
            startActivity(Intent(this, SplashActivity::class.java))
            return
        }

        setupActivityComponent(getAppComponent())

        mPresenter?.onRestoreInstanceState(savedInstanceState ?: intent.extras)

        settingContentView();
        ButterKnife.bind(this)
        initActivity();
        initView(savedInstanceState)
        mPresenter?.onViewInitialized()

    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(outState!=null){

            mPresenter?.onSaveInstanceState(outState)
        }
    }

    override fun showProgressDialog(content: String) {
        getProgressDialog(content)
        mProgressDialog?.show()
    }

    override fun dismissProgressDialog() {
        mProgressDialog?.dismiss()
    }

    override fun getProgressDialog(content: String): ProgressDialog {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.setCancelable(false)
        }
        mProgressDialog!!.setMessage(content)
        return mProgressDialog as ProgressDialog
    }

    override fun showErrorToast(message: String) {
        Toasty.error(getActivity(), message).show()
    }

    override fun showInfoToast(message: String) {
        Toasty.info(getActivity(), message).show()
    }

//    override fun showLoginPage(){
//        val intent = Intent(getActivity(), LoginActivity::class.java)
//        startActivity(intent)
//    }

    override fun updateBadge() {

    }

    override fun onResume() {
        super.onResume()
        curActivity = getActivity()
    }

    open fun delayFinish() {
        delayFinish(1000)
    }

    open fun delayFinish(mills: Int) {
        Handler().postDelayed({ finish() }, mills.toLong())
    }

    abstract fun settingContentView()

    abstract fun initActivity();

    open fun initView(savedInstanceState:Bundle?){

    }

    @NonNull
    open fun getActivity(): BaseActivity<*> {
        return this
    }
    @NonNull
    open fun getAppApplication(): ClientApplication {
        return application as ClientApplication
    }

    open fun getAppComponent(): AppComponent {
        return getAppApplication().getAppComponent()!!
    }





    /**
     * 依赖注入的入口
     * @param appComponent appComponent
     */
     abstract fun setupActivityComponent(appComponent: AppComponent)

    companion object{
        private var curActivity: BaseActivity<*>? = null
        fun getCurActivity(): BaseActivity<*> {
            return curActivity!!
        }
    }

}
