package top.bitleo.ijkdemo.inject.component

import dagger.Component
import top.bitleo.ijkdemo.inject.FragmentScope
import top.bitleo.ijkdemo.inject.module.FragmentModule
import top.bitleo.ijkdemo.ui.fragment.*

@FragmentScope
@Component(modules = [FragmentModule::class], dependencies = [AppComponent::class])
interface FragmentComponent {

}
