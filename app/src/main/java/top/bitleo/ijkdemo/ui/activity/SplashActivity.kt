package top.bitleo.ijkdemo.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import es.dmoral.toasty.Toasty
import top.bitleo.ijkdemo.R
import top.bitleo.ijkdemo.inject.component.AppComponent
import top.bitleo.ijkdemo.inject.component.DaggerActivityComponent
import top.bitleo.ijkdemo.mvp.contract.ISplashContract
import top.bitleo.ijkdemo.mvp.presenter.SplashPresenter
import top.bitleo.ijkdemo.ui.activity.base.BaseActivity

/*
  splash 页面，初始化操作
 */
class SplashActivity: BaseActivity<SplashPresenter>(), ISplashContract.View{
    override fun setupActivityComponent(appComponent: AppComponent) {

        DaggerActivityComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(this)
    }
    override fun settingContentView() {
        mPresenter?.attachView(this)
        setContentView(R.layout.activity_splash)
    }

    override fun showMainPage() {
        delayFinish()
        startActivity(Intent(this, MainActivity::class.java))
    }


    override fun initView(savedInstanceState: Bundle?) {
       // window.statusBarColor = Color.WHITE
       // toolbar?.setBackgroundColor(Color.WHITE)
    }

    override fun initActivity() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_PHONE_STATE
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            mPresenter?.getUser()

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE), PERMISSIONS_REQUEST_READ_PHONE_STATE
            )
        }

    }

    override fun showErrorToast(message: String) {
        Toasty.error(getActivity(), message).show()
    }

    private val PERMISSIONS_REQUEST_READ_PHONE_STATE = 10001

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSIONS_REQUEST_READ_PHONE_STATE->{
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPresenter?.getUser()
                } else {
                  //  Toast.makeText(this, R.string.trial_permission_required, Toast.LENGTH_SHORT).show()

                }
            }
        }
    }


}