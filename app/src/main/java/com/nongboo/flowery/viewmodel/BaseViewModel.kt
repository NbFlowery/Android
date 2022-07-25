package com.nongboo.flowery.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import com.nongboo.flowery.FloweryApplication
import com.nongboo.flowery.utilities.SF_KEY

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    /*private val sf: SharedPreferences = getApplication<FloweryApplication>().getSharedPreferences(
        SF_KEY,
        Context.MODE_PRIVATE
    )
    val editor: SharedPreferences.Editor = sf.edit()*/
}