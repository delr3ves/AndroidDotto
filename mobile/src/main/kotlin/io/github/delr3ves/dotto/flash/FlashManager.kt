package io.github.delr3ves.dotto.flash

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import io.github.delr3ves.dotto.core.model.IndicatorStatus
import io.github.delr3ves.dotto.core.model.IndicatorStatus.Off
import io.github.delr3ves.dotto.core.model.IndicatorStatus.On

class FlashManager(builder: CaptureRequest.Builder, session: CameraCaptureSession) {

    private val builder = builder
    private val session = session

    fun toggle(): IndicatorStatus {
        return when (getStatus()) {
            is Off -> {
                turnOn()
            }
            is On -> {
                turnOff()
            }
        }
    }

    fun setStatus(status: IndicatorStatus) = when (status) {
        is On -> turnOn()
        is Off -> turnOff()
    }


    fun turnOn(): IndicatorStatus {
        builder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH)
        session.setRepeatingRequest(builder!!.build(), null, null)
        return On
    }

    fun turnOff(): IndicatorStatus {
        builder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_OFF)
        session.setRepeatingRequest(builder!!.build(), null, null)
        return Off
    }

    fun getStatus(): IndicatorStatus {
        if (builder.get(CaptureRequest.FLASH_MODE).equals(CameraMetadata.FLASH_MODE_OFF)) {
            return Off
        } else {
            return On
        }
    }
}

