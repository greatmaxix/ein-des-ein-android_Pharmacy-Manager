package com.pharmacy.manager.components.needHelp

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.pharmacy.manager.R
import com.pharmacy.manager.components.needHelp.adapter.HelpAdapter
import com.pharmacy.manager.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.manager.core.extensions.animateGoneIfNot
import com.pharmacy.manager.core.extensions.animateVisibleIfNot
import com.pharmacy.manager.core.extensions.falseIfNull
import com.pharmacy.manager.data.DummyData
import kotlinx.android.synthetic.main.fragment_need_help.*
import kotlinx.coroutines.launch

class NeedHelpFragment(private val vm: NeedHelpViewModel) : BaseMVVMFragment(R.layout.fragment_need_help) {

    private val helpAdapter by lazy { HelpAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()

        initMenu(R.menu.search) {
            it.isVisible = false
            searchViewNeedHelp.animateVisibleIfNot()
            searchViewNeedHelp.requestFocus()
            true
        }

        attachBackPressCallback {
            if (searchViewNeedHelp.isVisible) {
                searchViewNeedHelp.animateGoneIfNot()
                toolbar?.menu?.findItem(R.id.search)?.isVisible = true
            } else {
                navController.popBackStack()
            }
        }

        searchViewNeedHelp.setSearchListener { text ->
            viewLifecycleOwner.lifecycleScope.launch {
                helpAdapter.filter { it.title.contains(text, true).falseIfNull() || it.text.contains(text, true).falseIfNull() }
            }
        }

        initHelpList()
    }

    private fun initHelpList() {
        rvItemsNeedHelp.adapter = helpAdapter
        rvItemsNeedHelp.setHasFixedSize(true)

        helpAdapter.notifyDataSet(DummyData.help.apply { forEach { it.isExpanded = false } }) // TODO set proper list
    }
}