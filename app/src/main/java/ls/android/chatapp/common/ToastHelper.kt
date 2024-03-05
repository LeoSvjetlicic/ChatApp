package ls.android.chatapp.common

import android.content.Context
import android.widget.Toast
import javax.inject.Inject

class ToastHelper @Inject constructor(
    private val context: Context
) {
    fun createToast(text: String, length: Int) {
        Toast.makeText(context, text, length).show()
    }
}
