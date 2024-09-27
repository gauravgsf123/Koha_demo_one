package com.bbbdem.koha.app

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.ImageView
import androidx.databinding.DataBindingUtil


abstract class MVVMBindingActivity<B> : BaseActivity() {
    protected var binding: B? = null

    //protected var viewModel :V? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, provideViewResource())

        /*window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT*/
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {

            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.transparent);
        }*/

        //val viewModel: V by  provideViewModel() { ViewModalFactoryClass(application) }
        //viewModel = ViewModelProvider(this, ViewModalFactoryClass(application)).get(provideViewModel())
        initializeView()
    }

    abstract fun initializeView()

    abstract fun provideViewResource(): Int

    fun onBackArrow(backArrow: ImageView) {
        backArrow.setOnClickListener {
            finish()
        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }


}