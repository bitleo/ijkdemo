package top.bitleo.ijkdemo.inject.component

import javax.inject.Singleton

import dagger.Component
import top.bitleo.ijkdemo.ClientApplication
import top.bitleo.ijkdemo.inject.module.AppModule

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    /**
     * 获取AppApplication
     * @return
     */
    val application: ClientApplication



}
