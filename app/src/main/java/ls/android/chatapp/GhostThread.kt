package ls.android.chatapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ls.android.chatapp.common.SharedPreferencesHelper

@HiltAndroidApp
class GhostThread : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesHelper.init(this)
    }
}