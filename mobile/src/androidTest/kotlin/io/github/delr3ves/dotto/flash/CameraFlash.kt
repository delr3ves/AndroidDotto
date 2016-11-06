package io.github.delr3ves.dotto.flash

import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest


class CameraFlash(camera: CameraDevice) {

    private val camera: CameraDevice = camera
    private val builder: CaptureRequest.Builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_MANUAL);


    fun turnOn() {
        builder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AF_MODE_AUTO);
        builder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);

    }

    fun turnOff() {
    }
}