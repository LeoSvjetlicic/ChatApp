package ls.android.chatapp.presentation.registration_login

import androidx.compose.runtime.mutableStateOf

class RegistrationLoginViewModel {
    val screenState = mutableStateOf(RegistrationLoginScreenState("", "", true))
    fun onEmailChange(value: String) {
        screenState.value = screenState.value.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        screenState.value = screenState.value.copy(password = value)
    }

    fun onTextClick() {
        screenState.value = screenState.value.copy(isLogin = screenState.value.isLogin.not())
    }
}