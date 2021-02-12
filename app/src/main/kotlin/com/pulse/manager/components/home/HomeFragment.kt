package com.pulse.manager.components.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.home.HomeFragmentDirections.Companion.fromHomeToChat
import com.pulse.manager.components.home.HomeFragmentDirections.Companion.fromHomeToScanner
import com.pulse.manager.components.home.HomeFragmentDirections.Companion.fromHomeToSearch
import com.pulse.manager.components.home.HomeFragmentDirections.Companion.globalToProductCard
import com.pulse.manager.components.home.adapter.ChatListAdapter
import com.pulse.manager.components.home.adapter.ProductAdapter
import com.pulse.manager.components.mercureService.MercureEventListenerService
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import com.pulse.manager.core.extensions.*
import com.pulse.manager.databinding.FragmentHomeBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class HomeFragment(private val viewModel: HomeViewModel) : BaseMVVMFragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private val customerAvatars by lazy {
        listOf(
            binding.ivAvatar4,
            binding.ivAvatar3,
            binding.ivAvatar2,
            binding.ivAvatar1
        )
    }
    private val chatAdapter by lazy { ChatListAdapter { navController.navigate(fromHomeToChat(it)) } }
    private val productAdapter by lazy {
        ProductAdapter {
            observeResult(viewModel.requestProductInfo(it.globalProductId)) {
                onEmmit = { navController.navigate(globalToProductCard(this)) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        mcvScan.setDebounceOnClickListener { navController.navigate(fromHomeToScanner()) }
        mcvSearch.setDebounceOnClickListener { navController.navigate(fromHomeToSearch()) }
        mtvSoonCounter.text = "∞"

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
    override fun onBindLiveData() = with(binding) {
        observeResult(viewModel.openedChats) {
            onEmmit = {
                val openedChatsCount = totalCount
                mtvChatRequestsCounter.text = openedChatsCount.toString()
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
                rvChatList.animateVisibleOrGoneIfNot(!chatAdapter.isEmpty())
            }
        }
        observeResult(viewModel.recentProductListLiveData) {
            onEmmit = {
                productAdapter.notifyDataSet(this)
                mtvRecommended.animateVisibleOrGoneIfNot(!productAdapter.isEmpty())
                rvProducts.animateVisibleOrGoneIfNot(!productAdapter.isEmpty())
            }
        }
    }

    private fun initChatList() = with(binding.rvChatList) {
        adapter = chatAdapter
        setHasFixedSize(true)
        layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    private fun initProductList() = with(binding.rvProducts) {
        adapter = productAdapter
        setHasFixedSize(true)
    }
}