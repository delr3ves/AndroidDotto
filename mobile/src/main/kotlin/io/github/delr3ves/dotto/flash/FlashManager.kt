package io.github.delr3ves.dotto.flash

import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import io.github.delr3ves.dotto.core.model.IndicatorStatus
import io.github.delr3ves.dotto.core.model.IndicatorStatus.Off
import io.github.delr3ves.dotto.core.model.IndicatorStatus.On

class FlashManager(builder: CaptureRequest.Builder) {

    private val builder = builder

    fun toggle(): IndicatorStatus {
        when (getStatus()) {
            is Off -> {
                turnOn()
                return On()
            }
            is On -> {
                turnOff()
                return Off()
            }
        }
    }

    fun turnOn() {
        builder!!.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH)

    }

    fun turnOff() {
        builder!!.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_OFF)
    }

    fun getStatus(): IndicatorStatus {
        if (builder!!.get(CaptureRequest.FLASH_MODE).equals(CameraMetadata.FLASH_MODE_OFF)) {
            return Off()
        } else {
            return On()
        }
    }
}

