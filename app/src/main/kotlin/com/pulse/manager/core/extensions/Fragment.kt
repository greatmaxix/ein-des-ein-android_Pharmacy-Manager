package com.pulse.manager.core.extensions

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.DimenRes
import androidx.annotation.FontRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pulse.manager.BuildConfig
import com.pulse.manager.core.base.fragment.dialog.AlertDialogData
import com.pulse.manager.core.base.fragment.dialog.AlertDialogDataRes
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ViewModelParameter
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

fun <T : View> Fragment.findViewById(@IdRes id: Int) = view?.findViewById<T>(id)

fun Fragment.getFragmentTag(suffix: String? = null): String =
    this::class.java.simpleName + (suffix ?: "")

inline fun <F : Fragment> F.putArgs(argsBuilder: Bundle.() -> Unit): F =
    apply { arguments = Bundle().apply(argsBuilder) }

fun <F : Fragment> F.getIntArg(key: String, defValue: Int) =
    arguments?.getInt(key, defValue) ?: defValue

fun <F : Fragment> F.getStringArg(key: String, defValue: String? = null) =
    arguments?.getString(key, defValue) ?: defValue

fun <T> Fragment.asyncViewScope(async: () -> T, result: T.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        result.invoke(withContext(Default) { async.invoke() })
    }
}

val Fragment.windowManager: WindowManager
    get() = requireActivity().windowManager

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

inline fun <reified VM : ViewModel> Fragment.sharedGraphViewModel(
    @IdRes navGraphId: Int,
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
) = lazy {
    val store = findNavController().getViewModelStoreOwner(navGraphId).viewModelStore
    getKoin().getViewModel(ViewModelParameter(VM::class, qualifier, parameters, Bundle(), store, null))
}

val Fragment.isKeyboardOpen
    get() = requireActivity().isKeyboardOpen

fun <T> Fragment.asyncWithContext(async: () -> T, result: T.() -> Unit) =
    viewLifecycleOwner.lifecycleScope.launch {
        result.invoke(withContext(Default) { async.invoke() })
    }

inline fun Fragment.debug(code: () -> Unit) {
    if (BuildConfig.DEBUG) {
        code()
    }
}

fun Fragment.alert(message: String, block: AlertDialogData.() -> Unit) = requireActivity().alert(message, block)

fun Fragment.showAlert(message: String, block: AlertDialogData.() -> Unit) = requireActivity().showAlert(message, block)

fun Fragment.showAlert(@StringRes resId: Int, block: AlertDialogData.() -> Unit) = requireActivity().showAlert(getString(resId), block)

fun Fragment.showAlertRes(@StringRes resId: Int, block: AlertDialogDataRes.() -> Unit) =
    requireActivity().showAlertRes(getString(resId), block)

fun Fragment.showAlertRes(message: String, block: AlertDialogDataRes.() -> Unit) = requireActivity().showAlertRes(message, block)

fun Fragment.doWithDelay(delay: Long, action: () -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        delay(delay)
        action()
    }
}

fun Fragment.doWithDelay(delay: Int, action: () -> Unit) = doWithDelay(delay.toLong(), action)

fun Fragment.dimension(@DimenRes dimenRes: Int) = resources.getDimension(dimenRes)

fun Fragment.dimensionPixelSize(@DimenRes dimenRes: Int) = resources.getDimensionPixelSize(dimenRes)

fun Fragment.font(@FontRes fontId: Int) = resources.getFont(fontId)