package com.mansao.trianglesneacare.ui.screen.section.customer.transactionList

import androidx.lifecycle.ViewModel
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
}