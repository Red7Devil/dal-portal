package com.example.dalportal.ui.ta_portal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaPortalViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is TA portal Fragment"
    }
    val text: LiveData<String> = _text
}