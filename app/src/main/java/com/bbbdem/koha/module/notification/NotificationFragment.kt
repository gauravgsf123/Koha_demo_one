package com.bbbdem.koha.module.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentNotificationBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.notification.model.NotificationRequestModel
import com.bbbdem.koha.module.notification.model.NotificationResponseModel
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog


class NotificationFragment : BaseFragment() {
    private lateinit var binding:FragmentNotificationBinding
    private lateinit var viewModel:NotificationViewModel
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
            R.layout.fragment_notification,
            container,
            false
        )
        (activity as DashboardActivity?)?.run {
            showToolbar(true)
            setAppTitle(getString(R.string.notifications))
        }
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(requireActivity().application)
        )[NotificationViewModel::class.java]
        viewModel.getNotification(NotificationRequestModel(sharedPreference.getValueInt(Constant.PATRON_ID).toString(),Constant.ID_LIBRARY))
        setObserver()
        return binding.root
    }

    private fun setObserver(){
        viewModel.notificationRequestModel.observe(requireActivity()) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    response.data?.let { setupRecylerView(it) }
                }
                is Resource.Loading -> {
                    ProgressDialog.showProgressBar(requireContext())
                }
                is Resource.Error -> {
                    ProgressDialog.hideProgressBar()
                    response.message?.let { showToast(it) }
                }
                else -> {
                    ProgressDialog.hideProgressBar()
                    response.message?.let { showToast(it) }
                }
            }
        }
    }

    private fun setupRecylerView(data:List<NotificationResponseModel.Data>){
        binding.rvNotification.adapter = NotificationAdapter(data as ArrayList<NotificationResponseModel.Data>)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            NotificationFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}