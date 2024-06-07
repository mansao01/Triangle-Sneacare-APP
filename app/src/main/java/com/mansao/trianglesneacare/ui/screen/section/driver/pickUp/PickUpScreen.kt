package com.mansao.trianglesneacare.ui.screen.section.driver.pickUp

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.data.network.response.dto.TransactionsItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.HeaderText
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun PickUpScreen(
    pickUpViewModel: PickUpViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateToDetailPickUp: () -> Unit
) {
    LaunchedEffect(Unit) {
        pickUpViewModel.getPicUpList()

    }

    Scaffold(
        topBar = { PickUpTopBar() }
    ) { scaffoldPadding ->
        Surface(modifier = Modifier.padding(scaffoldPadding)) {
            PickUpContent(
                navigateToDetailPickUp = { navigateToDetailPickUp() },
                sharedViewModel = sharedViewModel,
                pickupViewModel = pickUpViewModel
            )
        }
    }
}

@Composable
fun PickUpContent(
    pickupViewModel: PickUpViewModel,
    navigateToDetailPickUp: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    pickupViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Toast.makeText(
                context,
                uiState.errorMessage,
                Toast.LENGTH_SHORT
            ).show()

            UiState.Loading -> LoadingDialog()
            UiState.Standby -> {

            }

            is UiState.Success -> {
                PickUpList(
                    transactions = uiState.data.transactions,
                    navigateToDetailPickUp = navigateToDetailPickUp
                )
            }
        }
    }
}

@Composable
fun PickUpList(
    modifier: Modifier = Modifier,
    transactions: List<TransactionsItem>,
    navigateToDetailPickUp: () -> Unit
) {
    LazyColumn {
        items(transactions) {
            PickUpListItem(
                transaction = it,
                modifier = modifier.clickable { navigateToDetailPickUp() })
        }
    }

}

@Composable
fun PickUpListItem(modifier: Modifier = Modifier, transaction: TransactionsItem) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = transaction.customerAddress.receiverName,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = transaction.customerAddress.fullAddress,
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = transaction.customerAddress.phone,
                style = TextStyle(
                    fontSize = 14.sp,
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickUpTopBar() {
    LargeTopAppBar(title = {
        HeaderText(
            text = "Pick Up",
            description = "",
            showDescription = false
        )
    })
}