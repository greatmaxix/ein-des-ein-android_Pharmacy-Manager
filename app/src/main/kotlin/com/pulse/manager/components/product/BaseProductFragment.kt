package com.pulse.manager.components.product

import androidx.annotation.LayoutRes
import androidx.lifecycle.lifecycleScope
import com.pulse.manager.components.product.model.Product
import com.pulse.manager.components.scanner.ScannerFragment
import com.pulse.manager.components.scanner.ScannerListFragment
import com.pulse.manager.components.search.SearchFragment
import com.pulse.manager.components.search.SearchFragmentDirections
import com.pulse.manager.core.base.fragment.BaseToolbarFragment
import com.pulse.manager.core.extensions.observe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.component.KoinApiExtension
import kotlin.reflect.KClass

@KoinApiExtension
@ExperimentalCoroutinesApi
abstract class BaseProductFragment<VM : BaseProductViewModel>(@LayoutRes private val layoutResourceId: Int, viewModelClass: KClass<VM>) :
    BaseToolbarFragment<VM>(layoutResourceId, viewModelClass) {

    protected fun performProductInfoRequest(globalProductId: Int) {
        viewModel.requestProductInfo(globalProductId)
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(viewModel.productEvent.events) {
            navController.navigate(getNavDirection(it))
        }
    }

    private fun getNavDirection(product: Product) = when (this) {
        is SearchFragment -> SearchFragmentDirections.fromSearchToProduct(product)
        is ScannerFragment -> SearchFragmentDirections.globalToProductCard(product)
        is ScannerListFragment -> SearchFragmentDirections.globalToProductCard(product)
        else -> throw Exception("Add new instance to base product")
    }
}