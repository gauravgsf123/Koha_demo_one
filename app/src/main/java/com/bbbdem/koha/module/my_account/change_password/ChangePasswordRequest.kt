package com.bbbdem.koha.module.my_account.change_password

data class ChangePasswordRequest(var password         : String? = null,
                                 var password_2       : String? = null)
