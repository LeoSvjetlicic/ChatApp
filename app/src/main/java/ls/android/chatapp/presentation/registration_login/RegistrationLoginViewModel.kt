package ls.android.chatapp.presentation.registration_login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ls.android.chatapp.domain.repository.AuthenticationRepository
import javax.inject.Inject

@HiltViewModel
class RegistrationLoginViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) : ViewModel() {
    val screenState = mutableStateOf(RegistrationLoginScreenState())
    fun onEmailChange(value: String) {
        screenState.value = screenState.value.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        screenState.value = screenState.value.copy(password = value)
    }

    fun onTextClick() {
        screenState.value = screenState.value.copy(isLogin = screenState.value.isLogin.not())
    }

    fun onContinueClick(navigate: () -> Unit) {
        viewModelScope.launch {
            async {
                if (screenState.value.isLogin) {
                    authenticationRepository.loginUser(
                        screenState.value.email,
                        screenState.value.password,
                        navigate = navigate
                    )
                } else {
                    authenticationRepository.registerUser(
                        screenState.value.email,
                        screenState.value.password,
                        navigate = navigate
                    )
                }
                delay(500)
            }.invokeOnCompletion {
                screenState.value =
                    screenState.value.copy(email = "", password = "", isLogin = true)
            }
        }
    }
}
