package com.arzaapps.android.yamobile.views

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.arzaapps.android.yamobile.views.actionslistscreen.view.ActionsListFragment
import com.arzaapps.android.yamobile.views.favouritescreen.view.FavouriteFragment

class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            MAIN_FRAGMENT_POSITION -> ActionsListFragment()
            FAVOURITE_FRAGMENT_POSITION -> FavouriteFragment()
            else -> throw IllegalStateException("Ошибка позиции $position")
        }
    }

    fun getTitle(position: Int): String = when (position) {
        MAIN_FRAGMENT_POSITION -> "Stocks"
        FAVOURITE_FRAGMENT_POSITION -> "Favourite"
        else -> throw IllegalStateException("Ошибка позиции $position")
    }

    companion object {
        private const val MAIN_FRAGMENT_POSITION = 0
        private const val FAVOURITE_FRAGMENT_POSITION = 1
    }

}