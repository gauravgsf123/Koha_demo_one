package com.bbbdem.koha.module.payment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.bbbdem.koha.R
import com.bbbdem.koha.app.BaseFragment
import com.bbbdem.koha.databinding.FragmentMobilPayBinding
import com.bbbdem.koha.module.dashboard.DashboardActivity
import com.bbbdem.koha.module.my_account.charges.ChargesFragment


class MobilPayFragment(var url: String, val callBack: ChargesFragment.CallBackInterface) : BaseFragment() {

    private lateinit var binding:FragmentMobilPayBinding
    private var isDone = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_mobil_pay,
            container,
            false
        )
        binding.run {
            webView.webViewClient = WebViewClient()

            // this will load the url of the website


            // this will enable the javascript settings, it can also allow xss vulnerabilities
            webView.settings.loadWithOverviewMode = true
            webView.settings.useWideViewPort = true
            webView.settings.javaScriptEnabled = true
            webView.settings.allowFileAccess = true
            webView.settings.allowContentAccess = true
            webView.isScrollbarFadingEnabled = false
            webView.settings.domStorageEnabled = true

            // if you want to enable zoom feature
            webView.settings.setSupportZoom(true)

            webView.webViewClient = object :WebViewClient(){
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null) {
                        view?.loadUrl(url)
                    };
                    return true
                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    if(url?.contains("receipt.php")==true && !isDone){
                        isDone = true
                        showConfirmation(url)
                    }
                }

                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler,
                    error: SslError?
                ) {
                    handler.proceed()
                    Log.d("WebView", "error $error")
                    showToast(error.toString())
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage(getString(R.string.notification_error_ssl_cert_invalid))
                    builder.setPositiveButton(
                        "continue"
                    ) { dialog, which -> handler.proceed() }
                    builder.setNegativeButton(
                        "cancel"
                    ) { dialog, which -> handler.cancel() }
                    val dialog = builder.create()
                    dialog.show()
                }
            }

            webView.loadUrl(url)
        }
        return binding.root
    }

    private fun showConfirmation(data:String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Your payment is successful")
            .setCancelable(false)
            .setPositiveButton("done") { dialog, id ->
                dialog.dismiss()
                val uri = Uri.parse(data)
                val id = uri.getQueryParameter("id")
                callBack.onPaymentDone(id!!)
                (activity as DashboardActivity).onBackPressed()

            }
        val alert = builder.create()
        alert.show()
    }

}