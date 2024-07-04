package com.example.jettip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.math.RoundingMode
import java.text.DecimalFormat

class MainViewModel : ViewModel() {

    private val _splitPersons = MutableStateFlow(1)
    private val _tipPercentage = MutableStateFlow(0f)
    private val _enterBilling = MutableStateFlow(0f)

    val splitPersons: StateFlow<Int> = _splitPersons

    val totalTip = combine(_tipPercentage, _enterBilling){ tipPercentage, billing ->
        (tipPercentage / 100) * billing
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0f
    )

    val totalPerson = combine(_enterBilling, _splitPersons, totalTip) { billing, splitPerson, totalTip ->
        (billing + totalTip) / splitPerson
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0f
    )

    fun increaseSplitPersons() {
        if (_splitPersons.value < 10) {
            _splitPersons.value++
        }
    }

    fun decreaseSplitPersons() {
        if (_splitPersons.value > 1) {
            _splitPersons.value--
        }
    }

    fun tipPercentageChange(percentage: Float) {
        _tipPercentage.value = roundDownDecimal(percentage)
    }

    fun enterBilling(value: Float) {
        _enterBilling.value = value
    }

    private fun roundDownDecimal(number: Float): Float {
        val df = DecimalFormat("#")
        df.roundingMode = RoundingMode.HALF_UP
        return df.format(number).toFloat()
    }
}