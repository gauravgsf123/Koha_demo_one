package com.bbbdem.koha.network

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bbbdem.koha.dashboard.MainViewModel
import com.bbbdem.koha.login.LoginViewModel
import com.bbbdem.koha.module.dashboard.DashboardViewModel
import com.bbbdem.koha.module.forgot.ForgotViewModel
import com.bbbdem.koha.module.my_account.change_password.ChangePasswordViewModel
import com.bbbdem.koha.module.my_account.charges.ChargesViewModel
import com.bbbdem.koha.module.my_account.personal_detail.PersonalDetailViewModel
import com.bbbdem.koha.module.my_account.purchase_suggestions.SuggestionViewModel
import com.bbbdem.koha.module.my_account.reading_history.ReadingHistoryViewModel
import com.bbbdem.koha.module.my_account.summary.SummaryDetailViewModel
import com.bbbdem.koha.module.notification.NotificationViewModel
import com.bbbdem.koha.module.otp.OTPViewModel
import com.bbbdem.koha.module.registration.RegistrationViewModel
import com.bbbdem.koha.module.reset_password.ResetPasswordViewModel
import com.bbbdem.koha.module.set_new_password.SetNewPasswordViewModel
import com.bbbdem.koha.module.splash_screen.SplashViewModel

class ViewModelFactoryClass(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(ForgotViewModel::class.java)) {
            return ForgotViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(ResetPasswordViewModel::class.java)) {
            return ResetPasswordViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(SetNewPasswordViewModel::class.java)) {
            return SetNewPasswordViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(PersonalDetailViewModel::class.java)) {
            return PersonalDetailViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(SummaryDetailViewModel::class.java)) {
            return SummaryDetailViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(ReadingHistoryViewModel::class.java)) {
            return ReadingHistoryViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
            return ChangePasswordViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(ChargesViewModel::class.java)) {
            return ChargesViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(SuggestionViewModel::class.java)) {
            return SuggestionViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(OTPViewModel::class.java)) {
            return OTPViewModel(app) as T
        }
        else if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}