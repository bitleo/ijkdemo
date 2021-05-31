package top.bitleo.ijkdemo.inject.module

import android.content.Context
import androidx.fragment.app.FragmentManager


import dagger.Module
import dagger.Provides
import top.bitleo.ijkdemo.inject.ActivityScope
import top.bitleo.ijkdemo.ui.activity.base.BaseActivity


@Module
class ActivityModule(private val mActivity: BaseActivity<*>) {

    @Provides
    @ActivityScope
    fun provideActivity(): BaseActivity<*> {
        return mActivity
    }

    @Provides
    @ActivityScope
    fun provideContext(): Context {
        return mActivity
    }

    @Provides
    @ActivityScope
    fun provideFragmentManager(): FragmentManager {
        return mActivity.supportFragmentManager
    }

}
