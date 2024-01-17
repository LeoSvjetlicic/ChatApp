package ls.android.chatapp.presentation.connections

import ls.android.chatapp.domain.model.Connection

data class ConnectionScreenState(
    val connections:List<Connection> = listOf()
)