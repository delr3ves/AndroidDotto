package io.github.delr3ves.dotto.flash

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.CameraManager
import android.util.Size
import android.view.Surface
import android.widget.Toast

class CameraManager(context: Context) {

    private val context = context
    private var session: CameraCaptureSession? = null
    private var cameraDevice: CameraDevice? = null
    private var cameraManager: CameraManager? = null
    private var flashManager: FlashManager? = null
    private var surfaceTexture: SurfaceTexture? = null
    private var surface: Surface? = null
    private var builder: CaptureRequest.Builder? = null

    @SuppressWarnings("ResourceType")
    @Throws(CameraAccessException::class)
    fun initializeFlashIfNeeded() {
        if (flashManager == null) {
            cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraCharacteristics = cameraManager!!.getCameraCharacteristics("0")
            val flashAvailable = cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
            if (flashAvailable) {
                cameraManager!!.openCamera("0", FlashCameraDeviceStateCallback(), null)
            } else {
                Toast.makeText(context, "Flash not available", Toast.LENGTH_SHORT).show()
            }
            cameraManager!!.openCamera("0", FlashCameraDeviceStateCallback(), null)
        }
    }

    fun getFlashManager(): FlashManager? {
        return flashManager
    }


    fun closeCamera() {
        if (cameraDevice == null || session == null) {
            return
        }
        session!!.close()
        cameraDevice!!.close()
        cameraDevice = null
        session = null
    }

    internal inner class FlashCameraDeviceStateCallback : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            try {
                builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

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

    internal inner class FlashSwitcherSessionStateCallback : CameraCaptureSession.StateCallback() {

        override fun onConfigured(cameraSession: CameraCaptureSession?) {
            session = cameraSession
            flashManager = FlashManager(builder!!, session!!)
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
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

}