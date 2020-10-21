package com.pharmacy.manager.components.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.pharmacy.manager.R
import com.pharmacy.manager.components.chatList.adapter.ChatAdapter
import com.pharmacy.manager.components.home.HomeFragmentDirections.Companion.fromHomeToChat
import com.pharmacy.manager.components.home.HomeFragmentDirections.Companion.fromHomeToScanner
import com.pharmacy.manager.components.home.HomeFragmentDirections.Companion.fromHomeToSearch
import com.pharmacy.manager.components.home.HomeFragmentDirections.Companion.globalToProductCard
import com.pharmacy.manager.components.home.adapter.ProductAdapter
import com.pharmacy.manager.components.mercureService.MercureEventListenerService
import com.pharmacy.manager.components.product.model.Product
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.manager.core.extensions.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
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

    override fun onResume() {
        super.onResume()

        // TODO add manual mercure stop
        if (!requireContext().isServiceRunning(MercureEventListenerService::class.java)) {
            requireContext().startService(Intent(requireContext(), MercureEventListenerService::class.java))
        }
    }

    @ExperimentalPagingApi
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
        rvChatListHome.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        chatAdapter.addStateListener { progressCallback?.setInProgress(it) }
    }

    private fun initProductList() {
        rvProductsHome.adapter = productAdapter
        rvProductsHome.setHasFixedSize(true)
    }
}