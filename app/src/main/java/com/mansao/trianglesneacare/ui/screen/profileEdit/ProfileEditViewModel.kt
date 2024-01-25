package com.mansao.trianglesneacare.ui.screen.profileEdit

import androidx.lifecycle.ViewModel
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(private val appRepositoryIMpl: AppRepositoryImpl):ViewModel() {
}