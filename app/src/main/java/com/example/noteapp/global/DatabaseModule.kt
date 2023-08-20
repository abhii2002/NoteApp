package com.example.noteapp.global

import android.content.Context
import androidx.room.Room
import com.example.noteapp.data.NoteDatabase
import com.example.noteapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
In this module, we need to describe how we want to provide instances of certain types, which we want to
inject later

Sometimes we can not construct or inject a certain type for example we can not construct or inject an interface
or third party libraries such as retrofit, ok , http , room

In our case we dont own room therefore, we need to create a module and we need to describe how to provide
instance of our own database to our application.

Thats  why we are going to inject our own interface, which provides annotation from our hilt module. But in order
to inject our dao first  we need to describe how to provide room database builder
 */


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // function which tells how to provide instance to our room database
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(context, NoteDatabase::class.java, Constants.DATABASE_NAME).build()

    //function to provide dao interface

    @Provides
    fun providesNoteDao(database: NoteDatabase) = database.noteDao()


}