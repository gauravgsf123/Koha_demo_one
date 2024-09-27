package com.bbbdem.koha.module.my_account.my_qr_code

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.FragmentQrCodeBinding
import com.bbbdem.koha.login.model.UserDetailResponseModel
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder


class QRCodeFragment : BaseFragment() {
   private lateinit var binding:FragmentQrCodeBinding
    private lateinit var userDetail: UserDetailResponseModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_qr_code,
            container,
            false
        )
        (activity as DashboardActivity?)?.run {
            showToolbar(true)
            setAppTitle(getString(R.string.qr_code))
        }
        userDetail = Gson().fromJson(sharedPreference.getValueString(Constant.USER_DETAIL), UserDetailResponseModel::class.java)
        qrcodeGenerate()
        return binding.root
    }

    private fun qrcodeGenerate() {
        val mWriter = MultiFormatWriter()
        try {
            //BitMatrix class to encode entered text and set Width & Height
            val mMatrix = mWriter.encode(userDetail.cardnumber, BarcodeFormat.QR_CODE, 400, 400)
            val mEncoder = BarcodeEncoder()
            val mBitmap = mEncoder.createBitmap(mMatrix) //creating bitmap of code
            binding.ivQRCode.setImageBitmap(mBitmap) //Setting generated QR code to imageView
            // to hide the keyboard
        } catch (e: WriterException) {
            e.printStackTrace()
        }

    }

}