package ls.android.chatapp

import android.app.Application
import ls.android.chatapp.common.SharedPreferencesHelper

class GhostThread : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesHelper.init(this)
    }
}