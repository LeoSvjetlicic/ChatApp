package ls.android.chatapp.presentation.connections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ls.android.chatapp.R
import ls.android.chatapp.common.components.ActionButton
import ls.android.chatapp.presentation.connections.components.ConnectionItem

@Composable
fun ConnectionRoute(
    modifier: Modifier = Modifier,
    viewModel: ConnectionsViewModel,
    onItemClick: (String) -> Unit,
    onAddButtonClick: () -> Unit
) {
    val connections by viewModel.connections.collectAsState(initial = ConnectionScreenState())
    ConnectionsScreen(
        modifier = modifier,
        connectionScreenState = connections,
        onItemClick = { onItemClick(it) },
        onAddButtonClick = { onAddButtonClick() }
    )
}

@Composable
fun ConnectionsScreen(
    modifier: Modifier = Modifier,
    connectionScreenState: ConnectionScreenState,
    onItemClick: (String) -> Unit,
    onAddButtonClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .clickable(interactionSource = interactionSource, indication = null) {
                focusManager.clearFocus()
            }) {
        val (text, connections, button) = createRefs()

        Text(
            modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(vertical = 16.dp),
            text = "Your connections",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

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
                    onItemClick = { onItemClick(it) })
            }
        }

        ActionButton(
            modifier = Modifier
                .size(50.dp)
                .constrainAs(button) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                }, imageId = R.drawable.ic_add
        ) {
            onAddButtonClick()
        }
    }
}