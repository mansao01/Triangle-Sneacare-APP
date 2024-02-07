package com.mansao.trianglesneacare.ui.screen.section.customer.updateAddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateAddressViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {

    fun updateAddress() {
        viewModelScope.launch {
            try {

            } catch (e: Exception) {

            }
        }
    }

    fun deleteAddress() {
        viewModelScope.launch {
            try {

            } catch (e: Exception) {

            }
        }
    }
}