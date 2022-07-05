package com.pulse.manager.components.search

import androidx.annotation.LayoutRes
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.pulse.manager.R
import com.pulse.manager.components.product.BaseProductFragment
import com.pulse.manager.components.product.BaseProductViewModel
import com.pulse.manager.components.product.model.ProductLite
import com.pulse.manager.components.search.adapter.ProductListAdapter
import com.pulse.manager.core.extensions.addAutoKeyboardCloser
import com.pulse.manager.core.extensions.addStateListener
import com.pulse.manager.core.extensions.observe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinApiExtension
import kotlin.reflect.KClass

@ExperimentalCoroutinesApi
@KoinApiExtension
abstract class BaseProductListFragment<VM : BaseProductViewModel>(@LayoutRes private val layoutResourceId: Int, viewModelClass: KClass<VM>) :
    BaseProductFragment<VM>(layoutResourceId, viewModelClass) {

    protected val productAdapter = ProductListAdapter(::performProductInfoRequest)

    override fun initUI() {
        requireView().findViewById<RecyclerView>(R.id.rv_products)?.apply {
            adapter = productAdapter
            addAutoKeyboardCloser()
        }
        productAdapter.addStateListener { uiHelper.showLoading(it) }
    }

    abstract val productFlow: Flow<PagingData<ProductLite>>

    override fun onBindStates() {
        super.onBindStates()
        with(lifecycleScope) {
            observe(productFlow) { productAdapter.submitData(lifecycle, it) }
        }
    }
}