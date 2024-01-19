package ls.android.chatapp.presentation.registration_login.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ls.android.chatapp.presentation.ui.IceBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputText(
    modifier: Modifier,
    value: String,
    hint: String,
    isPassword: Boolean,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        value = value,
        onValueChange = {
            onValueChanged(it)
        },
        placeholder = { Text(text = hint) },
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 16.sp
        ),
        shape = RoundedCornerShape(10.dp),
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = IceBlue,
            unfocusedIndicatorColor = IceBlue,
            focusedLabelColor = IceBlue,
            unfocusedLabelColor = IceBlue,
            cursorColor = IceBlue
        )
    )
}