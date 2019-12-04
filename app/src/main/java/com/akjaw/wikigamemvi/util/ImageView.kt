package com.akjaw.wikigamemvi.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions

fun ImageView.glideLoadImage(
    url: String,
//    placeholder: Drawable? = null,
    onLoadingFinished: () -> Unit){

    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
            onLoadingFinished()
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            onLoadingFinished()
            return false
        }
    }
    Glide.with(this)
        .load(url)
        .apply(RequestOptions()
            .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
            .format(DecodeFormat.PREFER_ARGB_8888))
//        .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
        .listener(listener)
        .into(this)


}