package com.mansao.trianglesneacare.ui.screen.section.customer.transaction.transactionList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.GetTransactionsByIdResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {


    private var _transactionsUiState: MutableStateFlow<UiState<GetTransactionsByIdResponse>> =
        MutableStateFlow(UiState.Standby)
    val transactionsUiState: Flow<UiState<GetTransactionsByIdResponse>> = _transactionsUiState

    fun getTransactions() = viewModelScope.launch {
        _transactionsUiState.value = UiState.Loading
        try {
            val userId = appRepositoryImpl.getUserId() ?: ""
            val result = appRepositoryImpl.getTransactionById(userId)
            _transactionsUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _transactionsUiState.value = UiState.Error(e.message.toString())

        }
    }




}