package com.pharmacy.manager.components.search

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.pharmacy.manager.R
import com.pharmacy.manager.components.product.BaseProductFragment
import com.pharmacy.manager.components.product.BaseProductViewModel
import com.pharmacy.manager.components.product.model.ProductLite
import com.pharmacy.manager.components.search.adapter.ProductListAdapter
import com.pharmacy.manager.core.extensions.addAutoKeyboardCloser
import com.pharmacy.manager.core.extensions.addStateListener
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
abstract class BaseProductListFragment<VM : BaseProductViewModel>(@LayoutRes private val layoutResourceId: Int, private val viewModel: VM) :
    BaseProductFragment<VM>(layoutResourceId, viewModel) {

    protected val productAdapter = ProductListAdapter(::performProductInfoRequest)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.rvProducts)?.apply {
            adapter = productAdapter
            addAutoKeyboardCloser()
        }

        productAdapter.addStateListener { progressCallback?.setInProgress(it) }
    }

    abstract val productLiveData: LiveData<PagingData<ProductLite>>

    override fun onBindLiveData() {
        super.onBindLiveData()

        observe(productLiveData) { productAdapter.submitData(lifecycle, this) }
    }
}