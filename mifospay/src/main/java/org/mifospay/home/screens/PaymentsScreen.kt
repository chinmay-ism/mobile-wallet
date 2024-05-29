package org.mifospay.home.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mifospay.core.model.domain.Transaction
import org.mifospay.core.ui.MifosScrollableTabRow
import org.mifospay.core.ui.utility.TabContent
import org.mifospay.history.ui.HistoryScreen
import org.mifospay.invoice.ui.InvoiceScreen
import org.mifospay.payments.TransferViewModel
import org.mifospay.payments.send.SendScreenRoute
import org.mifospay.payments.ui.RequestScreen
import org.mifospay.standinginstruction.ui.StandingInstructionsScreen

@Composable
fun PaymentsRoute(
    viewModel: TransferViewModel = hiltViewModel(),
    showQr: (String) -> Unit,
    onNewSI: () -> Unit,
    viewReceipt: (String) -> Unit,
    onAccountClicked: (String, ArrayList<Transaction>) -> Unit
) {
    val vpa by viewModel.vpa.collectAsStateWithLifecycle()
    PaymentScreenContent(vpa = vpa, showQr = showQr, onNewSI = onNewSI, onAccountClicked = onAccountClicked, viewReceipt = viewReceipt)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaymentScreenContent(
    vpa: String,
    showQr: (String) -> Unit,
    onNewSI: () -> Unit,
    viewReceipt: (String) -> Unit,
    onAccountClicked: (String, ArrayList<Transaction>) -> Unit
) {

    val pagerState = rememberPagerState(
        pageCount = { PaymentsScreenContents.entries.size }
    )

    val tabContents = listOf(
        TabContent(PaymentsScreenContents.SEND.name) {
            SendScreenRoute(
                onBackClick = {},
                showToolBar = false,
                proceedWithMakeTransferFlow = { externalId, transferAmount ->

                }
            )
        },
        TabContent(PaymentsScreenContents.REQUEST.name) {
            RequestScreen(showQr = { showQr.invoke(vpa) })
        },
        TabContent(PaymentsScreenContents.HISTORY.name) {
            HistoryScreen(
                accountClicked = onAccountClicked,
                viewReceipt = viewReceipt
            )
        },
        TabContent(PaymentsScreenContents.SI.name) {
            StandingInstructionsScreen(onNewSI = { onNewSI.invoke() })
        },
        TabContent(PaymentsScreenContents.INVOICES.name) {
            InvoiceScreen()
        }
    )

    Column(modifier = Modifier.fillMaxSize()) {
        MifosScrollableTabRow(tabContents = tabContents, pagerState = pagerState)
    }
}

enum class PaymentsScreenContents {
    SEND,
    REQUEST,
    HISTORY,
    SI,
    INVOICES
}

@Preview(showBackground = true)
@Composable
fun PaymentsScreenPreview() {
    PaymentScreenContent(vpa = "", { _ -> }, {}, {},{ _, _ ->})
}
