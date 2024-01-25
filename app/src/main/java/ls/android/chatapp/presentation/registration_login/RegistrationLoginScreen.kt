package ls.android.chatapp.presentation.registration_login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ls.android.chatapp.presentation.registration_login.components.InputText
import ls.android.chatapp.presentation.ui.DarkBlue
import ls.android.chatapp.presentation.ui.IceBlue

@Composable
fun RegistrationLoginRoute(viewModel: RegistrationLoginViewModel, onButtonClick: () -> Unit) {
    RegistrationLoginScreen(
        modifier = Modifier.fillMaxSize(),
        screenState = viewModel.screenState.value,
        onEmailChanged = viewModel::onEmailChange,
        onPasswordChanged = viewModel::onPasswordChange,
        onButtonClick = {
            viewModel.onContinueClick(onButtonClick)
        },
        onTextClick = viewModel::onTextClick
    )
}

@Composable
fun RegistrationLoginScreen(
    modifier: Modifier,
    screenState: RegistrationLoginScreenState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onButtonClick: () -> Unit,
    onTextClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .clickable(interactionSource = interactionSource, indication = null) {
                focusManager.clearFocus()
            }
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(top = 60.dp),
            text = if (screenState.isLogin) {
                "Registration"
            } else {
                "Login"
            },
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            InputText(
                modifier = Modifier,
                value = screenState.email,
                hint = "Email",
                isPassword = false,
                onValueChanged = onEmailChanged
            )
            InputText(
                modifier = Modifier.padding(top = 20.dp),
                value = screenState.password,
                hint = "Password",
                isPassword = true,
                onValueChanged = onPasswordChanged
            )
            Button(
                modifier = Modifier.padding(top = 30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkBlue,
                    contentColor = IceBlue
                ),
                onClick = { onButtonClick() }
            ) {
                Text(text = "Continue")
            }
        }
        Row(
            modifier = Modifier
                .padding(bottom = 20.dp)
        ) {
            Text(
                text = if (screenState.isLogin) {
                    "Already have an account?"
                } else {
                    "Don't have an account?"
                },
                fontSize = 11.sp
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { onTextClick() },
                text = "Click here",
                color = DarkBlue,
                fontSize = 11.sp
            )
        }
    }
}