package com.pharmacy.manager.components.chatList

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.pharmacy.manager.R
import com.pharmacy.manager.components.chatList.ChatListFragmentDirections.Companion.fromChatListToChat
import com.pharmacy.manager.components.chatList.adapter.ChatAdapter
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.manager.core.extensions.addStateListener
import com.pharmacy.manager.core.extensions.animateGoneIfNot
import com.pharmacy.manager.core.extensions.animateVisibleIfNot
import kotlinx.android.synthetic.main.fragment_chat_list.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ChatListFragment(private val vm: ChatListViewModel) : BaseMVVMFragment(R.layout.fragment_chat_list) {

    private val chatAdapter by lazy {
        ChatAdapter {
            searchViewChatList.clearFocus()
            navController.navigate(fromChatListToChat(it))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        initMenu(R.menu.search) {
            it.isVisible = false
            searchViewChatList.animateVisibleIfNot()
            searchViewChatList.requestFocus()
            true
        }
        initChatList()

        attachBackPressCallback {
            if (searchViewChatList.isVisible) {
                searchViewChatList.animateGoneIfNot()
                toolbar?.menu?.findItem(R.id.search)?.isVisible = true
            } else {
                navController.popBackStack()
            }
        }

        searchViewChatList.setSearchListener { text ->
            viewLifecycleOwner.lifecycleScope.launch {
                // TODO filter
//                chatAdapter.filter {
//                    it.name.contains(text, true).falseIfNull() || it.lastMessage.contains(text, true).falseIfNull()
//                }
            }
        }

        chatAdapter.addStateListener { progressCallback?.setInProgress(it) }
        searchViewChatList.onBackClick = {
            requireActivity().onBackPressed()
        }
    }

    @ExperimentalPagingApi
    override fun onBindLiveData() {
        super.onBindLiveData()

        observe(vm.chatListLiveData) { chatAdapter.submitData(lifecycle, this) }
    }

    private fun initChatList() {
        rvChatList.adapter = chatAdapter
        rvChatList.setHasFixedSize(true)
    }
}
