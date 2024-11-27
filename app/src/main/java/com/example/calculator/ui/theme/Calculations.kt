package com.example.calculator.ui.theme

class Calculations (
    var number1 : Int = 0,
    var number2 : Int = 0,
    var operation : Char
) {
    fun calculate(): Int {
        return when(operation) {
            '+' -> number1 + number2
            '-' -> number1 - number2
            '*' -> number1 * number2
            '/' -> number1 / number2
            '%' -> number1 % number2
            else -> {0}
        }

    }
}