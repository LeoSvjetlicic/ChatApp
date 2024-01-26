package ls.android.chatapp.presentation.registration_login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ls.android.chatapp.domain.repository.AuthenticationRepository
import javax.inject.Inject

@HiltViewModel
class RegistrationLoginViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    init {
        firebaseAuth.signOut()
    }

    val screenState = mutableStateOf(RegistrationLoginScreenState("", "", false))
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
            if (!screenState.value.isLogin) {
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
        }
    }
}