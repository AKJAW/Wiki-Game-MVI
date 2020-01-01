package com.akjaw.wikigamemvi.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParcelableWikiArticle(
    val name: String = "",
    val description: String = "",
    val imageUrl: String? = null
): Parcelable