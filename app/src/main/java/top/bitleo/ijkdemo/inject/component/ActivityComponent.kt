package top.bitleo.ijkdemo.inject.component

import dagger.Component
import top.bitleo.ijkdemo.inject.ActivityScope
import top.bitleo.ijkdemo.inject.module.ActivityModule
import top.bitleo.ijkdemo.ui.activity.*


@ActivityScope
@Component(modules = [ActivityModule::class], dependencies = [AppComponent::class])
interface ActivityComponent {
    fun inject(activity: LoginActivity)
    fun inject(activity: SplashActivity)
    fun inject(activity: MainActivity)
}
