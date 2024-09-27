package com.bbbdem.koha.module.my_account.personal_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.databinding.FragmentPersonalDetailMainBinding
import com.bbbdem.koha.library.fragments.ContactInfoFragment
import com.bbbdem.koha.library.fragments.MainAddressFragment
import com.bbbdem.koha.module.my_account.personal_detail.fragment.PersonalDetailsFragment
import com.bbbdem.koha.library.fragments.LibraryInfoFragment
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.google.android.material.tabs.TabLayoutMediator


class PersonalDetailMainFragment : BaseFragment() {

    private lateinit var binding :FragmentPersonalDetailMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_personal_detail_main,
            container,
            false
        )
        (activity as DashboardActivity?)?.run {
            showToolbar(true)
            setAppTitle(getString(R.string.your_personal_detail))
        }
        setTabListener()
        return binding.root
    }

    override fun onClick(p0: View?) {
    }

    private fun setTabListener() {
        var tabTitle = ArrayList<String>()
        tabTitle.add("Library Profile")
        tabTitle.add("Personal Details")
        tabTitle.add("Main Address")
        tabTitle.add("Contact info")

        val fragmentList = mutableListOf<Fragment>()
        fragmentList.add(LibraryInfoFragment(binding.viewpager))
        fragmentList.add(PersonalDetailsFragment(binding.viewpager))
        fragmentList.add(MainAddressFragment(binding.viewpager))
        fragmentList.add(ContactInfoFragment(binding.viewpager))



        binding.viewpager.adapter = LeaderBoardAdapter(parentFragmentManager, lifecycle, fragmentList)
        //binding.viewpager.isUserInputEnabled = false

        TabLayoutMediator(
            binding.tablayout,
            binding.viewpager
        ) { tab, position ->
            tab.text = tabTitle[position]

        }.attach()

    }

    fun setTab(position:Int){
        binding.tablayout.getTabAt(position)?.select()
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            PersonalDetailMainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}