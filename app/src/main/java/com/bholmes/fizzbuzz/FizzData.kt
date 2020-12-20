package com.bholmes.fizzbuzz

data class FizzData(var id: Int, var image: FizzImage, var title: String, var media: Boolean){
    inner class FizzImage(var width: Int, var height: Int, var url: String)
}