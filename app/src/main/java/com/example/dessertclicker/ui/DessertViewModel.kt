package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.data.DessertUiState
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(DessertUiState())
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    init{
        reset()
    }

    fun reset(){
        val initDessert = Datasource.dessertList.first()
        _uiState.value = DessertUiState(currentDessertImageId = initDessert.imageId)
    }

    fun dessertClicked() {
        _uiState.update { currentState ->
            val dessertsSold = currentState.dessertsSold + 1
            // Show the next dessert
            val dessertToShow = determineDessertToShow(Datasource.dessertList, dessertsSold)
            currentState.copy(
                revenue = currentState.revenue + currentState.currentDessertPrice,
                dessertsSold = dessertsSold,
                currentDessertImageId = dessertToShow.imageId,
                currentDessertPrice = dessertToShow.price
            )
        }
    }

    /**
     * Determine which dessert to show.
     */
    fun determineDessertToShow(
        desserts: List<Dessert>,
        dessertsSold: Int
    ): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        return dessertToShow
    }
}