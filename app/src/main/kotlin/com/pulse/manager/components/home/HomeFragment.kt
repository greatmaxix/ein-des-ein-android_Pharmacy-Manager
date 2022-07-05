package com.pulse.manager.components.home

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.home.HomeFragmentDirections.Companion.fromHomeToChat
import com.pulse.manager.components.home.HomeFragmentDirections.Companion.fromHomeToScanner
import com.pulse.manager.components.home.HomeFragmentDirections.Companion.fromHomeToSearch
import com.pulse.manager.components.home.adapter.ChatListAdapter
import com.pulse.manager.components.home.adapter.ProductAdapter
import com.pulse.manager.components.mercureService.MercureEventListenerService
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import com.pulse.manager.core.extensions.*
import com.pulse.manager.databinding.FragmentHomeBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class HomeFragment : BaseMVVMFragment<HomeViewModel>(R.layout.fragment_home, HomeViewModel::class) {

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
            viewModel.requestProductInfo(it.globalProductId)
        }
    }

    override fun initUI() = with(binding) {
        mcvScan.setDebounceOnClickListener { navController.navigate(fromHomeToScanner()) }
        mcvSearch.setDebounceOnClickListener { navController.navigate(fromHomeToSearch()) }
        mtvSoonCounter.text = "∞"

        initChatList()
        initProductList()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchLastRecommended()

        // TODO add manual mercure stop
        if (!requireContext().isServiceRunning(MercureEventListenerService::class.java)) {
            requireContext().startService(Intent(requireContext(), MercureEventListenerService::class.java))
        }
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.openedChats) {
            it?.let { model ->
                val openedChatsCount = model.totalCount
                binding.mtvChatRequestsCounter.text = openedChatsCount.toString()
                val customerImages = model.items.map { item -> item.customer.avatar?.url ?: "" }
                customerAvatars.forEachIndexed { index, imageView ->
                    val avatarUrl = customerImages.getOrNull(index)
                    avatarUrl?.let { url ->
                        imageView.loadCircularImage(
                            url,
                            resources.getDimensionPixelSize(R.dimen._2sdp).toFloat(),
                            requireContext().compatColor(R.color.colorGlobalWhite)
                        )
                        imageView.animateVisibleIfNot()
                    } ?: run { imageView.animateGoneIfNot() }
                }
                chatAdapter.setList(it.items.take(2))
                binding.rvChatList.animateVisibleOrGoneIfNot(!chatAdapter.isEmpty())
            }
        }
        observe(viewModel.recentProductListState) {
            productAdapter.notifyDataSet(it)
            binding.mtvRecommended.animateVisibleOrGoneIfNot(!productAdapter.isEmpty())
            binding.rvProducts.animateVisibleOrGoneIfNot(!productAdapter.isEmpty())
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