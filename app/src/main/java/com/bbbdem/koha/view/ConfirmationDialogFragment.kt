package com.bbbdem.koha.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.bbbdem.koha.R
import com.bbbdem.koha.databinding.FragmentConfirmationDialogBinding

class ConfirmationDialogFragment(val mListener: ConfirmationDialogFragmentListener) : DialogFragment() {
    private lateinit var binding: FragmentConfirmationDialogBinding

    companion object {
        fun newInstance(title: String, mListener: ConfirmationDialogFragmentListener): ConfirmationDialogFragment {
            val args = bundleOf(
                Pair(TITLE, title)
            )
            val fragment = ConfirmationDialogFragment(mListener)
            fragment.arguments = args
            return fragment
        }
        private const val TITLE = "title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.AppTheme_Dialog_Custom)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_confirmation_dialog,
            container,
            false
        )

        dialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setCanceledOnTouchOutside(false)
        }
        init()
        return binding.root
    }

    private fun init() {
        binding.btnNo.setOnClickListener {
            dismiss()
            mListener.onConfirmationCancelClick()
        }
        binding.btnYes.setOnClickListener {
            dismiss()
            mListener.onConfirmationOKClick()
        }

        arguments?.let {
            val title = it.getString(TITLE)
            binding.tvTitle.text = title
        }
    }

    interface ConfirmationDialogFragmentListener {
        fun onConfirmationCancelClick()
        fun onConfirmationOKClick()
    }
}