package com.pulse.manager.components.chat_list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.paging.ExperimentalPagingApi
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.chat_list.ChatListFragmentDirections.Companion.fromChatListToChat
import com.pulse.manager.components.chat_list.adapter.ChatListPagingAdapter
import com.pulse.manager.core.base.mvvm.BaseMVVMFragment
import com.pulse.manager.core.extensions.addStateListener
import com.pulse.manager.core.extensions.animateGoneIfNot
import com.pulse.manager.core.extensions.animateVisibleIfNot
import com.pulse.manager.databinding.FragmentChatListBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ChatListFragment(private val viewModel: ChatListViewModel) : BaseMVVMFragment(R.layout.fragment_chat_list) {

    private val binding by viewBinding(FragmentChatListBinding::bind)

    private val chatAdapter by lazy {
        ChatListPagingAdapter {
            binding.viewSearch.clearFocus()
            navController.navigate(fromChatListToChat(it))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        initMenu(R.menu.search) {
            it.isVisible = false
            viewSearch.animateVisibleIfNot()
            viewSearch.requestFocus()
            true
        }
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

        chatAdapter.addStateListener { progressCallback?.setInProgress(it) }
        viewSearch.onBackClick = {
            requireActivity().onBackPressed()
        }
    }

    @ExperimentalPagingApi
    override fun onBindLiveData() {
        super.onBindLiveData()

        observe(viewModel.chatListLiveData) { chatAdapter.submitData(lifecycle, this) }
    }

    private fun initChatList() = with(binding.rvChatList) {
        adapter = chatAdapter
        setHasFixedSize(true)
    }
}
