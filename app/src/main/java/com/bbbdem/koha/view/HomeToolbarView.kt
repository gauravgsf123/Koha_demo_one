package com.bbbdem.koha.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bbbdem.koha.R
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.common.SharedPreference
import com.bbbdem.koha.databinding.HomeToolbarViewBinding
import com.bbbdem.koha.utils.TrackedConstraintLayout


class HomeToolbarView : TrackedConstraintLayout, View.OnClickListener {
    lateinit var binding: HomeToolbarViewBinding

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.home_toolbar_view, this, true)
        binding.imageViewClear.setOnClickListener(this)
        //binding.ivNotification.setOnClickListener(this)
        //binding.tvCount.setOnClickListener(this)
        val sharedPreference = SharedPreference(context)
        binding.tvTitle.text = sharedPreference.getValueString(Constant.LIBRARY_NAME)

    }

    override fun onClick(p0: View?) {
        binding.run {
            when(p0?.id){
                imageViewClear.id->binding.editTextSearch.setText("")
                /*ivNotification.id, tvCount.id->{

                }*/

            }
        }
    }
}