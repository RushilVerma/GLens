package com.techcravers.glens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun DraggableResizableBox() {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var size by remember { mutableStateOf(Size(200f, 200f)) }
    val SENSITIVITY = 0.5f

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.test),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.toInt(), offsetY.toInt()) }
                .size(width = size.width.dp, height = size.height.dp)
                .background(Color(0x66FF0000))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { },
                        onDragEnd = { },
                        onDragCancel = { },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    )
                }
        ) {
            // Add resize handlers at corners if needed
            ResizeHandle(
                Modifier.align(Alignment.TopStart),
                onResize = { dw, dh ->
                    size = Size(
                        width = max(50f, size.width - dw),
                        height = max(50f, size.height - dh)
                    )
                    offsetX += dw
                    offsetY += dh
                }
            )

            ResizeHandle(
                Modifier.align(Alignment.TopEnd),
                onResize = { dw, dh ->
                    size = Size(
                        width = max(50f, size.width + dw),
                        height = max(50f, size.height - dh)
                    )
                    offsetY += dh
                }
            )

            ResizeHandle(
                Modifier.align(Alignment.BottomStart),
                onResize = { dw, dh ->
                    size = Size(
                        width = max(50f, size.width - dw),
                        height = max(50f, size.height + dh)
                    )
                    offsetX += dw
                }
            )

            ResizeHandle(
                Modifier.align(Alignment.BottomEnd),
                onResize = { dw, dh ->
                    size = Size(
                        width = max(50f, size.width + dw),
                        height = max(50f, size.height + dh)
                    )
                }
            )
        }
    }
}

@Composable
fun ResizeHandle(
    modifier: Modifier,
    onResize: (Float, Float) -> Unit
){
    Box(
        modifier = modifier
            .size(16.dp)
            .background(Color.White)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onResize(dragAmount.x,dragAmount.y)
                }
            }
    )
}

@Composable
fun Float.toDp() = (this / androidx.compose.ui.platform.LocalDensity.current.density).dp
