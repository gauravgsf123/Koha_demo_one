package com.bbbdem.koha.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.bbbdem.koha.R
import com.bbbdem.koha.databinding.SearchViewLayoutBinding
import com.bbbdem.koha.utils.TrackedConstraintLayout


class SearchMoreView : TrackedConstraintLayout {
    lateinit var binding: SearchViewLayoutBinding
    public var searchByValue: String = ""
    var searchWithValue: String = ""
    var andOr: String = "and"

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context?) {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.search_view_layout, this, true)
        setupSearchBy()
        setupSearchWith()
        binding.rgType.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                binding.rbAnd.id->andOr = "and"
                binding.rbOr.id->andOr = "or"
            }
        }
    }

    private fun setupSearchBy() {
        var searchByList = resources.getStringArray(R.array.search_by)
        var arrayAdapter =
            ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, searchByList)
        binding.spinnerAuthor.adapter = arrayAdapter
        binding.spinnerAuthor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //if(position>0)
                searchByValue = searchByList[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun setupSearchWith() {
        var searchByList = resources.getStringArray(R.array.search_with)
        var arrayAdapter =
            ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, searchByList)
        binding.spinnerContainer.adapter = arrayAdapter
        binding.spinnerContainer.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    //if(position>0)
                    searchWithValue = searchByList[position].toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    public fun getSearchWith():String{
        return searchWithValue
    }
}