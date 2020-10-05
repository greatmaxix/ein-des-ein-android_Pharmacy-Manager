package com.pharmacy.manager.components.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pharmacy.manager.R
import com.pharmacy.manager.components.chatList.adapter.ChatAdapter
import com.pharmacy.manager.components.home.HomeFragmentDirections.Companion.fromHomeToChat
import com.pharmacy.manager.components.home.HomeFragmentDirections.Companion.fromHomeToScanner
import com.pharmacy.manager.components.home.HomeFragmentDirections.Companion.fromHomeToSearch
import com.pharmacy.manager.components.home.HomeFragmentDirections.Companion.globalToProductCard
import com.pharmacy.manager.components.home.adapter.ProductAdapter
import com.pharmacy.manager.components.product.model.Product
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.manager.core.extensions.animateVisibleOrGoneIfNot
import com.pharmacy.manager.core.extensions.compatColor
import com.pharmacy.manager.core.extensions.loadCircularImage
import com.pharmacy.manager.core.extensions.setDebounceOnClickListener
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment(private val vm: HomeViewModel) : BaseMVVMFragment(R.layout.fragment_home) {

    private val chatAdapter by lazy { ChatAdapter { navController.navigate(fromHomeToChat(it)) } }
    private val productAdapter by lazy {
        ProductAdapter {
            observeRestResult<Product> {
                liveData = vm.requestProductInfo(it.globalProductId)
                onEmmit = { navController.navigate(globalToProductCard(this)) }

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardScanHome.setDebounceOnClickListener { navController.navigate(fromHomeToScanner()) }
        cardSearchHome.setDebounceOnClickListener { navController.navigate(fromHomeToSearch()) }
        tvChatRequestsCounterHome.text = 12.toString()
        tvSoonCounterHome.text = "∞"

        initChatList()
        initProductList()

        val avatarAddress =
            "https://www.nj.com/resizer/h8MrN0-Nw5dB5FOmMVGMmfVKFJo=/450x0/smart/cloudfront-us-east-1.images.arcpublishing.com/advancelocal/SJGKVE5UNVESVCW7BBOHKQCZVE.jpg"
        ivAvatar4Home.loadCircularImage(avatarAddress, resources.getDimensionPixelSize(R.dimen._2sdp).toFloat(), requireContext().compatColor(R.color.colorGlobalWhite))
        ivAvatar3Home.loadCircularImage(avatarAddress, resources.getDimensionPixelSize(R.dimen._2sdp).toFloat(), requireContext().compatColor(R.color.colorGlobalWhite))
        ivAvatar2Home.loadCircularImage(avatarAddress, resources.getDimensionPixelSize(R.dimen._2sdp).toFloat(), requireContext().compatColor(R.color.colorGlobalWhite))
        ivAvatar1Home.loadCircularImage(avatarAddress, resources.getDimensionPixelSize(R.dimen._2sdp).toFloat(), requireContext().compatColor(R.color.colorGlobalWhite))

        vm.getRecentProductList()
    }

    override fun onBindLiveData() {
        super.onBindLiveData()

        observe(vm.chatListLiveData) { chatAdapter.submitData(lifecycle, this) }
        observe(vm.recentProductListLiveData) {
            productAdapter.notifyDataSet(this)
            tvRecommendedHome.animateVisibleOrGoneIfNot(!productAdapter.isEmpty())
            rvProductsHome.animateVisibleOrGoneIfNot(!productAdapter.isEmpty())
        }
    }

    private fun initChatList() {
        rvChatListHome.adapter = chatAdapter
        rvChatListHome.setHasFixedSize(true)
        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeRemoved(position: Int, count: Int) = rvChatListHome.animateVisibleOrGoneIfNot(chatAdapter.itemCount != 1)
            override fun onItemRangeInserted(position: Int, count: Int) = rvChatListHome.animateVisibleOrGoneIfNot(count != 0)
        })
    }

    private fun initProductList() {
        rvProductsHome.adapter = productAdapter
        rvProductsHome.setHasFixedSize(true)
    }
}