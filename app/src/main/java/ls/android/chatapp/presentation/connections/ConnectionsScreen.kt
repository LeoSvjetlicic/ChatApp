package ls.android.chatapp.presentation.connections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ls.android.chatapp.R
import ls.android.chatapp.presentation.components.ActionButton
import ls.android.chatapp.presentation.connections.components.ConnectionItem
import ls.android.chatapp.presentation.connections.components.CustomPopup
import ls.android.chatapp.presentation.connections.components.PopupState
import ls.android.chatapp.presentation.connections.components.SettingsDropDownMenuElements
import ls.android.chatapp.presentation.ui.DarkBlue

@Composable
fun ConnectionRoute(
    modifier: Modifier = Modifier,
    viewModel: ConnectionsViewModel,
    onItemClick: (String) -> Unit,
    onAddButtonClick: () -> Unit,
    onDropDownElementClick: (String) -> Unit,
    onShowQRCodeButtonClick: () -> Unit
) {
    val connections by viewModel.connections.collectAsState(initial = ConnectionScreenState())
    ConnectionsScreen(
        modifier = modifier,
        connectionScreenState = connections,
        onItemClick = { onItemClick(it) },
        onAddButtonClick = { onAddButtonClick() },
        onDropDownElementClick = onDropDownElementClick,
        onDeleteItemClick = viewModel::deleteConnection,
        onShowQRCodeButtonClick = { onShowQRCodeButtonClick() }
    )
}

@Composable
fun ConnectionsScreen(
    modifier: Modifier = Modifier,
    connectionScreenState: ConnectionScreenState,
    onItemClick: (String) -> Unit,
    onDeleteItemClick: (String) -> Unit,
    onAddButtonClick: () -> Unit,
    onDropDownElementClick: (String) -> Unit,
    onShowQRCodeButtonClick: () -> Unit,
) {
    val topEndState = remember { PopupState(false) }
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {
        val (text, connections, button) = createRefs()
        val popUp = createRef()
        Row(
            modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your connections",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(DarkBlue)
                    .padding(4.dp)
                    .clickable { topEndState.isVisible = !topEndState.isVisible },
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = null
            )
        }
        Box(
            modifier = Modifier
                .constrainAs(popUp) {
                    top.linkTo(text.bottom)
                    end.linkTo(parent.end)
                }
                .padding(end = 16.dp)
        ) {
                CustomPopup(
                    modifier = Modifier.width(150.dp),
                    popupState = topEndState,
                    onDismissRequest = { topEndState.isVisible = false }) {
                    SettingsDropDownMenuElements(
                        modifier = Modifier.padding(end = 16.dp),
                        elements = listOf("Sign out")
                    ) {
                        onDropDownElementClick(it)
                        topEndState.isVisible = false
                    }
            }
        }
        LazyColumn(modifier = Modifier.constrainAs(connections) {
            top.linkTo(text.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(button.top)
            height = Dimension.fillToConstraints
            width = Dimension.matchParent
        }) {
            items(connectionScreenState.connections) { connection ->
                ConnectionItem(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .height(50.dp),
                    connection = connection,
                    onDeleteClick = onDeleteItemClick,
                    onItemClick = { onItemClick(connection.id) })
            }
        }

        Row(modifier = Modifier
            .height(50.dp)
            .constrainAs(button) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = 16.dp)
                width = Dimension.wrapContent
            }) {
            ActionButton(
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(50.dp),
                imageId = R.drawable.ic_add
            ) {
                onAddButtonClick()
            }
            ActionButton(
                modifier = Modifier.size(50.dp),
                imageId = R.drawable.ic_qr_code
            ) {
                onShowQRCodeButtonClick()
            }
        }
    }
}
