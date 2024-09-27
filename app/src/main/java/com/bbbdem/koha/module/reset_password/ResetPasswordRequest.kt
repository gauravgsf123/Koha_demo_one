package com.bbbdem.koha.module.reset_password

data class ResetPasswordRequest(var password         : String? = null,
                                var password_repeated : String? = null,
                                var old_password      : String? = null)
