package com.bbbdem.koha.module.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentSettingBinding
import com.bbbdem.koha.login.LoginActivity
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.my_account.personal_detail.PersonalDetailMainFragment
import com.bbbdem.koha.module.setting.bootmsheet.StartScreenFragment
import com.bbbdem.koha.module.setting.bootmsheet.WarningPeriodFragment
import com.bbbdem.koha.view.ConfirmationDialogFragment


class SettingFragment : BaseFragment() {
    private lateinit var binding:FragmentSettingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_setting,
            container,
            false
        )
        binding.tvStartScreen.setOnClickListener(this)
        binding.tvStartScreenDetail.setOnClickListener(this)
        binding.tvWarningPeriodTitle.setOnClickListener(this)
        binding.tvWarningPeriodDetail.setOnClickListener(this)
        binding.tvAccountTitle.setOnClickListener(this)
        binding.tvAccountDetail.setOnClickListener(this)
        (activity as DashboardActivity).run {
            showToolbar(true)
            setAppTitle(resources.getString(R.string.settings))
        }
        return binding.root
    }

    override fun onClick(p0: View?) {
        super.onClick(p0)
        when(p0?.id){
            binding.tvStartScreen.id->{}
            binding.tvStartScreenDetail.id->{
                StartScreenFragment().show(
                    requireFragmentManager(),
                    "StartScreenFragment"
                )
            }
            binding.tvWarningPeriodTitle.id->{}
            binding.tvWarningPeriodDetail.id->{
                WarningPeriodFragment().show(
                    requireFragmentManager(),
                    "WarningPeriodFragment"
                )
            }

            binding.tvAccountTitle.id->{}
            binding.tvAccountDetail.id->{
                loginUser(PersonalDetailMainFragment())
            }
        }
    }

    private fun loginUser(fragment: Fragment) {
        if (sharedPreference.getValueInt(Constant.PATRON_ID)!=0) {
            (activity as DashboardActivity).replaceFragment(fragment)
        }else{
            ConfirmationDialogFragment.newInstance(
                resources.getString(R.string.do_you_want_login),
                object : ConfirmationDialogFragment.ConfirmationDialogFragmentListener {
                    override fun onConfirmationCancelClick() {}
                    override fun onConfirmationOKClick() {
                        (activity as DashboardActivity).startNewActivity(LoginActivity())
                        (activity as DashboardActivity).finish()
                    }

                }
            ).show(parentFragmentManager, "logout")
        }
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}