package top.bitleo.ijkdemo.inject.module

import dagger.Module
import dagger.Provides
import top.bitleo.ijkdemo.ClientApplication
import javax.inject.Singleton


@Module
class AppModule(private val application: ClientApplication) {

    @Provides
    @Singleton
    fun provideApplication(): ClientApplication {
        return application
    }


}
