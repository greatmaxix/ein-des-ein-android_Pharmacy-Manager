package com.pulse.manager.components.search

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.pulse.manager.R
import com.pulse.manager.components.product.BaseProductFragment
import com.pulse.manager.components.product.BaseProductViewModel
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.components.search.adapter.ProductListAdapter
import com.pulse.manager.core.extensions.addAutoKeyboardCloser
import com.pulse.manager.core.extensions.addStateListener
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
abstract class BaseProductListFragment<VM : BaseProductViewModel>(@LayoutRes private val layoutResourceId: Int, private val viewModel: VM) :
    BaseProductFragment<VM>(layoutResourceId, viewModel) {

    protected val productAdapter = ProductListAdapter(::performProductInfoRequest)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.rv_products)?.apply {
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