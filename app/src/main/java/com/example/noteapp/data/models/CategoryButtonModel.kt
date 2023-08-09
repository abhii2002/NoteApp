package com.example.noteapp.data.models

class CategoryButtonModel {

    private var name: String? = null
    private var num: Int? = null

    constructor(name: String, num: Int){
         this.name = name
         this.num = num
    }


    fun getName(): String? {
         return name
    }



}