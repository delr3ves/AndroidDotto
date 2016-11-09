package io.github.delr3ves.dotto

import android.Manifest
import android.app.Application
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import io.github.delr3ves.dotto.di.ApplicationComponent
import io.github.delr3ves.dotto.di.ApplicationModule
import io.github.delr3ves.dotto.di.DaggerApplicationComponent

class DottoApplication : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        Dexter.initialize(this)

        val dialogPermissionListener = DialogOnDeniedPermissionListener.Builder.withContext(this)
                .withTitle("Camera permission")
                .withMessage("Camera permission is needed to turn the flash on")
                .withButtonText(android.R.string.ok)
                .withIcon(R.mipmap.ic_launcher)
                .build()
        Dexter.checkPermission(dialogPermissionListener, Manifest.permission.CAMERA)

        component = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
        component.inject(this)
        this.component = component
    }

}