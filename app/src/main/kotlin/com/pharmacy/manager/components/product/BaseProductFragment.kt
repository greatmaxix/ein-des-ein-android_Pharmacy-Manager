package com.pharmacy.manager.components.product

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment

abstract class BaseProductFragment<VM : BaseProductViewModel>(@LayoutRes private val layoutResourceId: Int, private val viewModel: VM) : BaseMVVMFragment(layoutResourceId) {

    override fun onBindLiveData() {
        observe(viewModel.errorLiveData, ::errorOrDialog)
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(this) }
//        observe(viewModel.productLiteLiveData) { navController.navigate(getNavDirection(this)) } TODO
    }

    private fun errorOrDialog(@StringRes strResId: Int) {
        messageCallback?.showError(strResId)
    }

//    private fun getNavDirection(product: Product) = when (this) { // TODO
//        is SearchFragment -> SearchFragmentDirections.fromSearchToProduct(product)
//        is ScannerFragment -> SearchFragmentDirections.globalToProductCard(product)
//        is ScannerListFragment -> SearchFragmentDirections.globalToProductCard(product)
//        else -> throw Exception("Add new instance to base product")
//    }
}