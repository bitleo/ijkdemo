package top.bitleo.ijkdemo.inject

import java.lang.annotation.Documented
import java.lang.annotation.Retention

import javax.inject.Scope

import java.lang.annotation.RetentionPolicy.RUNTIME

/**
 * activity生命周期内
 * Created by ThirtyDegreesRay on 2016/8/30 15:21
 */
@Documented
@Scope
@Retention(RUNTIME)
annotation class ActivityScope
