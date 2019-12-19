package com.akjaw.presentation.base

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParcelableWikiArticle(
    val name: String,
    val description: String,
    val imageUrl: String
): Parcelable