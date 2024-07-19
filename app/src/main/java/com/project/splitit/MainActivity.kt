package com.project.splitit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.splitit.components.InputField
import com.project.splitit.ui.theme.SplitItTheme
import com.project.splitit.util.calculateTotalPerPerson
import com.project.splitit.util.calculateTotalTip
import com.project.splitit.util.isValid
import com.project.splitit.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp {
                Column {
                    CreateHeader(tppState = it)
                    MainContent(tppState = it)
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable (MutableState<Double>) -> Unit) {
    val tppState = remember {
        mutableStateOf(0.0)
    }
    SplitItTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Surface(modifier = Modifier
                .padding(innerPadding)) {
                content(tppState)
            }
        }
    }
}

@Composable
fun CreateHeader(tppState: MutableState<Double>) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(15.dp)
        .clip(shape = RoundedCornerShape(12.dp)),
        color = Color(0xFFE9D7F7)
    ) {
        Column(modifier = Modifier
            .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = "Total Per Person",
                color = MaterialTheme.colorScheme.scrim,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineSmall)
            Text(text = "$${"%.2f".format(tppState.value)}",
                color = MaterialTheme.colorScheme.scrim,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineLarge)
        }
    }
}

@Composable
fun MainContent(tppState: MutableState<Double>) {
    BillForm(modifier = Modifier.fillMaxWidth(),
        tppState = tppState)
}

@Composable
fun BillForm(modifier: Modifier, tppState: MutableState<Double>) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.isValid()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val tipState = remember {
        mutableStateOf(0.0)
    }
    val sliderState = remember {
        mutableStateOf(0f)
    }
    val splitAmong = remember {
        mutableStateOf(1)
    }
    Surface(modifier = modifier.padding(10.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 2.dp, color = Color.LightGray)
    ) {
        Column {
            InputField(modifier = modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                valueState = totalBillState,
                onValueChange = { newVal ->
                    if(newVal.isValid()) {
                        tppState.value = calculateTotalPerPerson(totalBill = totalBillState.value.trim().toInt(),
                            tipInFloat = sliderState.value,
                            splitBy = splitAmong.value)
                        tipState.value = calculateTotalTip(totalBill = totalBillState.value.trim().toInt(),
                            tipInFloat = sliderState.value)
                    }
                    else tppState.value = 0.00
                },
                labelId = "Enter Bill",
                isEnabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if(!validState) return@KeyboardActions
                    keyboardController?.hide()
                })
            if(validState)
                ShowTipCalculator(totalBill = totalBillState.value.trim().toInt(),
                    tppState = tppState,
                    tipState = tipState,
                    sliderState = sliderState,
                    splitAmong = splitAmong)
        }
    }
}

@Composable
fun ShowTipCalculator(totalBill: Int,
                      tppState: MutableState<Double>,
                      tipState: MutableState<Double>,
                      sliderState: MutableState<Float>,
                      splitAmong: MutableState<Int>) {
    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Split",
            fontSize = 20.sp,
            style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.width(120.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            RoundIconButton(icon = Icons.Default.Remove,
                backgroundColor = Color(0xFFE9D7F7)) {
                if(splitAmong.value!=1) splitAmong.value--
                tppState.value = calculateTotalPerPerson(totalBill = totalBill,
                    tipInFloat = sliderState.value,
                    splitBy = splitAmong.value)
            }
            Text(text = "${splitAmong.value}",
                modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                fontSize = 20.sp)
            RoundIconButton(icon = Icons.Default.Add,
                backgroundColor = Color(0xFFE9D7F7)) {
                if(splitAmong.value!=7) splitAmong.value++
                tppState.value = calculateTotalPerPerson(totalBill = totalBill,
                    tipInFloat = sliderState.value,
                    splitBy = splitAmong.value)
            }
        }
    }
    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
        Text(text = "Tip",
            fontSize = 20.sp,
            style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.width(195.dp))
        Text(text = "$${tipState.value}",
            fontSize = 20.sp,
            style = MaterialTheme.typography.bodySmall)
    }
    Column(modifier = Modifier.padding(top = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "${(sliderState.value * 100).toInt()}%",
            fontSize = 20.sp,
            style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(10.dp))
        Slider(value = sliderState.value,
            onValueChange = {newVal ->
                sliderState.value = newVal
                tipState.value = calculateTotalTip(totalBill = totalBill,
                    tipInFloat = sliderState.value)
                tppState.value = calculateTotalPerPerson(totalBill = totalBill,
                    tipInFloat = sliderState.value,
                    splitBy = splitAmong.value)
            },
            modifier = Modifier.padding(horizontal = 10.dp),
            colors = SliderDefaults.colors(thumbColor = Color(0xFFE9D7F7)))
    }
}

@Preview(showBackground = true)
@Composable
fun UIPreview() {
    MyApp {
        Column {
            CreateHeader(tppState = it)
            MainContent(tppState = it)
        }
    }
}