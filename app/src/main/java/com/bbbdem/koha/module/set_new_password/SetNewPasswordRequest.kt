package com.bbbdem.koha.module.set_new_password

data class SetNewPasswordRequest(var password         : String? = null,
                                 var password_2       : String? = null)
