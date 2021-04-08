package com.pulse.manager.components.scanner

import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.product.BaseProductFragment
import com.pulse.manager.components.product.ProductViewModel
import com.pulse.manager.components.scanner.adapter.ProductListScannerAdapter
import com.pulse.manager.databinding.FragmentScannerResultBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ScannerListFragment : BaseProductFragment<ProductViewModel>(R.layout.fragment_scanner_result, ProductViewModel::class) {

    private val args by navArgs<ScannerListFragmentArgs>()
    private val binding by viewBinding(FragmentScannerResultBinding::bind)

    private val adapter by lazy { ProductListScannerAdapter(::performProductInfoRequest, args.products.toMutableList()) }

    override fun initUI() = with(binding) {
        showBackButton()
        rvProducts.adapter = adapter
    }
}