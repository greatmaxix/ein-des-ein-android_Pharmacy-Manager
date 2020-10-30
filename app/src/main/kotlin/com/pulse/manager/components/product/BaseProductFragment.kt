package com.pulse.manager.components.product

import androidx.annotation.LayoutRes
import com.pulse.manager.components.product.model.Product
import com.pulse.manager.components.scanner.ScannerFragment
import com.pulse.manager.components.scanner.ScannerListFragment
import com.pulse.manager.components.search.SearchFragment
import com.pulse.manager.components.search.SearchFragmentDirections
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import org.koin.core.component.KoinApiExtension
import timber.log.Timber

@KoinApiExtension
abstract class BaseProductFragment<VM : BaseProductViewModel>(@LayoutRes private val layoutResourceId: Int, private val viewModel: VM) : BaseMVVMFragment(layoutResourceId) {

    protected fun performProductInfoRequest(globalProductId: Int) {
        Timber.e("perform $globalProductId")
        observeResult(viewModel.requestProductInfo(globalProductId)) {
            onEmmit = {
                Timber.e("emit $this")
                navController.navigate(getNavDirection(this))
            }
        }
    }

    private fun getNavDirection(product: Product) = when (this) {
        is SearchFragment -> SearchFragmentDirections.fromSearchToProduct(product)
        is ScannerFragment -> SearchFragmentDirections.globalToProductCard(product)
        is ScannerListFragment -> SearchFragmentDirections.globalToProductCard(product)
        else -> throw Exception("Add new instance to base product")
    }
}