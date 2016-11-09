package io.github.delr3ves.dotto.di

import android.app.Application
import android.content.Context
import android.hardware.camera2.CameraManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule(application: Application) {

    val application = application

    @Provides
    fun provideContext(): Context {
        return application
    }

    @Singleton
    @Provides
    fun provideCameraManager(): CameraManager {
        return application.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

}