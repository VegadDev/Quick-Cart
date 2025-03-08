package com.dev.quickcart.screens.home

import androidx.lifecycle.ViewModel
import com.dev.quickcart.navigation.AppScreens
import com.dev.quickcart.navigation.NavigationCommand
import com.dev.quickcart.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val navigator: Navigator
) : ViewModel() {


    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()


    val interActor = object : HomeInterActor {
        override fun gotoProfile() {
            navigator.navigate(NavigationCommand.To(AppScreens.AddScreen.route))
        }

        override fun updateSearchInput(it: String) {
            _uiState.value = _uiState.value.copy(searchInput = it , searchInputError = "")
        }

    }

}