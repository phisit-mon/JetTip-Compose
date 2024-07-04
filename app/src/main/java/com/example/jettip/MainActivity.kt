package com.example.jettip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jettip.ui.theme.JetTipTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetTipTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val viewModel: MainViewModel by viewModels()
                    val splitPersons by viewModel.splitPersons.collectAsStateWithLifecycle()
                    val totalTip by viewModel.totalTip.collectAsStateWithLifecycle()
                    val totalPerson by viewModel.totalPerson.collectAsStateWithLifecycle()

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(vertical = 20.dp, horizontal = 16.dp)
                    ) {

                        TotalBillingWidget(totalBill = totalPerson)

                        BillingScreen(
                            totalTip = totalTip,
                            splitPersons = splitPersons,
                            onEnterBillingDone = { viewModel.enterBilling(it) },
                            onTipPercentageChanged = { viewModel.tipPercentageChange(it) },
                            onIncreaseSplitter = { viewModel.increaseSplitPersons() },
                            onDecreaseSplitter = { viewModel.decreaseSplitPersons() },
                            modifier = Modifier.padding(top = 24.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillingScreen(
    totalTip: Float,
    splitPersons: Int,
    onEnterBillingDone: (Float) -> Unit,
    onIncreaseSplitter: () -> Unit,
    onDecreaseSplitter: () -> Unit,
    onTipPercentageChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var enterBillState by rememberSaveable {
        mutableStateOf("")
    }

    val validEnterBillState by rememberSaveable(enterBillState) {
        mutableStateOf(
            enterBillState.trim().isNotEmpty()
        )
    }

    Surface(
        modifier = modifier,
        shadowElevation = 6.dp,
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.DarkGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            InputBillingTextField(
                value = enterBillState,
                labelId = "Enter your bill",
                enabled = true,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onValueChange = {
                    enterBillState = it
                },
                onAction = KeyboardActions {
                    if (!validEnterBillState) {
                        return@KeyboardActions
                    }
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onEnterBillingDone(enterBillState.toFloat())
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            BillSplitter(
                split = splitPersons,
                onValueIncrease = onIncreaseSplitter,
                onValueDecrease = onDecreaseSplitter,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(8.dp))
            SummaryTip(
                tip = totalTip,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            TipSlider(
                onPercentageChange = onTipPercentageChanged,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            )

        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetTipTheme {
        BillingScreen(
            totalTip = 30f,
            splitPersons = 1,
            onEnterBillingDone = {},
            onTipPercentageChanged = {},
            onIncreaseSplitter = {},
            onDecreaseSplitter = {}
        )
    }
}