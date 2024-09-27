package com.bbbdem.koha.module.my_account.personal_detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class LeaderBoardAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    var fragmentList: MutableList<Fragment>
) :

    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return fragmentList[0]
            1 -> return fragmentList[1]
            2 -> return fragmentList[2]
            3 -> return fragmentList[3]
            else -> return Fragment()
        }
    }
}