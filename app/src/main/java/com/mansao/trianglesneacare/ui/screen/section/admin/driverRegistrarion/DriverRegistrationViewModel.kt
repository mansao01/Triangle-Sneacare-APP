package com.mansao.trianglesneacare.ui.screen.section.admin.driverRegistrarion

import androidx.lifecycle.ViewModel
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DriverRegistrationViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl):ViewModel() {
}