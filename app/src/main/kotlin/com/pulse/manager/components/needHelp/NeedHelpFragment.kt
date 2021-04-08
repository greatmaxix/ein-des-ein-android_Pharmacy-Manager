package com.pulse.manager.components.needHelp

import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.manager.R
import com.pulse.manager.components.needHelp.adapter.HelpAdapter
import com.pulse.manager.core.base.fragment.BaseToolbarFragment
import com.pulse.manager.core.extensions.*
import com.pulse.manager.data.DummyData
import com.pulse.manager.databinding.FragmentNeedHelpBinding
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NeedHelpFragment : BaseToolbarFragment<NeedHelpViewModel>(R.layout.fragment_need_help, NeedHelpViewModel::class) {

    // TODO change items

    private val helpAdapter by lazy { HelpAdapter() }
    private val binding by viewBinding(FragmentNeedHelpBinding::bind)

    override fun initUI() = with(binding) {
        showBackButton()
        attachBackPressCallback { clickBack() }

        ivSearch.setDebounceOnClickListener {
            llHeaderContainer.gone()
            viewSearch.animateVisibleIfNot()
            viewSearch.requestFocus()
            toolbar.toolbar.title = getString(R.string.have_questions)
        }
        viewSearch.setSearchListener { text ->
            viewLifecycleOwner.lifecycleScope.launch {
                helpAdapter.filter { it.title.contains(text, true).falseIfNull() || it.text.contains(text, true).falseIfNull() }
            }
        }
        viewSearch.onBackClick = {
            requireActivity().onBackPressed()
        }

        initHelpList()
    }

    override fun onClickNavigation() {
        clickBack()
    }

    private fun clickBack() = with(binding) {
        if (viewSearch.isVisible) {
            hideKeyboard()
            viewSearch.gone()
            llHeaderContainer.animateVisibleIfNot()
            toolbar.toolbar.title = null
        } else {
            navController.popBackStack()
        }
    }

    private fun initHelpList() = with(binding.rvItems) {
        adapter = helpAdapter
        setHasFixedSize(true)

        helpAdapter.notifyDataSet(DummyData.help.onEach { it.isExpanded = false }) // TODO set proper list
    }
}