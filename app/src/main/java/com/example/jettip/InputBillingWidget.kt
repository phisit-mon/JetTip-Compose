package com.example.jettip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.rounded.Money
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@ExperimentalComposeUiApi
@Composable
fun InputBillingTextField(
    value: String,
    labelId: String,
    enabled: Boolean,
    singleLine: Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = labelId)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Rounded.Money, contentDescription = "Money Icon")
        },
        modifier = modifier,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        enabled = enabled,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = keyboardType
        ),
        keyboardActions = onAction
    )
}

@Composable
fun BillSplitter(
    split: Int,
    onValueIncrease: () -> Unit,
    onValueDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundedIconButton(
            imageVector = Icons.Default.Remove,
            onClick = onValueDecrease
        )
        Text(
            text = "$split",
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        RoundedIconButton(
            imageVector = Icons.Default.Add, onClick = onValueIncrease
        )
    }
}


@Composable
fun SummaryTip(
    tip: Float,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = "Tip",
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "$${String.format(Locale.US, "%.2f", tip)}",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun TipSlider(
    onPercentageChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var slidePosition by rememberSaveable {
        mutableStateOf(0f)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = ("position : ${String.format(Locale.US, "%.0f", slidePosition)} %"))
        Slider(
            value = slidePosition,
            modifier = Modifier.padding(horizontal = 8.dp),
            steps = 100,
            valueRange = 0f..100f,
            onValueChangeFinished = {},
            onValueChange = {
                slidePosition = it
                onPercentageChange(slidePosition)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TipSliderPreview() {
    TipSlider(onPercentageChange = {})
}

@Preview(showBackground = true)
@Composable
private fun SummaryTipPreview() {
    SummaryTip(tip = 0f)
}


@Preview(showBackground = true)
@Composable
private fun BillSplitterPreview() {
    BillSplitter(
        split = 5,
        onValueDecrease = {},
        onValueIncrease = {}
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
private fun InputBillingTextFieldPreview() {
    InputBillingTextField(
        "Hello world",
        "Enter Bill",
        enabled = true,
        singleLine = true,
        onValueChange = {})
}