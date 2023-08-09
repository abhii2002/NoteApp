package com.example.noteapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.noteapp.preferences.PreferenceStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DataStoreViewModel @Inject constructor(private val preferenceStorage: PreferenceStorage): ViewModel() {
    // saved key as liveData
    val savedKey = preferenceStorage.savedKey().asLiveData()
    fun setSavedKey(key: Boolean){
        viewModelScope.launch {
            preferenceStorage.setSavedKey(key)
        }
    }
}