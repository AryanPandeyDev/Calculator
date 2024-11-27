package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.CalculatorTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                Calculator()
            }
        }
    }
}

fun removeEnd(text : String): String {
    val res = text.substring(0,text.length-1)
    return res
}

fun  num1(str : String): Number? {
    var num1 = ""
    for (i in str) {
        if (i.isDigit() || i == '.') num1 += i else break
    }
    return when {
        num1.contains('.') -> num1.toDoubleOrNull() // If it has a decimal point, treat as Double
        else -> num1.toIntOrNull() // Otherwise, treat as Int
    }
}

fun  num2(str : String): Number? {
    var num2 = ""
    var pos = -1
    for (i in str) {
        if (!i.isDigit() && i != '.'){
            pos = str.indexOf(i)
            break
        }
    }
    if ( pos != -1 && pos+1 < str.length) num2 = str.substring(pos+1)
    return when {
        num2.contains('.') -> num2.toDoubleOrNull() // If it has a decimal point, treat as Double
        else -> num2.toIntOrNull() // Otherwise, treat as Int
    }
}

fun symbol(str: String) : Char {
    for (i in str) {
        if (!i.isDigit() && i != '.') {
            return i
        }
    }
    return ' '
}

fun calculate(number1 : Number?,number2 : Number?,symbol : Char): Number? {
    val num1: Number = when {
        number1 is Double || number2 is Double -> number1?.toDouble() ?: 0.0
        else -> number1?.toInt() ?: 0
    }

    val num2: Number = when {
        number1 is Double || number2 is Double -> number2?.toDouble() ?: 0.0
        else -> number2?.toInt() ?: 0
    }

    return when (symbol) {
        '+' -> if (num1 is Double || num2 is Double) num1.toDouble() + num2.toDouble() else num1.toInt() + num2.toInt()
        '-' -> if (num1 is Double || num2 is Double) num1.toDouble() - num2.toDouble() else num1.toInt() - num2.toInt()
        '*' -> if (num1 is Double || num2 is Double) num1.toDouble() * num2.toDouble() else num1.toInt() * num2.toInt()
        '/' -> {
            if (num2.toDouble() != 0.0) num1.toDouble() / num2.toDouble() else null  // Avoid division by zero
        }
        '%' -> {
            if (num2.toDouble() != 0.0) {
                if (num1 is Double || num2 is Double) num1.toDouble() % num2.toDouble()
                else num1.toInt() % num2.toInt()
            } else null  // Avoid modulo by zero
        }
        else -> number1  // Unsupported operator
    }

}

fun decimal(str: String) : Int{
    return if (num2(str) == null && symbol(str) == ' ') 1 else 2
}


fun containsAllFromList(inputString: String, list: List<String>): Boolean {
    return list.any { it in inputString }  // Check if all items are in the inputString
}



@Composable
fun Calculator(modifier: Modifier = Modifier) {
    val operators = listOf("+","-","/","%","*")
    var textFieldState by remember { mutableStateOf("") }
    Box(modifier = modifier
        .fillMaxSize()
        .background(Color.Black)
        .padding(vertical = 16.dp),
        contentAlignment = Alignment.BottomCenter) {
        TextField(
            value = textFieldState, readOnly = true,
            onValueChange = { newValue ->
                textFieldState = newValue
            },
            textStyle = TextStyle(
                fontSize = 24.sp,
                textAlign = TextAlign.End
            ),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Black, // Background color when not focused
                focusedContainerColor = Color.Black,   // Background color when focused
                unfocusedTextColor = Color.White,      // Text color when not focused
                focusedTextColor = Color.White,        // Text color when focused
                unfocusedIndicatorColor = Color.Black,  // Optional: Indicator color when unfocused
                focusedIndicatorColor = Color.Black
            ),
            modifier = modifier
                .align(Alignment.TopEnd)
                .fillMaxWidth()
                .padding(top = 50.dp)
        )
        Row (
            modifier= Modifier.wrapContentSize(),
            verticalAlignment = Alignment.Bottom
        ){
            Column (
                modifier = Modifier.wrapContentHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom
            ){
                CalculatorButton("AC",18.sp,Color(0xFFFF8C00)){textFieldState = ""}
                CalculatorButton("7"){ textFieldState += it}
                CalculatorButton("4"){ textFieldState += it}
                CalculatorButton("1"){ textFieldState += it}
                CalculatorButton("00",19.sp,color = Color(0xFFFF8C00)){if (textFieldState != "") textFieldState += it }

            }
            Column (
                modifier = Modifier.wrapContentHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom
            ){
                CalculatorButton("%",33.sp,Color(0xFF2E2E2E)){if (textFieldState != "" && !containsAllFromList(textFieldState,operators)) textFieldState += it }
                CalculatorButton("8"){ textFieldState += it}
                CalculatorButton("5"){ textFieldState += it}
                CalculatorButton("2"){ textFieldState += it}
                CalculatorButton("0", color = Color(0xFF2E2E2E)){ textFieldState += it}

            }
            Column (
                modifier = Modifier.wrapContentHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom
            ){
                CalculatorButton("C", color = Color(0xFFFF8C00)){if(textFieldState != "") textFieldState = removeEnd(textFieldState)}
                CalculatorButton("9"){ textFieldState += it}
                CalculatorButton("6"){ textFieldState += it}
                CalculatorButton("3"){ textFieldState += it}
                CalculatorButton(".",color = Color(0xFF2E2E2E)){
                    when (decimal(textFieldState)) {
                        1 -> if (textFieldState != "" && !textFieldState.contains(".")) textFieldState += it
                        2 -> if (!num2(textFieldState).toString().contains(".")) textFieldState += it
                    }

                }

            }
            Column (
                modifier = Modifier.wrapContentHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom
            ){
                CalculatorButton("/",color = Color(0xFF2E2E2E)){if (textFieldState != "" && !containsAllFromList(textFieldState,operators) ) textFieldState += it }
                CalculatorButton("x",color = Color(0xFF2E2E2E)){if (textFieldState != "" && !containsAllFromList(textFieldState,operators)) textFieldState += it }
                CalculatorButton("-",color = Color(0xFF2E2E2E)){if (textFieldState != "" && !containsAllFromList(textFieldState,operators)) textFieldState += it }
                CalculatorButton("+",color = Color(0xFF2E2E2E)){if (textFieldState != "" && !containsAllFromList(textFieldState,operators)) textFieldState += it }
                CalculatorButton("=",color = Color(0xFF2E2E2E)) {textFieldState = calculate(num1(textFieldState),
                    num2(textFieldState), symbol(textFieldState)
                ).toString()}

            }
        }


    }

}



@Composable
fun CalculatorButton(text : String, size: TextUnit = 38.sp, color: Color = Color.DarkGray, modifier: Modifier = Modifier, updateText : (String) -> Unit = {}) {
    Button(
        onClick = {
            if (text == "x") {
                updateText("*")
            }else {
                updateText(text)
            }
        },
        modifier = modifier
            .size(87.dp)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(color),
        shape = CircleShape,


    ) {
        Text(text,
            color = Color.White,
            fontSize = size,
            fontWeight = FontWeight.Bold,
            softWrap = false
        )

    }
}


@Preview(showSystemUi = true)
@Composable
fun GreetingPreview() {
    Calculator()
}