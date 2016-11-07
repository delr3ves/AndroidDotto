package io.github.delr3ves.dotto

import android.app.Activity
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Bundle
import android.os.Handler
import android.util.Size
import android.view.Surface
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import io.github.delr3ves.dotto.core.model.IndicatorStatus
import io.github.delr3ves.dotto.flash.FlashManager

class TorchActivity : Activity() {
    private var session: CameraCaptureSession? = null
    private var builder: CaptureRequest.Builder? = null
    private var cameraDevice: CameraDevice? = null
    private var cameraManager: CameraManager? = null
    private var flashManager: FlashManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_torch)
        val flashButton = findViewById(R.id.flashlight_button) as ImageButton
        flashButton.setOnClickListener(FlashSwitcherListener())
        initializeFlashIfNeeded()
    }

    @SuppressWarnings("ResourceType")
    @Throws(CameraAccessException::class)
    private fun initializeFlashIfNeeded() {
        if (flashManager == null) {
            cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraCharacteristics = cameraManager!!.getCameraCharacteristics("0")
            val flashAvailable = cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
            if (flashAvailable) {
                cameraManager!!.openCamera("0", FlashCameraDeviceStateCallback(), null)
            } else {
                Toast.makeText(this, "Flash not available", Toast.LENGTH_SHORT).show()
            }
            cameraManager!!.openCamera("0", FlashCameraDeviceStateCallback(), null)
        }
    }

    private var surfaceTexture: SurfaceTexture? = null
    private var surface: Surface? = null

    /**
     * camera device callback
     */
    internal inner class FlashCameraDeviceStateCallback : CameraDevice.StateCallback() {

        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            try {
                builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                flashManager = FlashManager(builder!!)

                builder!!.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AF_MODE_AUTO)
                surfaceTexture = SurfaceTexture(1)
                val size = getSmallestSize(cameraDevice!!.getId())
                surfaceTexture!!.setDefaultBufferSize(size.getWidth(), size.getHeight())
                surface = Surface(surfaceTexture)
                builder!!.addTarget(surface)
                camera.createCaptureSession(arrayListOf(surface), FlashSwitcherSessionStateCallback(), null)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        override fun onDisconnected(p0: CameraDevice?) {
        }

        override fun onError(camera: CameraDevice, error: Int) {

        }
    }

    @Throws(CameraAccessException::class)
    private fun getSmallestSize(cameraId: String): Size {
        val outputSizes = cameraManager!!.getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                .getOutputSizes(SurfaceTexture::class.java)
        if (outputSizes == null || outputSizes!!.size == 0) {
            throw IllegalStateException(
                    "Camera " + cameraId + "doesn't support any outputSize.")
        }
        var chosen = outputSizes!![0]
        for (s in outputSizes!!) {
            if (chosen.getWidth() >= s.getWidth() && chosen.getHeight() >= s.getHeight()) {
                chosen = s
            }
        }
        return chosen
    }

    /**
     * session callback
     */
    internal inner class FlashSwitcherSessionStateCallback : CameraCaptureSession.StateCallback() {

        override fun onConfigured(session: CameraCaptureSession) {
            this@TorchActivity.session = session
            try {
                this@TorchActivity.session!!.setRepeatingRequest(builder!!.build(), null, null)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }

        }

        override fun onConfigureFailed(session: CameraCaptureSession) {

        }
    }

    /**
     * switch listener
     */
    internal inner class FlashSwitcherListener : View.OnClickListener {

        override fun onClick(view: View?) {
            try {
                val button = view as ImageButton
                val newStatus = flashManager!!.toggle()
                when (newStatus) {
                    is IndicatorStatus.On -> {
                        Handler().postDelayed({ onClick(view) }, 2000)
                        button.setImageResource(R.drawable.flashlight_on)
                    }
                    is IndicatorStatus.Off -> {
                        button.setImageResource(R.drawable.flashlight_off)
                    }
                }
                session!!.setRepeatingRequest(builder!!.build(), null, null)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

    }

    private fun close() {
        if (cameraDevice == null || session == null) {
            return
        }
        session!!.close()
        cameraDevice!!.close()
        cameraDevice = null
        session = null
    }

    override fun onDestroy() {
        super.onDestroy()
        close()
    }
}