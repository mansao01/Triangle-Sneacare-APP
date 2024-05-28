package com.mansao.trianglesneacare.ui.screen.section.customer.uploadImage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadImageViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {

        init {
            createOrder()
        }
        fun createOrder() = viewModelScope.launch {
            val userId = appRepositoryImpl.getUserId()
            Log.d("userId", userId.toString())

        }
}