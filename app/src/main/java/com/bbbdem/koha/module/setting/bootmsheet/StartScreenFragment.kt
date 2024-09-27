package com.bbbdem.koha.module.setting.bootmsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseBottomSheetDialogFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentStartScreenBinding

class StartScreenFragment : BaseBottomSheetDialogFragment() {
    private lateinit var binding:FragmentStartScreenBinding
    var startPage = Constant.StartScreen.LIBRARY_HOME


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
            R.layout.fragment_start_screen,
            container,
            false
        )
        if(sharedPreference.getStartPage(Constant.START_PAGE)==Constant.StartScreen.LIBRARY_HOME) {
            binding.rbLibraryHome.isChecked = true
        }else binding.rbUserSummary.isChecked = true

        binding.rbUserSummary.setOnClickListener(rbUserSummaryListener)
        binding.rbLibraryHome.setOnClickListener(rbLibraryHomeListener)
        binding.btnApply.setOnClickListener {
            sharedPreference.save(Constant.START_PAGE,startPage)
            dismiss()
        }
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    var rbUserSummaryListener: View.OnClickListener = View.OnClickListener {
        startPage = Constant.StartScreen.USER_SUMMARY
        binding.rbUserSummary.isChecked = true
        binding.rbLibraryHome.isChecked = false
    }
    var rbLibraryHomeListener: View.OnClickListener = View.OnClickListener {
        startPage = Constant.StartScreen.LIBRARY_HOME
        binding.rbUserSummary.isChecked = false
        binding.rbLibraryHome.isChecked = true
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StartScreenFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}