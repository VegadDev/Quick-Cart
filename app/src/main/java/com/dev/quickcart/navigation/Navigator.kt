package com.dev.quickcart.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class Navigator
@Inject
constructor() : ViewModel() {

    private val _navigation = Channel<NavigationEvent>(Channel.BUFFERED)
    val navigation = _navigation.receiveAsFlow()

    fun navigate(command: NavigationCommand) {
        _navigation.trySend(NavigationEvent(command = command))
    }

}