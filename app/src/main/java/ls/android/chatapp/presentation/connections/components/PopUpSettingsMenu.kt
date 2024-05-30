package ls.android.chatapp.presentation.connections.components

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties

@Composable
fun CustomPopup(
    popupState: PopupState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable ColumnScope.() -> Unit
) {
    val expandedState = remember {
        MutableTransitionState(false)
    }
    expandedState.targetState = popupState.isVisible
    if (expandedState.currentState || expandedState.targetState) {
        val density = LocalDensity.current
        val popupPositionProvider = PopUpSettingsMenuProvider(
            contentOffset = offset,
            density = density
        ) { alignment, isTop ->
            popupState.horizontalAlignment = alignment
            popupState.isTop = isTop
        }
        Popup(
            onDismissRequest = onDismissRequest,
            popupPositionProvider = popupPositionProvider,
            properties = properties
        ) {
            PopupContent(
                expandedStates = expandedState,
                transformOrigin = TransformOrigin(pivotFractionX = 1f, pivotFractionY = 0f),
                modifier = modifier,
                content = content
            )
        }
    }
}

@Stable
class PopupState(
    isVisible: Boolean = false
) {
    var horizontalAlignment: Alignment.Horizontal by mutableStateOf(Alignment.CenterHorizontally)
    var isTop: Boolean by mutableStateOf(false)
    var isVisible: Boolean by mutableStateOf(isVisible)
}

private data class PopUpSettingsMenuProvider(
    val contentOffset: DpOffset,
    val density: Density,
    val onPopupPositionFound: (Alignment.Horizontal, Boolean) -> Unit
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val contentOffsetX = with(density) { contentOffset.x.roundToPx() }
        val contentOffsetY = with(density) { contentOffset.y.roundToPx() }
        val xOffset = anchorBounds.left + contentOffsetX
        val yOffset = anchorBounds.bottom + contentOffsetY
        onPopupPositionFound(Alignment.Start, true)

        return IntOffset(xOffset, yOffset)
    }
}

@Composable
private fun PopupContent(
    expandedStates: MutableTransitionState<Boolean>,
    transformOrigin: TransformOrigin,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val transition = updateTransition(targetState = expandedStates, "Popup")
    val scale by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 200)
        },
        label = "Popup scale"
    ) {
        if (it.targetState) {
            1f
        } else {
            0f
        }
    }

    fun GraphicsLayerScope.graphicsLayerAnim() {
        scaleX = scale
        scaleY = scale
        this.transformOrigin = transformOrigin
    }
    Box(modifier = modifier.graphicsLayer {
        graphicsLayerAnim()
    }) {
        Column(content = content)
    }
}
