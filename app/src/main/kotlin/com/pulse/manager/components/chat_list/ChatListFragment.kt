package com.pulse.manager.components.chat_list

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.chat_list.ChatListFragmentDirections.Companion.fromChatListToChat
import com.pulse.manager.components.chat_list.adapter.ChatListPagingAdapter
import com.pulse.manager.core.base.fragment.BaseToolbarFragment
import com.pulse.manager.core.extensions.addStateListener
import com.pulse.manager.core.extensions.animateGoneIfNot
import com.pulse.manager.core.extensions.animateVisibleIfNot
import com.pulse.manager.core.extensions.observe
import com.pulse.manager.databinding.FragmentChatListBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ChatListFragment : BaseToolbarFragment<ChatListViewModel>(R.layout.fragment_chat_list, ChatListViewModel::class, R.menu.search) {

    private val binding by viewBinding(FragmentChatListBinding::bind)

    private val chatAdapter by lazy {
        ChatListPagingAdapter {
            binding.viewSearch.clearFocus()
            navController.navigate(fromChatListToChat(it))
        }
    }

    override fun initUI() = with(binding) {
        showBackButton()
        initChatList()

        attachBackPressCallback {
            if (viewSearch.isVisible) {
                viewSearch.animateGoneIfNot()
                toolbar.toolbar.menu?.findItem(R.id.search)?.isVisible = true
            } else {
                navController.popBackStack()
            }
        }

        viewSearch.setSearchListener { text -> viewModel.searchChat(text.toString()) }

        chatAdapter.addStateListener { uiHelper.showLoading(it) }
        viewSearch.onBackClick = {
            requireActivity().onBackPressed()
        }
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(menuItemsFlow) {
            it.isVisible = false
            binding.viewSearch.animateVisibleIfNot()
            binding.viewSearch.requestFocus()
        }
    }

    @ExperimentalPagingApi
    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.chatListFlow) { chatAdapter.submitData(lifecycle, it) }
    }

    private fun initChatList() = with(binding.rvChatList) {
        adapter = chatAdapter
        setHasFixedSize(true)
    }
}
