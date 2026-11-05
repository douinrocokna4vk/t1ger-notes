package com.Telqopu0.Kitdag.presentation.app

import androidx.lifecycle.ViewModel
import com.Telqopu0.Kitdag.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = mutableListOf<Screen>()

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _canGoBack = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = _canGoBack.asStateFlow()

    fun open(target: Screen) {
        val current = _screen.value
        if (current !is Screen.Splash) backStack.add(current)
        _screen.value = target
        syncBack()
    }

    fun replace(target: Screen) {
        _screen.value = target
        syncBack()
    }

    fun openMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
        syncBack()
    }

    fun back() {
        if (backStack.isEmpty()) return
        val lastIndex = backStack.size - 1
        val previous = backStack[lastIndex]
        backStack.removeAt(lastIndex)
        _screen.value = previous
        syncBack()
    }

    private fun syncBack() {
        _canGoBack.value = backStack.isNotEmpty()
    }
}
