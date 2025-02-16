package net.runner.fitbit

import android.content.Context
import android.util.Log
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult

class ImageCaching {
    val listener = object : ImageRequest.Listener {
        override fun onError(request: ImageRequest, result: ErrorResult) {
            super.onError(request, result)
        }
        override fun onSuccess(request: ImageRequest, result: SuccessResult) {
            Log.d("CACHE","Loadedfrom:${result.dataSource}")
        }
    }

    fun CacheBuilder(context: Context, imageUrl: String): ImageRequest.Builder {
        return ImageRequest.Builder(context)
            .data(imageUrl)
            .listener(listener)
            .memoryCacheKey(imageUrl.substringBefore("?"))
            .diskCacheKey(imageUrl.substringBefore("?"))
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .setHeader("Cache-Control","max-stale=31536000")
    }

}