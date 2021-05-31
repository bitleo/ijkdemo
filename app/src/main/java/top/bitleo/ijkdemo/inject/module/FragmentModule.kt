package top.bitleo.ijkdemo.inject.module

import android.content.Context
import dagger.Module
import dagger.Provides
import top.bitleo.ijkdemo.inject.FragmentScope
import top.bitleo.ijkdemo.ui.fragment.base.BaseFragment


@Module
class FragmentModule(private val mFragment: BaseFragment<*>) {

    @Provides
    @FragmentScope
    fun provideFragment(): BaseFragment<*> {
        return mFragment
    }

    @Provides
    @FragmentScope
    fun provideContext(): Context {
        return mFragment.activity!!
    }
}
