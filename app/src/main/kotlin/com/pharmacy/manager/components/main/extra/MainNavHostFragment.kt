package com.pharmacy.manager.components.main.extra

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import org.koin.android.ext.android.get

class MainNavHostFragment : NavHostFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.fragmentFactory = get()
        super.onCreate(savedInstanceState)
    }
}