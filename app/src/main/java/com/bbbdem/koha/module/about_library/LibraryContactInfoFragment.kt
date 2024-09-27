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
import com.bbbdem.koha.databinding.FragmentLibraryContactInfoBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class LibraryContactInfoFragment : BaseFragment() {
    private lateinit var binding:FragmentLibraryContactInfoBinding

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
            R.layout.fragment_library_contact_info,
            container,
            false
        )
        (activity as DashboardActivity?)?.run {
            showToolbar(true)
            setAppTitle(resources.getString(R.string.about_library))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvContactUs.text =
                HtmlCompat.fromHtml(sharedPreference.getValueString(Constant.CONTACT_US)!!, HtmlCompat.FROM_HTML_MODE_COMPACT);
        } else {
            binding.tvContactUs.text = HtmlCompat.fromHtml(sharedPreference.getValueString(Constant.CONTACT_US)!!,
                HtmlCompat.FROM_HTML_MODE_COMPACT);
        }
        showMap()
        /*binding.tvEmail.text = sharedPreference.getValueString(Constant.LIBRARY_EMAIL)
        binding.tvContactInfo.text = sharedPreference.getValueString(Constant.LIBRARY_PHONE)
        binding.tvWebsiteLink.text = sharedPreference.getValueString(Constant.LIBRARY_LINK)*/
        return binding.root
    }

    private fun showMap() {//28.533283524314626, 77.21791881349253
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?


        // Async map
        supportMapFragment!!.getMapAsync { googleMap ->
            val latLng = LatLng(sharedPreference.getValueString(Constant.LATITUDE)?.toDouble()!!, sharedPreference.getValueString(Constant.LONGITUDE)?.toDouble()!!)
            val marker = MarkerOptions().position(latLng).title(sharedPreference.getValueString(Constant.LIBRARY_NAME_GLOBAL))
            googleMap.addMarker(marker)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F));
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15F), 200, null)
            googleMap.uiSettings.isZoomControlsEnabled = true
            googleMap.uiSettings.isMapToolbarEnabled = true
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LibraryContactInfoFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}