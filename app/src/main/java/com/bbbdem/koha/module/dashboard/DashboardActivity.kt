package com.bbbdem.koha.module.dashboard

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.RemoteViews
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.R
import com.bbbdem.koha.app.MVVMBindingActivity
import com.bbbdem.koha.common.Constant
import com.bbbdem.koha.databinding.ActivityDashboardBinding
import com.bbbdem.koha.login.LoginActivity
import com.bbbdem.koha.login.model.UserDetailResponseModel
import com.bbbdem.koha.module.about_app.AboutAppFragment
import com.bbbdem.koha.module.about_library.AboutLibraryFragment
import com.bbbdem.koha.module.about_library.LibraryContactInfoFragment
import com.bbbdem.koha.module.about_library.RulesAndRegulationFragment
import com.bbbdem.koha.module.dashboard.bottomsheet.AdvanceFilterFragment
import com.bbbdem.koha.module.dashboard.bottomsheet.FilterFragment
import com.bbbdem.koha.module.dashboard.bottomsheet.ScanByFragment
import com.bbbdem.koha.module.dashboard.fragment.HomeFragment
import com.bbbdem.koha.module.dashboard.fragment.SearchListFragment
import com.bbbdem.koha.module.my_account.change_password.ChangePasswordFragment
import com.bbbdem.koha.module.my_account.charges.ChargesFragment
import com.bbbdem.koha.module.my_account.discharge.DischargeFragment
import com.bbbdem.koha.module.my_account.my_qr_code.QRCodeFragment
import com.bbbdem.koha.module.my_account.personal_detail.PersonalDetailMainFragment
import com.bbbdem.koha.module.my_account.purchase_suggestions.PurchaseSuggestionListFragment
import com.bbbdem.koha.module.my_account.reading_history.ReadingHistoryFragment
import com.bbbdem.koha.module.my_account.summary.SummaryDetailFragment
import com.bbbdem.koha.module.my_account.summary.model.CheckoutResponseModel
import com.bbbdem.koha.module.notification.NotificationFragment
import com.bbbdem.koha.module.notification.model.NotificationModel
import com.bbbdem.koha.module.setting.SettingFragment
import com.bbbdem.koha.network.Resource
import com.bbbdem.koha.network.ViewModelFactoryClass
import com.bbbdem.koha.utils.ProgressDialog
import com.bbbdem.koha.utils.Utils
import com.bbbdem.koha.view.ConfirmationDialogFragment
import com.google.gson.Gson
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class DashboardActivity : MVVMBindingActivity<ActivityDashboardBinding>() {
    private lateinit var userDetail: UserDetailResponseModel
    private lateinit var viewModel:DashboardViewModel
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    val CHANNEL_ID = "GFG"
    val CHANNEL_NAME = "GFG ContentWriting"
    val CHANNEL_DESCRIPTION = "GFG NOTIFICATION"
    var c: Date? = null
    var df: SimpleDateFormat? = null
    companion object {
        private const val REQUEST_CODE_STT = 1
    }
    private val textToSpeechEngine: TextToSpeech by lazy {
        TextToSpeech(this,
            TextToSpeech.OnInitListener { status ->
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeechEngine.language = Locale.UK
                }
            })
    }

    override fun initializeView() {
        onViewClick()
        viewHideGone()
        viewModel = ViewModelProvider(
            this,
            ViewModelFactoryClass(this.application)
        )[DashboardViewModel::class.java]
        binding?.homeToolbarView?.binding?.tvTitle?.text = sharedPreference.getValueString(Constant.LIBRARY_NAME)
        if(sharedPreference.getStartPage(Constant.START_PAGE)==Constant.StartScreen.LIBRARY_HOME) {
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit()
        }else{
            //binding?.bottomTab?bi.menu?.getItem(3)?.isChecked = true
            binding?.bottomTab?.binding?.bottomNavigationView?.menu?.getItem(3)?.isChecked = true
            val fragment = SummaryDetailFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit()
        }
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        setupNavigation()
        var notificationCount = sharedPreference.getValueInt(Constant.NOTIFICATION_COUNT)
        if(notificationCount>0){
            binding!!.homeToolbarView.binding.tvCount.text = notificationCount.toString()
        }else binding!!.homeToolbarView.binding.tvCount.visibility = View.GONE

        if (sharedPreference.getValueInt(Constant.PATRON_ID)!=0) {
            binding?.tvLogout?.text = resources.getString(R.string.logout)
            checkCurrentDate()
        } else binding?.tvLogout?.text = resources.getString(R.string.login)

        updateStatusBarColor("#000000")
        setObserver()
    }

    private fun checkCurrentDate(){
        df = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        c = Calendar.getInstance().time
        val currentDate = df!!.format(c)
        Log.d("current_date",currentDate+sharedPreference.getValueString(Constant.CURRENT_DATE))
        if(currentDate!=sharedPreference.getValueString(Constant.CURRENT_DATE)){
            viewModel.getCheckout(sharedPreference.getValueInt(Constant.PATRON_ID).toString())
        }
        //viewModel.getCheckout(sharedPreference.getValueInt(Constant.PATRON_ID).toString())
        sharedPreference.save(Constant.CURRENT_DATE,currentDate)

    }
    private fun setObserver(){
        viewModel.checkoutResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    checkForNotification(response.data)
                }
                is Resource.Loading -> {
                    ProgressDialog.showProgressBar(this)
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

        viewModel.notificationAddResponseModel.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    ProgressDialog.hideProgressBar()
                    //showNotification(title, message)
                }
                is Resource.Loading -> {
                    ProgressDialog.showProgressBar(this)
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

    private fun checkForNotification(data: List<CheckoutResponseModel>?) {
        data?.forEach {
            if(it.dueDate?.let { it1 -> Utils.daysCount(it1) }!! <=sharedPreference.getValueInt(Constant.WARRING_DAYS,1)){
                Log.d("dueDate","${it.dueDate?.let { it1 -> Utils.daysCount(it1) }!!} ${sharedPreference.getValueInt(Constant.WARRING_DAYS,1)}")
                createNotification(it)
            }
        }
    }

    private fun createNotification(checoutResponseModel: CheckoutResponseModel) {
        val title = "Due date reminder"//resources.getString(R.string.app_name)
        val message = "${checoutResponseModel.bookDetailResponseModel?.title} Book due date is over on ${checoutResponseModel.dueDate?.let {
            Utils.changeDateFormat(
                it
            )
        }}, Please renew"
        val requestModel = NotificationModel()
        requestModel.title = title
        requestModel.message = message
        requestModel.libraryId =Constant.ID_LIBRARY
        requestModel.userId =sharedPreference.getValueInt(Constant.PATRON_ID).toString()
        requestModel.bookId =checoutResponseModel?.itemDetailResponseModel?.biblioId?.toString()
        requestModel.dateTime =Utils.getDateTime("yyyy-MM-dd HH:mm")
        requestModel.status ="1"
        viewModel.addNotification(requestModel)
        showNotification(title,message)
    }

    private fun showNotification(title: String, message: String) {
        val intent = Intent(this, DashboardActivity::class.java)

        val pendingIntent : PendingIntent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        // RemoteViews are used to use the content of
        // some different layout apart from the current activity layout
        val contentView = RemoteViews(packageName, R.layout.activity_after_notification)

        // checking if android version is greater than oreo(API 26) or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                //.setContent(contentView)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                //.setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
        } else {

            builder = Notification.Builder(this)
                //.setContent(contentView)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                //.setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234, builder.build())
        var notificationCount = sharedPreference.getValueInt(Constant.NOTIFICATION_COUNT)
        sharedPreference.save(Constant.NOTIFICATION_COUNT,++notificationCount)
        binding!!.homeToolbarView.binding.tvCount.visibility = View.VISIBLE
        binding!!.homeToolbarView.binding.tvCount.text = notificationCount.toString()

    }

    override fun provideViewResource(): Int {
        return R.layout.activity_dashboard
    }

    fun updateStatusBarColor(color: String?) { // Color must be in hexadecimal fromat
        /*val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(color)*/

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.button_color)
    }

    override fun onClick(view: View?) {
        binding?.run {
            closeDrawer()
            when (view?.id) {
                tvHome.id -> {
                    binding?.bottomTab?.binding?.bottomNavigationView?.menu?.getItem(1)?.isChecked = true
                    val fm: FragmentManager = supportFragmentManager
                    for (i in 0 until fm.backStackEntryCount) {
                        fm.popBackStack()
                    }
                    replaceFragment(HomeFragment())
                }
                tvSummary.id -> loginUser(SummaryDetailFragment())
                tvPersonalDetails.id -> loginUser(PersonalDetailMainFragment())
                tvReadingHistory.id -> loginUser(ReadingHistoryFragment())
                tvCharge.id -> loginUser(ChargesFragment())
                tvChangePassword.id -> loginUser(ChangePasswordFragment())
                tvPurchaseSuggestions.id -> loginUser(PurchaseSuggestionListFragment())
                tvMyQRCode.id -> loginUser(QRCodeFragment())
                tvDischarge.id -> loginUser(DischargeFragment())
                tvAboutUs.id -> loginUser(AboutLibraryFragment())
                tvRulesRegulation.id -> loginUser(RulesAndRegulationFragment())
                tvContactUs.id -> loginUser(LibraryContactInfoFragment())
                consSearchCircle.id -> {
                    AdvanceFilterFragment().show(
                        supportFragmentManager,
                        "AdvanceFilterFragment"
                    )
                }
                consFolderSearch.id -> {
                    ScanByFragment().show(
                        supportFragmentManager,
                        "ScanByFragment"
                    )
                }
                consAboutApp.id -> replaceFragment(AboutAppFragment())
                consSetting.id -> replaceFragment(SettingFragment())
                homeToolbarView.binding.filter.id -> FilterFragment().show(
                    supportFragmentManager,
                    "FilterFragment"
                )
                homeToolbarView.binding.tvCount.id,homeToolbarView.binding.ivNotification.id ->{
                    sharedPreference.save(Constant.NOTIFICATION_COUNT,0)
                    binding!!.homeToolbarView.binding.tvCount.visibility = View.GONE
                    loginUser(NotificationFragment())
                }
                tvLogout.id -> checkUserLogin()
                ivClose.id -> closeDrawer()
                toolbarView.binding.rlBack.id -> {
                    showToolbar(false)
                    onBackPressed()
                }

                homeToolbarView.binding.toolbarDrawer.id -> {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        closeDrawer()
                    } else {
                        openDrawer()
                    }
                }
                homeToolbarView.binding.ivVoice.id -> {
                    val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    sttIntent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")

                    try {
                        startActivityForResult(sttIntent, REQUEST_CODE_STT)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                        Toast.makeText(this@DashboardActivity, "Your device does not support STT.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_STT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    result?.let {
                        val recognizedText = it[0]
                        binding!!.homeToolbarView.binding.editTextSearch.setText(recognizedText)
                        if(recognizedText.isNotEmpty()) callSearchApi()
                    }
                }
            }
        }
    }

    private fun callSearchApi(){
        val searchText = binding!!.homeToolbarView.binding.editTextSearch.text.trim().toString()
        val query = "{\"-or\":[{\"author\":{\"-like\":\"%$searchText%\"}},{\"title\":{\"-like\":\"%$searchText%\"}},{\"publisher\":{\"-like\":\"%$searchText%\"}},{\"isbn\":{\"-like\":\"%$searchText%\"}},{\"series_title\":{\"-like\":\"%$searchText%\"}}]}"
        val bundle = Bundle()
        bundle.putString(Constant.LIBRARY,"")
        bundle.putString(Constant.CATEGORY,"")
        bundle.putString(Constant.SEARCH_BY,"")
        bundle.putString(Constant.SEARCH,query)
        bundle.putInt(Constant.FRAGMENT_TYPE,Constant.FragmentType.SEARCH)
        val fragment = SearchListFragment.newInstance()
        fragment.arguments = bundle
        replaceFragment(fragment)
    }

    override fun onPause() {
        textToSpeechEngine.stop()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!sharedPreference.getValueBoolean(Constant.IS_LOGIN, false)) {
            val token = sharedPreference.getValueString(Constant.ACCESS_TOKEN)
            sharedPreference.clearSharedPreference()
            sharedPreference.save(Constant.ACCESS_TOKEN, token!!)
        }
        textToSpeechEngine.shutdown()

    }

    private fun checkUserLogin(){
        if (sharedPreference.getValueInt(Constant.PATRON_ID)!=0) {
            showConformationDialog()
        }else{
            startNewActivity(LoginActivity())
            finish()
        }
    }

    private fun loginUser(fragment:Fragment) {
        if (sharedPreference.getValueInt(Constant.PATRON_ID)!=0) {
            replaceFragment(fragment)
        }else{
            ConfirmationDialogFragment.newInstance(
                resources.getString(R.string.do_you_want_login),
                object : ConfirmationDialogFragment.ConfirmationDialogFragmentListener {
                    override fun onConfirmationCancelClick() {}
                    override fun onConfirmationOKClick() {
                        startNewActivity(LoginActivity())
                        finish()
                    }

                }
            ).show(supportFragmentManager, "logout")
        }
    }

    private fun showConformationDialog() {
        ConfirmationDialogFragment.newInstance(
            resources.getString(R.string.are_you_sure_you_want_to_logout),
            object : ConfirmationDialogFragment.ConfirmationDialogFragmentListener {

                override fun onConfirmationCancelClick() {

                }

                override fun onConfirmationOKClick() {
                    val token = sharedPreference.getValueString(Constant.ACCESS_TOKEN)
                    val libraryName = sharedPreference.getValueString(Constant.LIBRARY_NAME)
                    sharedPreference.clearSharedPreference()
                    sharedPreference.save(Constant.IS_LOGIN, false)
                    sharedPreference.save(Constant.LIBRARY_NAME, libraryName!!)
                    sharedPreference.save(Constant.ACCESS_TOKEN, token!!)
                    startNewActivity(LoginActivity())
                    finish()
                }

            }
        ).show(supportFragmentManager, "logout")
    }

    private fun setupNavigation() {
        var userDetailResponseModel = sharedPreference.getValueString(Constant.USER_DETAIL)
        if (!userDetailResponseModel.isNullOrEmpty()) {
            userDetail =
                Gson().fromJson(userDetailResponseModel, UserDetailResponseModel::class.java)
            binding?.run {
                profileName.text = "${userDetail.firstname} ${userDetail.surname}"
                tvUserName.text = "${userDetail.userid}"
                tvInitials.text =
                    Utils.getNameInitials("${userDetail.firstname} ${userDetail.surname}")
            }
        }
    }

    private fun viewHideGone() {
        binding?.run {
            lnrHome.setOnClickListener {
                if (consMyAccount.visibility == View.VISIBLE) {
                    consMyAccount.visibility = View.GONE
                    lnrHome.setBackgroundResource(R.color.white)
                    tvMyAccount.setTextColor(resources.getColor(R.color.text_color_secondary))
                } else {
                    tvMyAccount.setTextColor(resources.getColor(R.color.white))
                    consMyAccount.visibility = View.VISIBLE
                    lnrHome.setBackgroundResource(R.color.btbBack)
                }
            }

            lnrLibrary.setOnClickListener {
                if (consAboutLibrary.visibility == View.VISIBLE) {
                    consAboutLibrary.visibility = View.GONE
                    lnrLibrary.setBackgroundResource(R.color.white)
                    tvAboutLibrary.setTextColor(resources.getColor(R.color.text_color_secondary))
                } else {
                    tvAboutLibrary.setTextColor(resources.getColor(R.color.white))
                    consAboutLibrary.visibility = View.VISIBLE
                    lnrLibrary.setBackgroundResource(R.color.btbBack)
                }
            }
        }
    }

    private fun onViewClick() {
        binding?.run {
            tvHome.setOnClickListener(this@DashboardActivity)
            tvSummary.setOnClickListener(this@DashboardActivity)
            tvPersonalDetails.setOnClickListener(this@DashboardActivity)
            tvReadingHistory.setOnClickListener(this@DashboardActivity)
            tvCharge.setOnClickListener(this@DashboardActivity)
            tvChangePassword.setOnClickListener(this@DashboardActivity)
            tvPurchaseSuggestions.setOnClickListener(this@DashboardActivity)
            tvMyQRCode.setOnClickListener(this@DashboardActivity)
            consAboutApp.setOnClickListener(this@DashboardActivity)
            consSetting.setOnClickListener(this@DashboardActivity)
            consFolderSearch.setOnClickListener(this@DashboardActivity)
            consSearchCircle.setOnClickListener(this@DashboardActivity)
            tvDischarge.setOnClickListener(this@DashboardActivity)
            tvAboutUs.setOnClickListener(this@DashboardActivity)
            tvRulesRegulation.setOnClickListener(this@DashboardActivity)
            tvContactUs.setOnClickListener(this@DashboardActivity)
            tvLogout.setOnClickListener(this@DashboardActivity)
            bottomTab.binding.fabSearch.setOnClickListener(this@DashboardActivity)
            toolbarView.binding.rlBack.setOnClickListener(this@DashboardActivity)
            homeToolbarView.binding.toolbarDrawer.setOnClickListener(this@DashboardActivity)
            homeToolbarView.binding.filter.setOnClickListener(this@DashboardActivity)
            homeToolbarView.binding.ivVoice.setOnClickListener(this@DashboardActivity)
            homeToolbarView.binding.ivNotification.setOnClickListener(this@DashboardActivity)
            homeToolbarView.binding.tvCount.setOnClickListener(this@DashboardActivity)
            ivClose.setOnClickListener(this@DashboardActivity)


            consHome.setOnClickListener {
                consHome.setBackgroundResource(R.color.btbBack)
                tvHome.setTextColor(ContextCompat.getColor(this@DashboardActivity, R.color.white))
                ivHomee.setImageResource(R.drawable.baseline_home_24)

            }
            consNotification.setOnClickListener {
                consNotification.setBackgroundResource(R.color.btbBack)
                tvNotification.setTextColor(
                    ContextCompat.getColor(
                        this@DashboardActivity,
                        R.color.white
                    )
                )
                ivNotification.setImageResource(R.drawable.notification_white)

            }

            homeToolbarView.binding.editTextSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(v)
                    val searchText = homeToolbarView.binding.editTextSearch.text.toString()
                    val query = "{\"-or\":[{\"author\":{\"-like\":\"%$searchText%\"}},{\"title\":{\"-like\":\"%$searchText%\"}},{\"publisher\":{\"-like\":\"%$searchText%\"}},{\"isbn\":{\"-like\":\"%$searchText%\"}},{\"series_title\":{\"-like\":\"%$searchText%\"}}]}"
                    val bundle = Bundle()
                    bundle.putString(Constant.LIBRARY,"")
                    bundle.putString(Constant.CATEGORY,"")
                    bundle.putString(Constant.SEARCH_BY,"")
                    bundle.putString(Constant.SEARCH,query)
                    bundle.putInt(Constant.FRAGMENT_TYPE,Constant.FragmentType.SEARCH)
                    val fragment = SearchListFragment.newInstance()
                    fragment.arguments = bundle
                    replaceFragment(fragment)
                    return@OnEditorActionListener true
                }
                false
            })

            /*consSearchCircle.setOnClickListener {
                consSearchCircle.setBackgroundResource(R.color.btbBack)
                tvSearchCircle.setTextColor(
                    ContextCompat.getColor(
                        this@DashboardActivity,
                        R.color.white
                    )
                )
                ivNotification.setImageResource(R.drawable.notification_white)

            }*/

           /* consFolderSearch.setOnClickListener {
                consFolderSearch.setBackgroundResource(R.color.btbBack)
                tvSearchBook.setTextColor(
                    ContextCompat.getColor(
                        this@DashboardActivity,
                        R.color.white
                    )
                )
                ivNotification.setImageResource(R.drawable.notification_white)

            }*/

            bottomTab.binding.bottomNavigationView.setOnNavigationItemSelectedListener {
                if (it.itemId == R.id.home) {
                    val fm: FragmentManager = supportFragmentManager
                    for (i in 0 until fm.backStackEntryCount) {
                        fm.popBackStack()
                    }
                    replaceFragment(HomeFragment())
                } else if (it.itemId == R.id.barCode) {
                    /*val options = ScanOptions()
                    options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES)
                    options.setPrompt("Scan a barcode")
                    options.setCameraId(0) // Use a specific camera of the device
                    options.setBeepEnabled(false)
                    options.setBarcodeImageEnabled(true)
                    barcodeLauncher.launch(options)*/
                    ScanByFragment().show(
                        supportFragmentManager,
                        "ScanByFragment"
                    )
                }else if(it.itemId == R.id.summary){
                    loginUser(SummaryDetailFragment())
                }else if(it.itemId == R.id.profile){
                    loginUser(PersonalDetailMainFragment())
                }
                true
            }
            bottomTab.binding.fabSearch.setOnClickListener {
                AdvanceFilterFragment().show(
                    supportFragmentManager,
                    "AdvanceFilterFragment"
                )
            }

        }

    }

    private val barcodeLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@DashboardActivity, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                this@DashboardActivity,
                "Scanned: " + result.contents,
                Toast.LENGTH_LONG
            ).show()

            val bundle = Bundle()
            bundle.putString(Constant.LIBRARY, "")
            bundle.putString(Constant.CATEGORY, "")
            bundle.putString(Constant.SEARCH_BY, "external_id")
            bundle.putString(Constant.SEARCH, result.contents)
            bundle.putInt(Constant.FRAGMENT_TYPE, Constant.FragmentType.BARCODE_SCAN)
            val fragment = SearchListFragment.newInstance()
            fragment.arguments = bundle

            replaceFragment(fragment)
        }
    }

    fun showToolbar(visible: Boolean) {
        binding?.run {
            if (visible) {
                homeToolbarView.visibility = View.GONE
                toolbarView.visibility = View.VISIBLE
            } else {
                homeToolbarView.visibility = View.VISIBLE
                toolbarView.visibility = View.GONE
            }
        }
    }

    fun showReadingHistoryToolbar(visible: Boolean) {
        binding?.run {
            if (visible) {
                homeToolbarView.visibility = View.GONE
                toolbarView.visibility = View.GONE
            } else {
                homeToolbarView.visibility = View.VISIBLE
                toolbarView.visibility = View.GONE
            }
        }
    }

    fun showHomeToolbar() {
        binding?.run {
            homeToolbarView.visibility = View.VISIBLE
            toolbarView.visibility = View.GONE
        }
    }

    fun setAppTitle(title: String) {
        binding?.toolbarView?.binding?.tvTitle?.text = title
    }

    override fun onBackPressed() {
        if (binding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else {
            showReadingHistoryToolbar(false)
            super.onBackPressed()
        }
    }

    private fun openDrawer() {
        binding?.drawerLayout?.openDrawer(GravityCompat.START)
    }

    private fun closeDrawer() {
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)
    }


    fun popBack() {
        val fm: FragmentManager = supportFragmentManager
        fm.popBackStack()
    }
}