package com.kolo.gorskih.tica.imagine

import com.google.firebase.storage.StorageReference

data class MainUI(val images: List<Image>)

data class Image(val imageId: Int, val imageTitle: String, val imageURL: StorageReference)