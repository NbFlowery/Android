package com.nongboo.flowery.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.util.DataUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.nongboo.flowery.MainActivity
import com.nongboo.flowery.R
import com.nongboo.flowery.databinding.ActivityLoginBinding
import com.nongboo.flowery.utilities.google_web_api_client_id_key
import com.nongboo.flowery.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var startGoogleLoginForResult: ActivityResultLauncher<Intent>
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        //metion
        val textData: String = binding.flowery.text.toString()
        val builder = SpannableStringBuilder(textData)
        val colorSpan = ForegroundColorSpan(Color.parseColor("#84A47D"))
        builder.setSpan(colorSpan, 8, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.flowery.text = builder

        auth = Firebase.auth

        binding.loginBtnGoogle.setOnClickListener {
            googleSignIn() //로그인 통합 페이지로 넘김
        }

        initGoogle() // RegisterActivityResult Init
    }

    private fun initGoogle() { //다른 앱/액티비티가 실행된 후, 그 실행이 끝난 후 다시 이 액티비티로 돌아왔을 때
        startGoogleLoginForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.let { data ->
                        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            val account = task.getResult(ApiException::class.java)!!
                            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                            firebaseAuthWithGoogle(account.idToken!!)
                        } catch (e: ApiException) {
                            // Google Sign In failed, update UI appropriately
                            Log.w(TAG, "Google sign in failed", e)
                        }
                    }
                    // Google Login Success
                } else {
                    Log.e(TAG, "Google Result Error ${result.resultCode}")
                }
            }
    }

    // Start Auth With Google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser) // 이미 로그인 되어있을 시 바로 메인 액티비티로 이동
    }

    private fun googleSignIn() { //loginViewModel.getGoogleSignInClient()= GoogleSignIn.getClient(this,gso)
        startGoogleLoginForResult.launch(loginViewModel.getGoogleSignInClient().signInIntent)
    }


    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {//로그인 성공하면
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Log.d(TAG, "signInWithCredential:failed")
        }
    }

    companion object {
        const val TAG: String = "LoginActivity"
    }
}