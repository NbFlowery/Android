package com.nongboo.flowery

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nongboo.flowery.databinding.ActivityUserInformationBinding
import com.nongboo.flowery.view.LoginActivity
import java.util.*

class UserInformationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityUserInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        val user = Firebase.auth.currentUser
        auth = Firebase.auth

        //31개의 씨앗 중 꽃을
        cal.set(Calendar.DAY_OF_MONTH,1)
        var lastDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        binding.textView.text = "${lastDate}개의 씨앗 중 꽃을"

        //metion
        val textData: String = binding.textView2.text.toString()
        val builder = SpannableStringBuilder(textData)
        val colorSpan = ForegroundColorSpan(Color.parseColor("#EA7960"))
        builder.setSpan(colorSpan, 2, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.textView2.text = builder


        //로그아웃
        binding.logout.setOnClickListener {
            moveSignUpPage() //로그인 화면으로
            FirebaseAuth.getInstance().signOut()

        }

        //사용자 이메일
        binding.userEmailText.text = auth.currentUser?.email



    }

    //로그인 액티비티 호출
    private fun moveSignUpPage() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()

    }
}