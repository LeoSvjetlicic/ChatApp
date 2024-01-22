package ls.android.chatapp.presentation.navigation

import ls.android.chatapp.common.Constants

object ChatRoute : AppRoute(Constants.CHAT_WITH_ID) {
    fun createNavigationRoute(id: String): String = "${Constants.CHAT_ROUTE}/$id"
}