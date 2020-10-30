package com.pharmacy.manager.components.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.pharmacy.manager.R
import com.pharmacy.manager.components.home.HomeFragmentDirections.Companion.fromHomeToChat
import com.pharmacy.manager.components.home.HomeFragmentDirections.Companion.fromHomeToScanner
import com.pharmacy.manager.components.home.HomeFragmentDirections.Companion.fromHomeToSearch
import com.pharmacy.manager.components.home.HomeFragmentDirections.Companion.globalToProductCard
import com.pharmacy.manager.components.home.adapter.ChatListAdapter
import com.pharmacy.manager.components.home.adapter.ProductAdapter
import com.pharmacy.manager.components.mercureService.MercureEventListenerService
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.manager.core.extensions.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class HomeFragment(private val vm: HomeViewModel) : BaseMVVMFragment(R.layout.fragment_home) {

    private lateinit var customerAvatars: List<ImageView>
    private val chatAdapter by lazy { ChatListAdapter { navController.navigate(fromHomeToChat(it)) } }
    private val productAdapter by lazy {
        ProductAdapter {
            observeResult(vm.requestProductInfo(it.globalProductId)) {
                onEmmit = { navController.navigate(globalToProductCard(this)) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customerAvatars = listOf(
            ivAvatar4Home,
            ivAvatar3Home,
            ivAvatar2Home,
            ivAvatar1Home
        )
        cardScanHome.setDebounceOnClickListener { navController.navigate(fromHomeToScanner()) }
        cardSearchHome.setDebounceOnClickListener { navController.navigate(fromHomeToSearch()) }
        tvSoonCounterHome.text = "∞"

        initChatList()
        initProductList()
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
        observeResult(vm.openedChats) {
            onEmmit = {
                val openedChatsCount = totalCount
                tvChatRequestsCounterHome.text = openedChatsCount.toString()
                val customerImages = items.map { it.customer.avatar?.url ?: "" }
                customerAvatars.forEachIndexed { index, imageView ->
                    val avatarUrl = customerImages.getOrNull(index)
                    avatarUrl?.let {
                        imageView.loadCircularImage(
                            it,
                            resources.getDimensionPixelSize(R.dimen._2sdp).toFloat(),
                            requireContext().compatColor(R.color.colorGlobalWhite)
                        )
                        imageView.animateVisibleIfNot()
                    } ?: run { imageView.animateGoneIfNot() }
                }
                chatAdapter.setList(this.items.take(2))
                rvChatListHome.animateVisibleOrGoneIfNot(!chatAdapter.isEmpty())
            }
        }
        observeResult(vm.recentProductListLiveData) {
            onEmmit = {
                productAdapter.notifyDataSet(this)
                tvRecommendedHome.animateVisibleOrGoneIfNot(!productAdapter.isEmpty())
                rvProductsHome.animateVisibleOrGoneIfNot(!productAdapter.isEmpty())
            }
        }
    }

    private fun initChatList() = with(rvChatListHome) {
        adapter = chatAdapter
        setHasFixedSize(true)
        layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    private fun initProductList() = with(rvProductsHome) {
        adapter = productAdapter
        setHasFixedSize(true)
    }
}