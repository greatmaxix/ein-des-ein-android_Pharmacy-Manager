package com.pulse.manager.components.scanner

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.pulse.manager.R
import com.pulse.manager.components.product.BaseProductFragment
import com.pulse.manager.components.product.ProductViewModel
import com.pulse.manager.components.scanner.adapter.ProductListScannerAdapter
import kotlinx.android.synthetic.main.fragment_scanner_result.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ScannerListFragment(private val viewModel: ProductViewModel) : BaseProductFragment<ProductViewModel>(R.layout.fragment_scanner_result, viewModel) {

    private val args by navArgs<ScannerListFragmentArgs>()

    private val adapter by lazy { ProductListScannerAdapter(::performProductInfoRequest, args.products.toMutableList()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        rvProducts.adapter = adapter
    }
}