package ls.android.chatapp.presentation.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ls.android.chatapp.common.Constants
import ls.android.chatapp.presentation.chat.ChatRoute
import ls.android.chatapp.presentation.chat.ChatViewModel
import ls.android.chatapp.presentation.common.components.TopBar
import ls.android.chatapp.presentation.connections.ConnectionRoute
import ls.android.chatapp.presentation.connections.ConnectionsViewModel
import ls.android.chatapp.presentation.navigation.ChatRoute
import ls.android.chatapp.presentation.qr_code.QRCodeRoute
import ls.android.chatapp.presentation.registration_login.RegistrationLoginRoute

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier,
    onAddButtonClick: () -> Unit,
    onShowButtonClick: () -> Unit,
    setConnectionViewModel: (ConnectionsViewModel) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showTopBar by remember {
        derivedStateOf {
            when (navBackStackEntry?.destination?.route) {
                Constants.REGISTRATION_LOGIN_ROUTE -> false
                Constants.CONNECTIONS_ROUTE -> false
                else -> true
            }
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            if (showTopBar) {
                TopBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) { navController.popBackStack() }
            }
        }
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Constants.REGISTRATION_LOGIN_ROUTE,
                modifier = Modifier.fillMaxSize()
            ) {

                composable(Constants.REGISTRATION_LOGIN_ROUTE) {
                    RegistrationLoginRoute(viewModel = hiltViewModel(), onButtonClick = {
                        navController.navigate(
                            Constants.CONNECTIONS_ROUTE,
                            builder = {
                                popUpTo(Constants.REGISTRATION_LOGIN_ROUTE) {
                                    inclusive = true
                                }
                            }
                        )
                    })
                }

                composable(Constants.CONNECTIONS_ROUTE) {
                    val viewModel = hiltViewModel<ConnectionsViewModel>()
                    setConnectionViewModel(viewModel)
                    ConnectionRoute(
                        modifier = Modifier.padding(16.dp),
                        viewModel = viewModel,
                        onItemClick = {
                            navController.navigate(ChatRoute.createNavigationRoute(it))
                        },
                        onAddButtonClick = { onAddButtonClick() },
                        onShowQRCodeButtonClick = {
                            onShowButtonClick()
                            navController.navigate(Constants.QR_CODE_ROUTE)
                        })
                }

                composable(Constants.QR_CODE_ROUTE) {
                    QRCodeRoute(viewModel = hiltViewModel())
                }

                composable(
                    route = ChatRoute.route,
                    arguments = listOf(navArgument(Constants.CHAT_ID) { type = NavType.StringType })
                ) { navBackStackEntry ->
                    val connectionId = navBackStackEntry.arguments?.getString(Constants.CHAT_ID)
                    val chatViewModel =
                        hiltViewModel<ChatViewModel, ChatViewModel.ChatViewModelFactory> { factory: ChatViewModel.ChatViewModelFactory ->
                            factory.create(connectionId)
                        }
                    ChatRoute(chatViewModel = chatViewModel)
                }
            }
        }
    }
}