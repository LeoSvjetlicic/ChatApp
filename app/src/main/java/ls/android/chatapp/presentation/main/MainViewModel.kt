package ls.android.chatapp.presentation.main

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ls.android.chatapp.data.repository.real.ConnectionRepositoryImpl
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repositoryImpl: ConnectionRepositoryImpl,
) : ViewModel() {
    var isChatVisible = mutableStateOf(false)
    private var lastIncrementValue = mutableIntStateOf(0)
    var maxX = mutableFloatStateOf(0f)
    var maxY = mutableFloatStateOf(0f)

    fun updateConnections(connectionId: String) {
        viewModelScope.launch {
            if (connectionId.isNotBlank()) {
                if (isChatVisible.value) {
                    if (lastIncrementValue.intValue != 1) {
                        repositoryImpl.updateConnections(connectionId, true)
                        lastIncrementValue.intValue = 1
                    }
                } else {
                    if (lastIncrementValue.intValue != -1) {
                        repositoryImpl.updateConnections(connectionId, false)
                        lastIncrementValue.intValue = -1
                    }
                }
            }
        }
    }
}
