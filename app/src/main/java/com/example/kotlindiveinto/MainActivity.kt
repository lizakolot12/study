package com.example.kotlindiveinto

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.kotlindiveinto.ui.theme.KotlinDiveIntoTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinDiveIntoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Content(modifier = Modifier.padding(innerPadding), value = "10 кг")

                }
            }
        }
    }
}

@Composable
fun Content(value: String, modifier: Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Greeting(
            value = value,
            modifier = Modifier
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Unspecified)
                .height(24.dp)

        )
        GreetingCrossFade(
            value = value,
            modifier = Modifier
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
                .background(Color.Green)
        )
    }
}

@Composable
fun GreetingCrossFade(value: String, modifier: Modifier = Modifier) {
    var positions by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    var done by remember {
        mutableStateOf(false)
    }
    Column(
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { done = false }) {
            Text("RESET")
        }
        Box(modifier = Modifier) {
            Crossfade(
                targetState = done,
                animationSpec = tween(600), label = "crossfade"
            ) { isDone ->
                if (isDone) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.Gray, shape = RoundedCornerShape(4.dp))
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = value, color = Color.White)
                        Text(text = value, color = Color.White)
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.Gray, shape = RoundedCornerShape(4.dp))
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        var animate by remember {
                            mutableStateOf(false)
                        }
                        DragImage(
                            requireX = positions.x,
                            value1 = value,
                            onValueChange = { done = true },
                            animate = animate
                        )
                        DropTargetImage(
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                positions = coordinates.positionInRoot()
                            }
                        ) {
                            animate = true
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun Greeting(value: String, modifier: Modifier = Modifier) {
    var positions by remember {
        mutableStateOf(Offset(0f, 0f))
    }
    var done by remember {
        mutableStateOf(false)
    }
    Column(
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { done = false }) {
            Text("RESET")
        }
        Box(modifier = Modifier) {
            Column {
                AnimatedVisibility(
                    visible = done,
                    enter = fadeIn(animationSpec = tween(500)),
                    exit = fadeOut(animationSpec = tween(500))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.Gray, shape = RoundedCornerShape(4.dp))
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = value,
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        Text(
                            text = value,
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
            Column {
                AnimatedVisibility(
                    visible = !done,
                    enter = fadeIn(animationSpec = tween(500)),
                    exit = fadeOut(animationSpec = tween(500))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.Gray, shape = RoundedCornerShape(4.dp))
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        var animate by remember {
                            mutableStateOf(false)
                        }
                        DragImage(
                            requireX = positions.x,
                            value1 = value,
                            onValueChange = {
                                done = true
                            },
                            animate = animate
                        )
                        DropTargetImage(
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                positions = coordinates.positionInRoot()
                            }
                        ) {
                            animate = true
                        }
                    }

                }
            }
        }

    }
}

@Composable
fun DragImage(value1: String, requireX: Float, onValueChange: () -> Unit, animate: Boolean) {

    Box(modifier = Modifier.zIndex(1f)) {
        var offsetX by remember { mutableFloatStateOf(0f) }
        var size by remember {
            mutableStateOf(IntSize(0, 0))
        }

        LaunchedEffect(animate) {
            if (animate) {
                Animatable(offsetX).animateTo(
                    targetValue = requireX - size.width / 5,
                    animationSpec =  tween(300)
                ) {
                    Log.e("TEST", "value $value")
                    offsetX = value// Оновлення offsetX на кожному кроці
                }
                onValueChange()
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .onGloballyPositioned { coordinates ->
                    size = coordinates.size
                }
                .background(Color.DarkGray, shape = RoundedCornerShape(4.dp))
                .pointerInput(Unit) {
                    detectDragGestures(onDragEnd = {
                        if (offsetX < requireX) {
                            offsetX = 0F
                        }
                    }) { change, dragAmount ->
                        change.consume()
                        Log.e("TEST", " drag amount $dragAmount")
                        offsetX += dragAmount.x
                        if (offsetX + size.width / 2 > requireX) {
                            onValueChange()
                        }
                    }
                }

        ) {
            Text(
                text = value1,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
            )
            Image(
                imageVector = Icons.AutoMirrored.Sharp.KeyboardArrowRight,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White)
            )
        }
    }
}

@Composable
fun DropTargetImage(
    modifier: Modifier,
    onAcceptClicked: () -> Unit
) {
    Text(
        text = "Прийняти".uppercase(),
        color = Color.Green,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .zIndex(0.1f)
            .clickable {
                onAcceptClicked()
            }
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinDiveIntoTheme {
        Content(
            value = "10 кг",
            modifier = Modifier
        )
    }
}

data object CustomIndicationNodeFactory : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return CustomIndicationNode(interactionSource)
    }
}

private class CustomIndicationNode(
    private val interactionSource: InteractionSource,
) : Modifier.Node(), DrawModifierNode {

    private val animatedScaleFactor = Animatable(1f)

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> animatedScaleFactor.animateTo(3.3f)
                    else -> animatedScaleFactor.animateTo(1f)
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        scale(animatedScaleFactor.value) {
            this@draw.drawContent()
        }
    }
}