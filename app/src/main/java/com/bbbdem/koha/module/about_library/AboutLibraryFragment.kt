package com.bbbdem.koha.module.about_library

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentAboutLibraryBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity


class AboutLibraryFragment : BaseFragment() {
    private lateinit var binding: FragmentAboutLibraryBinding


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
            R.layout.fragment_about_library,
            container,
            false
        )
        (activity as DashboardActivity?)?.run {
            showToolbar(true)
            setAppTitle(resources.getString(R.string.about_library))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvAboutLibrary.text =
                HtmlCompat.fromHtml(sharedPreference.getValueString(Constant.ABOUT_US)!!, HtmlCompat.FROM_HTML_MODE_COMPACT);
        } else {
            binding.tvAboutLibrary.text = HtmlCompat.fromHtml(sharedPreference.getValueString(Constant.ABOUT_US)!!,HtmlCompat.FROM_HTML_MODE_COMPACT);
        }
        return binding.root
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AboutLibraryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}