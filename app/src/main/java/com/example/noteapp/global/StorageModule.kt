package com.example.noteapp.global

import com.example.noteapp.preferences.PreferenceImpl
import com.example.noteapp.preferences.PreferenceStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {
    @Binds
    abstract fun bindsPreferenceStorage(preferenceStorageImpl: PreferenceImpl): PreferenceStorage

}