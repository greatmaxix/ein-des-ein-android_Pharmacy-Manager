package com.pharmacy.manager.components.scanner

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.pharmacy.manager.R
import com.pharmacy.manager.components.product.BaseProductFragment
import com.pharmacy.manager.components.product.ProductViewModel
import com.pharmacy.manager.components.scanner.adapter.ProductListScannerAdapter
import kotlinx.android.synthetic.main.fragment_scanner_result.*

class ScannerListFragment(private val viewModel: ProductViewModel) : BaseProductFragment<ProductViewModel>(R.layout.fragment_scanner_result, viewModel) {

    private val args by navArgs<ScannerListFragmentArgs>()

    private val adapter by lazy { ProductListScannerAdapter(::performProductInfoRequest, args.products.toMutableList()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        rvProducts.adapter = adapter
    }
}