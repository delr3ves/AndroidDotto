package io.github.delr3ves.dotto

import android.Manifest
import android.app.Application
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener

class DottoApplication : Application() {

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
    }
}