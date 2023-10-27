package com.pandulapeter.beagle.core

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.ScreenCaptureService
import com.pandulapeter.beagle.utils.BundleArgumentDelegate


internal class OverlayFragment : Fragment() {

    private var fileName: String = "file"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = BeagleCore.implementation.createOverlayLayout(requireActivity(), this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fileName = savedInstanceState?.fileName ?: fileName
    }

    fun startCapture(isForVideo: Boolean, fileName: String) {
        (context?.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as? MediaProjectionManager?).let { mediaProjectionManager ->
            if (mediaProjectionManager == null) {
                BeagleCore.implementation.onScreenCaptureReady?.invoke(null)
            } else {
                this.fileName = fileName
                startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), if (isForVideo) SCREEN_RECORDING_REQUEST else SCREENSHOT_REQUEST)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SCREENSHOT_REQUEST,
            SCREEN_RECORDING_REQUEST -> {
                if (data == null) {
                    BeagleCore.implementation.onScreenCaptureReady?.invoke(null)
                } else {
                    requireContext().run {
                        startService(
                            ScreenCaptureService.getStartIntent(
                                this,
                                resultCode,
                                data,
                                requestCode == SCREEN_RECORDING_REQUEST,
                                fileName
                            )
                        )
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.fileName = fileName
    }

    companion object {
        private var Bundle.fileName by BundleArgumentDelegate.String("fileName")
        private const val SCREENSHOT_REQUEST = 4246
        private const val SCREEN_RECORDING_REQUEST = 4247

        fun newInstance() = OverlayFragment()
    }
}