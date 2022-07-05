package com.pulse.manager.components.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.budiyev.android.codescanner.*
import com.pulse.manager.R
import com.pulse.manager.components.product.BaseProductFragment
import com.pulse.manager.components.scanner.ScannerFragmentDirections.Companion.fromScannerToListResult
import com.pulse.manager.core.extensions.animateVisibleOrGoneIfNot
import com.pulse.manager.core.extensions.doWithDelay
import com.pulse.manager.core.extensions.observe
import com.pulse.manager.core.extensions.setDebounceOnClickListener
import com.pulse.manager.databinding.FragmentQrCodeScannerBinding
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import timber.log.Timber

@KoinApiExtension
@ExperimentalCoroutinesApi
class ScannerFragment : BaseProductFragment<ScannerViewModel>(R.layout.fragment_qr_code_scanner, ScannerViewModel::class) {

    private var codeScanner: CodeScanner? = null
    private val binding by viewBinding(FragmentQrCodeScannerBinding::bind)

    override fun initUI() = with(binding) {
        showBackButton()
        mbGoToScan.setDebounceOnClickListener { viewModel.descriptionViewed() }
        checkCameraPermission { initQRCamera() }
    }

    private fun checkCameraPermission(grant: () -> Unit) {
        val isDeviceSupportCamera: Boolean = requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        if (isDeviceSupportCamera) {
            requestPermissions(
                firstPermission = Manifest.permission.CAMERA,
                openSettingsMessage = R.string.cameraPermissionPermanentlyDenied,
                rationaleMessage = R.string.cameraPermissionRationaleMessage,
                deniedMessage = R.string.cameraPermissionDenied
            ) { grant.invoke() }
        } else {
            uiHelper.showMessage(getString(R.string.cameraPermissionNoCameraOnDevice))
        }
    }

    override fun onBindStates() {
        super.onBindStates()
        with(lifecycleScope) {
            observe(viewModel.descriptionVisibilityState) { binding.groupCodeScannerInstruction.animateVisibleOrGoneIfNot(it) }
            observe(viewModel.searchResultEvent.events) {
                if (it.size == 1) {
                    performProductInfoRequest(it.first().globalProductId)
                } else {
                    navController.navigate(fromScannerToListResult(it.toTypedArray()))
                }
            }
        }
    }

    private fun initQRCamera() {
        codeScanner = CodeScanner(requireContext(), binding.viewCodeScanner)
            .apply {
                camera = CodeScanner.CAMERA_BACK // TODO check on devices with only front camera available
                formats = CodeScanner.ONE_DIMENSIONAL_FORMATS
                autoFocusMode = AutoFocusMode.SAFE
                scanMode = ScanMode.SINGLE
                isAutoFocusEnabled = true
                isFlashEnabled = false

                decodeCallback = DecodeCallback {
                    if (it.text.isNullOrBlank()) {
                        doWithDelay(500) { startPreview() }
                    } else {
                        viewModel.searchQrCode(it.text)
                    }
                }

                errorCallback = ErrorCallback {
                    lifecycleScope.launch(Main.immediate) {
                        Timber.e(it, "Error scanning qr code")
                        uiHelper.showDialog(getString(R.string.qrCodeScannerError)) {
                            navController.popBackStack()
                        }
                    }
                }
                startPreview()
            }
    }

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }
}