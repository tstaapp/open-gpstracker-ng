package nl.sogeti.android.gpstracker.ng.dagger

import android.net.Uri
import dagger.Module
import dagger.Provides
import nl.sogeti.android.gpstracker.ng.common.controllers.GpsStatusControllerProvider
import java.util.*

@Module
class SystemModule {

    @Provides
    fun locale(): Locale {
        return Locale.getDefault()
    }

    @Provides
    fun gpsStatusControllerProvider(): GpsStatusControllerProvider {
        return GpsStatusControllerProvider()
    }

    @Provides
    fun uriBuilder() = Uri.Builder()
}