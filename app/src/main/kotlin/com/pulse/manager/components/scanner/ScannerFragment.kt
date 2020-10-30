package com.pulse.manager.components.scanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.budiyev.android.codescanner.*
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyDenied
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.addListener
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.google.zxing.Result
import com.pulse.manager.R
import com.pulse.manager.components.product.BaseProductFragment
import com.pulse.manager.components.scanner.ScannerFragmentDirections.Companion.fromScannerToListResult
import com.pulse.manager.core.base.fragment.dialog.AlertDialogFragment
import com.pulse.manager.core.extensions.animateVisibleOrGoneIfNot
import com.pulse.manager.core.extensions.doWithDelay
import com.pulse.manager.core.extensions.setDebounceOnClickListener
import com.pulse.manager.core.extensions.toast
import kotlinx.android.synthetic.main.fragment_qr_code_scanner.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import timber.log.Timber

@KoinApiExtension
class ScannerFragment(private val viewModel: ScannerViewModel) : BaseProductFragment<ScannerViewModel>(R.layout.fragment_qr_code_scanner, viewModel) {

    private var codeScanner: CodeScanner? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        goToScanBtn.setDebounceOnClickListener { viewModel.descriptionViewed() }
        checkCameraPermission { initQRCamera() }
    }

    private fun checkCameraPermission(grant: () -> Unit) {
        val isDeviceSupportCamera: Boolean = requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        if (isDeviceSupportCamera) {
            val request = permissionsBuilder(Manifest.permission.CAMERA).build()
            request.addListener { result ->
                when {
                    result.allGranted() -> grant.invoke()
                    result.anyDenied() -> requireContext().toast(R.string.cameraPermissionDenied)
                    result.anyPermanentlyDenied() -> openSettings()
                    result.anyShouldShowRationale() -> {
                        AlertDialogFragment.newInstance(
                            message = getString(R.string.cameraPermissionRationaleMessage),
                            positive = getString(R.string.grantPermissionButton),
                            negative = getString(R.string.closeButton)
                        ).apply {
                            setPositiveListener { _, _ -> request.send() }
                        }.show(childFragmentManager)
                    }
                }
            }
            request.send()
        } else {
            requireContext().toast(R.string.cameraPermissionNoCameraOnDevice)
        }
    }

    private fun openSettings() {
        AlertDialogFragment.newInstance(
            message = getString(R.string.cameraPermissionPermanentlyDenied),
            positive = getString(R.string.permissionDialogSettingsButton),
            negative = getString(R.string.permissionDialogCancel)
        ).apply {
            setPositiveListener { _, _ ->
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", requireActivity().packageName, null)
                })
            }
        }.show(childFragmentManager)
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        observe(viewModel.descriptionVisibility) { qrCodeScannerInstructionGroup.animateVisibleOrGoneIfNot(this) }
    }

    private fun initQRCamera() {
        codeScanner = CodeScanner(requireContext(), codeScannerView)
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
                        performBarcodeSearch(it)
                    }
                }

                errorCallback = ErrorCallback {
                    lifecycleScope.launch(Main.immediate) {
                        Timber.e(it, "Error scanning qr code")
                        messageCallback?.showError(getString(R.string.qrCodeScannerError)) {
                            navController.popBackStack()
                        }
                    }
                }
                startPreview()
            }
    }

    private fun performBarcodeSearch(it: Result) {
        observeResult(viewModel.searchQrCode(it.text)) {
            onEmmit = {
                if (this.size == 1) {
                    performProductInfoRequest(this.first().globalProductId)
                } else {
                    navController.navigate(fromScannerToListResult(this.toTypedArray()))
                }
            }
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