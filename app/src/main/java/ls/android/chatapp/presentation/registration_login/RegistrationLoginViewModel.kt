package ls.android.chatapp.presentation.registration_login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegistrationLoginViewModel @Inject constructor() : ViewModel() {
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

    fun onContinueClick() {
//        todo
    }
}