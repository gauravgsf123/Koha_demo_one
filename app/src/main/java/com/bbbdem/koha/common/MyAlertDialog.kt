package com.bbbdem.koha.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.bbbdem.koha.R
import com.bbbdem.koha.databinding.AlertDialogMessageBinding


class MyAlertDialog(context: Context, var title: String, var text: String,var confirmText: String,var cancelText:String, var cb: ClickListener) : AlertDialog(context) {
    private var i: ClickListener? = null
    private lateinit var binding : AlertDialogMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate<AlertDialogMessageBinding>(LayoutInflater.from(context), R.layout.alert_dialog_message,null,false)
        this.i = cb
        binding.titleText.text = title
        binding.contentText.text = text
        binding.confirmButton.text = confirmText
        binding.cancelButton.text = cancelText
        binding.confirmButton.setOnClickListener {
            i?.let {
                i!!.onOk()
            }
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            i?.let {
                i!!.onCancel()
            }
            dismiss()
        }
    }

    interface ClickListener{
        fun onOk()
        fun onCancel()
    }
}