package com.bbbdem.koha.module.about_library

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentRulesAndRegulationBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity


class RulesAndRegulationFragment : BaseFragment() {
    private lateinit var binding:FragmentRulesAndRegulationBinding
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
            R.layout.fragment_rules_and_regulation,
            container,
            false
        )
        (activity as DashboardActivity?)?.run {
            showToolbar(true)
            setAppTitle(resources.getString(R.string.rules_and_regulation))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvRulesRegulation.text =
                HtmlCompat.fromHtml(sharedPreference.getValueString(Constant.LIBRARY_RULE)!!, HtmlCompat.FROM_HTML_MODE_COMPACT);
        } else {
            binding.tvRulesRegulation.text = HtmlCompat.fromHtml(sharedPreference.getValueString(Constant.LIBRARY_RULE)!!, HtmlCompat.FROM_HTML_MODE_COMPACT);
        }
        return binding.root
    }

}