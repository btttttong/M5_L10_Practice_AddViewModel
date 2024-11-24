package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DessertViewModel : ViewModel() {
    private val desserts = Datasource.dessertList
    private val _uiState = MutableStateFlow(
        DessertUiState(
            currentDessertImageId = desserts[0].imageId,
            currentDessertPrice = desserts[0].price
        )
    )
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    private var dessertsSold = 0
    private var revenue = 0

    fun onDessertClicked() {
        revenue += _uiState.value.currentDessertPrice
        dessertsSold++

        val nextDessert = determineDessertToShow(dessertsSold)
        _uiState.value = _uiState.value.copy(
            revenue = revenue,
            dessertsSold = dessertsSold,
            currentDessertImageId = nextDessert.imageId,
            currentDessertPrice = nextDessert.price
        )
    }

    /**
     * Determine which dessert to show.
     */
    private fun determineDessertToShow(dessertsSold: Int): Dessert {
        return desserts.last { it.startProductionAmount <= dessertsSold }
    }
}