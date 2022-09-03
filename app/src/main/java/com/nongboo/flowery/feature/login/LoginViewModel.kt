package com.nongboo.flowery.feature.login

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.nongboo.flowery.utilities.google_web_api_client_id_key
import com.nongboo.flowery.feature.BaseViewModel

class LoginViewModel(application: Application) : BaseViewModel(application){
    private var mGoogleSignInClient: GoogleSignInClient

    init {
        /* Google Login Init */
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(google_web_api_client_id_key)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(application, gso)
    }

    fun getGoogleSignInClient() = mGoogleSignInClient
}