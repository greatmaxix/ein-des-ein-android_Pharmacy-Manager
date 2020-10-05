package com.pharmacy.manager.components.search

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.pharmacy.manager.R
import com.pharmacy.manager.components.product.BaseProductFragment
import com.pharmacy.manager.components.product.BaseProductViewModel
import com.pharmacy.manager.components.product.model.ProductLite
import com.pharmacy.manager.components.search.adapter.ProductListAdapter
import com.pharmacy.manager.core.extensions.addAutoKeyboardCloser

abstract class BaseProductListFragment<VM : BaseProductViewModel>(@LayoutRes private val layoutResourceId: Int, private val viewModel: VM) :
    BaseProductFragment<VM>(layoutResourceId, viewModel) {

    protected val productAdapter = ProductListAdapter(::performProductInfoRequest)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.rvProducts)?.apply {
            adapter = productAdapter
            addAutoKeyboardCloser()
        }

        productAdapter.addLoadStateListener { progressCallback?.setInProgress(it.refresh is LoadState.Loading || it.append is LoadState.Loading) }
    }

    abstract val productLiveData: LiveData<PagingData<ProductLite>>

    override fun onBindLiveData() {
        super.onBindLiveData()

        observe(productLiveData) { productAdapter.submitData(lifecycle, this) }
    }
}