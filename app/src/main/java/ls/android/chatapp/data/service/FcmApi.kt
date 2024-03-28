package ls.android.chatapp.data.service

import ls.android.chatapp.domain.model.SendMessageDto
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {

    @POST("/firebase/notification")
    suspend fun sendMessage(
        @Body body: SendMessageDto
    )
}
