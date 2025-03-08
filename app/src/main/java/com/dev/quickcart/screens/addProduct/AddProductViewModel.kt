package com.dev.quickcart.screens.addProduct

import androidx.lifecycle.ViewModel
import androidx.room.Insert
import com.dev.quickcart.data.DataDao
import com.dev.quickcart.data.repository.DataRepository
import com.dev.quickcart.screens.home.HomeInterActor
import com.dev.quickcart.screens.login.login_screen.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class AddProductViewModel
@Inject
constructor(
    private val dataRepository: DataRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(AddProductUiState())
    val uiState = _uiState.asStateFlow()


    val interActor = object : AddProductInterActor {
        override fun updateProdName(it: String) {
            _uiState.value = _uiState.value.copy(productName = it , productNameError = "")
        }


    }


//    suspend fun saveNote(){
//            dataRepository.upsertProduct()
//    }

}