package top.bitleo.ijkdemo.inject

import java.lang.annotation.Documented
import java.lang.annotation.Retention

import javax.inject.Scope

import java.lang.annotation.RetentionPolicy.RUNTIME


@Documented
@Scope
@Retention(RUNTIME)
annotation class FragmentScope
