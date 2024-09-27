package com.bbbdem.koha.module.splash_screen

data class TokenRequest(var grant_type:String,
                        var client_id:String,
                        var client_secret:String)

