package org.hfathi.bugloos

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import org.hfathi.bugloos.settings.SettingsManager

@Suppress("UNUSED")
class BugloosApp : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()

        // Init SettingsManager here so that there aren't any race conditions
        // [e.g PlaybackService gets SettingsManager before activity can init SettingsManager]
        SettingsManager.init(applicationContext)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(applicationContext)
            .diskCachePolicy(CachePolicy.DISABLED) // Not downloading anything, so no disk-caching
            .crossfade(true)
            .placeholder(android.R.color.transparent)
            .build()
    }
}
