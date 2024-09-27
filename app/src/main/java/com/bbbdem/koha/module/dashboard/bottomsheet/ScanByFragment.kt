package com.bbbdem.koha.module.dashboard.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseBottomSheetDialogFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentScanByBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.dashboard.fragment.SearchListFragment
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class ScanByFragment : BaseBottomSheetDialogFragment() {
    private lateinit var binding:FragmentScanByBinding
    private var scanBy = "barcode"

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
            R.layout.fragment_scan_by,
            container,
            false
        )
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.rbBarcode.setOnClickListener(rbBarcodeListener)
        binding.rbIsbnIssn.setOnClickListener(rbIsbnIssnListener)
        binding.btnScan.setOnClickListener {
            val options = ScanOptions()
            options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
            options.setPrompt("Scan a barcode")
            options.setCameraId(0) // Use a specific camera of the device
            options.setBeepEnabled(false)
            options.setBarcodeImageEnabled(true)
            barcodeLauncher.launch(options)
        }
        return binding.root
    }

    var rbBarcodeListener: View.OnClickListener = View.OnClickListener {
        scanBy = "barcode"
        binding.rbBarcode.isChecked = true
        binding.rbIsbnIssn.isChecked = false
    }
    var rbIsbnIssnListener: View.OnClickListener = View.OnClickListener {
        scanBy = "isbn"
        binding.rbBarcode.isChecked = false
        binding.rbIsbnIssn.isChecked = true
    }

    private val barcodeLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            dismiss()
            Toast.makeText(
                requireContext(),
                "Scanned: " + result.contents,
                Toast.LENGTH_LONG
            ).show()

            val bundle = Bundle()
            var query = ""
            if(scanBy == "barcode"){
                query = "{\"external_id\": { \"-like\": \"${result.contents}%\" }}"
            }else{
                query = "{ \"-or\": [ { \"isbn\": { \"-like\": \"${result.contents}%\" } }, { \"issn\": { \"-like\": \"${result.contents}%\" } } ] }"
            }
            bundle.putString(Constant.LIBRARY, "")
            bundle.putString(Constant.CATEGORY, "")
            bundle.putString(Constant.SEARCH_BY,scanBy)
            bundle.putString(Constant.SEARCH, query)
            bundle.putInt(Constant.FRAGMENT_TYPE, Constant.FragmentType.BARCODE_SCAN)
            val fragment = SearchListFragment.newInstance()
            fragment.arguments = bundle

            (activity as DashboardActivity).replaceFragment(fragment)
        }
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScanByFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}