package com.kolo.gorskih.tica.imagine

data class MainUI(val images : List<Image>)

data class Image(val imageId : Int, val imageTitle : String, val imageURL : String )