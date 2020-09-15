package com.pharmacy.manager.core.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pharmacy.manager.R
import com.pharmacy.manager.core.extensions.asyncWithContext
import com.pharmacy.manager.core.extensions.hideKeyboard
import com.pharmacy.manager.core.extensions.isKeyboardOpen
import com.pharmacy.manager.core.extensions.setMenu
import com.pharmacy.manager.core.general.behavior.IBehavior
import com.pharmacy.manager.core.general.interfaces.MessagesCallback
import com.pharmacy.manager.core.general.interfaces.ProgressCallback

abstract class BaseFragment(@LayoutRes layoutResourceId: Int) : Fragment(layoutResourceId) {

    protected val label by lazy { navController.currentDestination?.label?.toString() }

    protected var progressCallback: ProgressCallback? = null
        private set

    protected var messageCallback: MessagesCallback? = null
        private set

    private val behaviors = mutableListOf<IBehavior>()

    protected val navController by lazy { findNavController() }

    protected var toolbar: Toolbar? = null
        private set(value) {
            field = value
            initToolbar()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar = view.findViewById(R.id.toolbar)
        super.onViewCreated(view, savedInstanceState)
    }

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProgressCallback) {
            progressCallback = context
        }
        if (context is MessagesCallback) {
            messageCallback = context
        }
    }

    @CallSuper
    override fun onDetach() {
        progressCallback = null
        super.onDetach()
    }

    @CallSuper
    override fun onDestroy() {
        behaviors.forEach { it.detach() }
        behaviors.clear()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    protected fun <T : IBehavior> attachBehavior(behavior: T) = behavior.also {
        behaviors.add(it)
    }

    protected fun attachBackPressCallback(
        enabled: Boolean = true,
        action: OnBackPressedCallback.() -> Unit
    ) {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(enabled) {
                override fun handleOnBackPressed() {
                    action()
                }
            })
    }

    private fun initToolbar() {
        toolbar?.title = label
        toolbar?.setNavigationOnClickListener { navigationBack() }
    }

    protected fun initMenu(@MenuRes menu: Int, itemClick: Toolbar.OnMenuItemClickListener? = null) =
        toolbar?.setMenu(
            menu,
            itemClick ?: Toolbar.OnMenuItemClickListener { onOptionsItemSelected(it) })

    open fun navigationBack() {
        if (isKeyboardOpen) {
            hideKeyboard()
        } else {
            navController.popBackStack()
        }
    }

    protected fun showBackButton(
        @DrawableRes drawable: Int = R.drawable.ic_arrow_back,
        navigation: ((View) -> Unit)? = null
    ) {
        changeNavigationIcon(drawable)
        navigation?.let { toolbar?.setNavigationOnClickListener(it::invoke) }
    }

    protected fun changeNavigationIcon(@DrawableRes drawable: Int) {
        asyncWithContext(
            { ContextCompat.getDrawable(requireContext(), drawable) },
            { toolbar?.navigationIcon = this })
    }
}