package com.mansao.trianglesneacare.ui.screen.section.customer.transaction.transactionList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.GetPaymentStatusResponse
import com.mansao.trianglesneacare.data.network.response.GetTransactionsByIdResponse
import com.mansao.trianglesneacare.data.network.response.OnlyMsgResponse
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

    private var _getOnlinePaymentStatusUiState: MutableStateFlow<UiState<GetPaymentStatusResponse>> =
        MutableStateFlow(UiState.Standby)
    val getOnlinePaymentStatusUiState: Flow<UiState<GetPaymentStatusResponse>> = _getOnlinePaymentStatusUiState

    private var _updatePaymentStatusUiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val updatePaymentStatusUiState: Flow<UiState<OnlyMsgResponse>> = _updatePaymentStatusUiState


    private var _paymentStatus: MutableStateFlow<String> =
        MutableStateFlow("")
    val paymentStatus: Flow<String> = _paymentStatus

    fun setPaymentStatus(status:String){
        _paymentStatus.value = status
    }

    fun getTransactions() = viewModelScope.launch {
        try {
            val userId = appRepositoryImpl.getUserId() ?: ""
            val result = appRepositoryImpl.getTransactionById(userId)
            _transactionsUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _transactionsUiState.value = UiState.Error(e.message.toString())

        }
    }

    fun getPaymentStatus(transactionId: String) = viewModelScope.launch {
        try {
            val result = appRepositoryImpl.getPaymentStatus(transactionId)
            _getOnlinePaymentStatusUiState.value = UiState.Success(result)
//            updatePaymentStatus(transactionId, result.transactionStatus)
        } catch (e: Exception) {
            _getOnlinePaymentStatusUiState.value = UiState.Error(e.message.toString())

        }
    }

     fun updatePaymentStatus(transactionId: String, paymentStatus: String) =
        viewModelScope.launch {
            try {
                val result=  appRepositoryImpl.updatePaymentStatus(transactionId, paymentStatus)
                _updatePaymentStatusUiState.value = UiState.Success(result)
            } catch (e: Exception) {
                _updatePaymentStatusUiState.value = UiState.Error(e.message.toString())
            }
        }
}