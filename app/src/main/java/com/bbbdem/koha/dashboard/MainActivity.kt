package com.bbbdem.koha.dashboard

import android.Manifest
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.MVVMBindingActivity
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.common.ManagePermissions
import com.bbbdem.koha.databinding.ActivityMainBinding
import com.bbbdem.koha.network.ViewModelFactoryClass


class MainActivity : MVVMBindingActivity<ActivityMainBinding>(),OnClickListener {
    private lateinit var viewModel: MainViewModel
    private var isCamera = false
    private lateinit var managePermissions: ManagePermissions
    private val REQUEST_CAMERA_CAPTURE = 1002
    private val permissionList = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var cNoteList= mutableListOf<TripSheetResponse.TripSheetDocketDetail>()
    private var cNote : TripSheetResponse.TripSheetDocketDetail?=null

    override fun initializeView() {
        managePermissions = ManagePermissions(this, permissionList, Constant.REQUEST_PERMISION)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(application)
        )[MainViewModel::class.java]
        binding?.let{

        }
        setObserver()
    }

    override fun provideViewResource(): Int {
        return R.layout.activity_main
    }

    private fun setObserver() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == R.id.menu_new_bluetooth_setting) {
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(p0: View?) {
            binding?.run {
        }
    }

}