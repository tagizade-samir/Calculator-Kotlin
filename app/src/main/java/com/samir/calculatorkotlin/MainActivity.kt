package com.samir.calculatorkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.pow
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    lateinit var label: TextView
    lateinit var errorLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        label = findViewById(R.id.labelResult)
        errorLabel = findViewById(R.id.labelError)
    }

        fun IsDelimeter(x: String) : Boolean{
            if(x == "="){
                return true
            }
            return false
        }

        fun IsOperator(x: String) : Boolean{
            when (x) {
                "+" -> return true
                "-" -> return true
                "/" -> return true
                "*" -> return true
                "^" -> return true
                else -> {
                    return false
                }
            }
        }

        fun IsBracket(x: String) : Boolean {
            when (x) {
                "(" -> return true
                ")" -> return true
                else -> {
                    return false
                }
            }
        }

        fun GetPriority(x: String) : Int{
            when (x) {
                "(" -> return 0
                ")" -> return 1
                "+" -> return 2
                "-" -> return 2
                "*" -> return 4
                "/" -> return 4
                "^" -> return 5
                else -> {
                    return 6
                }
            }
        }

        fun IsDigit(x: String) : Boolean {
            when (x) {
                "1" -> return true
                "2" -> return true
                "3" -> return true
                "4" -> return true
                "5" -> return true
                "6" -> return true
                "7" -> return true
                "8" -> return true
                "9" -> return true
                "0" -> return true
                else -> {
                    return false
                }
            }
        }

        fun IsNumber(x: String): Boolean =
            try {
                x.toDouble()
                true
            } catch(e: NumberFormatException) {
                false
            }

        fun IsDoubleOrInt(x: String) : String {
            val enterArray = x.toCharArray()
            var count: Int = 0
            val len: Int = x.count()
            var equation:MutableList<String> = mutableListOf()
            var result: String = ""
            var number: String = ""
            var char: String
            for (i in enterArray){
                if(IsDigit(i.toString())){
                    number += i
                }else{
                    char = i.toString()
                    if(number != ""){
                        equation.add(number)
                    }
                    equation.add(char)
                    number = ""
                    char = ""
                }
                if(count == len - 1 && number != ""){
                    equation.add(number)
                }
                count += 1
            }

            if(equation.last() == "0"){
                for (j in equation) {
                    if(j == "."){
                        break
                    }
                    result += j
                }
            }else{
                for (j in equation) {
                    result += j
                }
            }

            return result
        }

        fun Arrange(x: String) : MutableList<String> {
            var count = 0
            val enterArray = x.toCharArray()
            var equation: MutableList<String> = mutableListOf()
            var number: String = ""
            var char: String
            val len = enterArray.count()
            val dot = ".".single()

            for (i in enterArray){
                count += 1
                if(IsDigit(i.toString()) || i == dot){
//             if(IsDigit(i.toString())){
                    number += i
                }else{
                    char = i.toString()
                    if(number != ""){
                        equation.add(number)
                    }
                    equation.add(char)
                    number = ""
                    char = ""
                }
                if(count == len && number != ""){
                    equation.add(number)
                }
            }
            return equation
        }

        fun GetExpression(x: MutableList<String> ) : MutableList<String> {
            var result: MutableList<String> = mutableListOf()
            var operStack: MutableList<String> = mutableListOf()
            var count = 0
            val len = x.count()
            while (count <  len){
                if(IsDelimeter(x[count])){
                    break
                }
                if(IsNumber(x[count])){
                    if( !IsDelimeter(x[count]) && !IsOperator(x[count])) {
                        result.add(x[count])
                        if(count == len){
                            break
                        }
                    }
                }
                if(IsOperator(x[count]) || IsBracket(x[count])){

                    if(x[count] == "("){
//                         operStack.insert(x[count], at: 0)
                        operStack.add(0, x[count])
                    } else if (x[count] == ")"){
                        var operators  = operStack[0]

                        while (operators != "(") {
                            result.add(operators)
                            if(operStack.count() > 0){
                                operStack.removeAt(0)
                                operators = operStack[0]
                            } else {
                                operators = ""
                            }
                            if(operStack[0] == "("){
                                if(operStack.count() > 0){
                                    operStack.removeAt(0)
                                }
                            }
                        }
                    }else{
                        if(operStack.count() > 0){
                            if(GetPriority(x[count]) <= GetPriority(operStack[0])){
                                while (operStack.count() > 0){
                                    result.add(operStack[0])
                                    operStack.removeAt(0)
                                }
                                operStack.add(0, x[count])
                            } else {
                                operStack.add(0, x[count])
                            }
                        } else {
                            operStack.add(0, x[count])
                        }
                    }

                }
                count += 1
            }

            while (operStack.count() > 0) {
                if(operStack[0] != "(" && operStack[0] != ")"){
                    result.add(operStack[0])
                    operStack.removeAt(0)
                }else{
                    operStack.removeAt(0)
                }
            }
            return result
        }

        fun Counting(x: MutableList<String> ) : String{
            var math: Double
            var result: String
            var temp: MutableList<String> = mutableListOf()
            var count = 0
            val len = x.count()

            if(!x.contains("+") && !x.contains("-") && !x.contains("/") && !x.contains("*") && !x.contains("^")){
                result = x[0]
                return result
            }

            while (count < len) {
                if(IsNumber(x[count])){
//                var y: Array<String>
                    if (!IsDelimeter(x[count]) && !IsOperator(x[count]) && !IsBracket(x[count])) {
                        temp.add(0, x[count])
                        if(count == len){
                            break
                        }
                    }

                } else if(IsOperator(x[count]) || IsBracket(x[count])) {
                    val a: Double = temp[0].toDouble()
                    temp.removeAt(0)
                    val b: Double = temp[0].toDouble()
                    temp.removeAt(0)

//                if(temp.count > 0){
//                    a = Double(temp[0])!
//                    temp.remove(at: 0)
//                }
//
//                if(temp.count > 0){
//                    b = Double(temp[0])!
//                    temp.remove(at: 0)
//                }

                    when (x[count]) {
                        "+" -> math = b + a
                        "-" -> math = b - a
                        "*" -> math = b * a
                        "/" -> math = b / a
                        "^" -> math = b.pow(a)
                        else -> {
                            math = 0.0
                        }
                    }
                    // var sss = String(math)
                    temp.add(0, math.toString())
                }
                count += 1
            }

            result = temp[0]

            return result
        }

        fun Calculate (x: String ) : String{
            val arranged: MutableList<String> = Arrange(x)
            val polishExpression: MutableList<String> = GetExpression(arranged)
            val counted: String = Counting(polishExpression)
            val finalResult: String = IsDoubleOrInt(counted)
            return finalResult
        }


    var resulEquation: String = ""
    var tempEquation: String = ""
    var errorText: String = ""
    var secondResult: String = ""
    var IsResult: Boolean = false
    var tempNumber: String = ""

    fun number0(View: View){
        if(tempNumber == "/"){
            tempNumber = "0."
            tempEquation = "0."
            resulEquation += "0."
            label.text = resulEquation
            return
        }else if (tempNumber == ")"){
            errorText = "You can not write number after ')' symbol"
            errorLabel.text = errorText
            return
        }else if (tempNumber == "0" && resulEquation == "0"){
            errorText = "You can not write zero after zero"
            errorLabel.text = errorText
            return
        }else if(tempNumber == ""){
            tempNumber = "0."
            tempEquation = "0."
            resulEquation += "0."
            label.text = resulEquation
            return
        }
        if(IsResult == true){
            resulEquation = ""
            label.text = resulEquation
            IsResult = false
        }
        tempEquation += "0"
        tempNumber = "0"
        resulEquation += "0"
        label.text = resulEquation
    }

    fun number1(View: View){
        if(tempNumber == ")"){
            errorText = "You can not write number after ')' symbol"
            errorLabel.text = errorText
            return
        }
        if(IsResult == true){
            resulEquation = ""
            label.text = resulEquation
            IsResult = false
        }
        tempEquation += "1"
        tempNumber = "1"
        resulEquation += "1"
        label.text = resulEquation
    }

    fun number2(View: View){
        if(tempNumber == ")"){
            errorText = "You can not write number after ')' symbol"
            errorLabel.text = errorText
            return
        }
        if(IsResult == true){
            resulEquation = ""
            label.text = resulEquation
            IsResult = false
        }
        tempEquation += "2"
        tempNumber = "2"
        resulEquation += "2"
        label.text = resulEquation
    }

    fun number3(View: View){
        if(tempNumber == ")"){
            errorText = "You can not write number after ')' symbol"
            errorLabel.text = errorText
            return
        }
        if(IsResult == true){
            resulEquation = ""
            label.text = resulEquation
            IsResult = false
        }
        tempEquation += "3"
        tempNumber = "3"
        resulEquation += "3"
        label.text = resulEquation
    }

    fun number4(View: View){
        if(tempNumber == ")"){
            errorText = "You can not write number after ')' symbol"
            errorLabel.text = errorText
            return
        }
        if(IsResult == true){
            resulEquation = ""
            label.text = resulEquation
            IsResult = false
        }
        tempEquation += "4"
        tempNumber = "4"
        resulEquation += "4"
        label.text = resulEquation
    }

    fun number5(View: View){
        if(tempNumber == ")"){
            errorText = "You can not write number after ')' symbol"
            errorLabel.text = errorText
            return
        }
        if(IsResult == true){
            resulEquation = ""
            label.text = resulEquation
            IsResult = false
        }
        tempEquation += "5"
        tempNumber = "5"
        resulEquation += "5"
        label.text = resulEquation
    }

    fun number6(View: View){
        if(tempNumber == ")"){
            errorText = "You can not write number after ')' symbol"
            errorLabel.text = errorText
            return
        }
        if(IsResult == true){
            resulEquation = ""
            label.text = resulEquation
            IsResult = false
        }
        tempEquation += "6"
        tempNumber = "6"
        resulEquation += "6"
        label.text = resulEquation
    }

    fun number7(View: View){
        if(tempNumber == ")"){
            errorText = "You can not write number after ')' symbol"
            errorLabel.text = errorText
            return
        }
        if(IsResult == true){
            resulEquation = ""
            label.text = resulEquation
            IsResult = false
        }
        tempEquation += "7"
        tempNumber = "7"
        resulEquation += "7"
        label.text = resulEquation
    }

    fun number8(View: View){
        if(tempNumber == ")"){
            errorText = "You can not write number after ')' symbol"
            errorLabel.text = errorText
            return
        }
        if(IsResult == true){
            resulEquation = ""
            label.text = resulEquation
            IsResult = false
        }
        tempEquation += "8"
        tempNumber = "8"
        resulEquation += "8"
        label.text = resulEquation
    }

    fun number9(View: View){
        if(tempNumber == ")"){
            errorText = "You can not write number after ')' symbol"
            errorLabel.text = errorText
            return
        }
        if(IsResult == true){
            resulEquation = ""
            label.text = resulEquation
            IsResult = false
        }
        tempEquation += "9"
        tempNumber = "9"
        resulEquation += "9"
        label.text = resulEquation
    }

    fun plusBtn(View: View){
        if(IsOperator(tempNumber)){
            resulEquation = resulEquation.dropLast(1)
            resulEquation += "+"
            label.text = resulEquation
            tempNumber = "+"
            return
        }else if(tempNumber == "("){
            errorText = "You can not write an operator after '(' symbol"
            errorLabel.text = errorText
            return
        }else if(tempNumber == "."){
            errorText = "you can not write an operator after dot"
            errorLabel.text = errorText
            return
        }

        tempEquation = ""
        tempNumber = "+"
        resulEquation += "+"
        label.text = resulEquation
        IsResult = false
    }

    fun minusBtn(View: View){
        if(IsOperator(tempNumber)){
            resulEquation = resulEquation.dropLast(1)
            resulEquation += "-"
            label.text = resulEquation
            tempNumber = "-"
            return
        }else if(tempNumber == "("){
            errorText = "You can not write an operator after '(' symbol"
            errorLabel.text = errorText
            return
        }else if(tempNumber == "."){
            errorText = "you can not write an operator after dot"
            errorLabel.text = errorText
            return
        }

        tempEquation = ""
        tempNumber = "-"
        resulEquation += "-"
        label.text = resulEquation
        IsResult = false
    }

    fun multiplyBtn(View: View){
        if(IsOperator(tempNumber)){
            resulEquation = resulEquation.dropLast(1)
            resulEquation += "*"
            label.text = resulEquation
            tempNumber = "*"
            return
        }else if(tempNumber == "("){
            errorText = "You can not write an operator after '(' symbol"
            errorLabel.text = errorText
            return
        }else if(tempNumber == "."){
            errorText = "you can not write an operator after dot"
            errorLabel.text = errorText
            return
        }

        tempEquation = ""
        tempNumber = "*"
        resulEquation += "*"
        label.text = resulEquation
        IsResult = false
    }

    fun divideBtn(View: View){
        if(IsOperator(tempNumber)){
            resulEquation = resulEquation.dropLast(1)
            resulEquation += "/"
            label.text = resulEquation
            tempNumber = "/"
            return
        }else if(tempNumber == "("){
            errorText = "You can not write an operator after '(' symbol"
            errorLabel.text = errorText
            return
        }else if(tempNumber == "."){
            errorText = "you can not write an operator after dot"
            errorLabel.text = errorText
            return
        }

        tempEquation = ""
        tempNumber = "/"
        resulEquation += "/"
        label.text = resulEquation
        IsResult = false
    }

    fun pow(View: View){
        if(IsOperator(tempNumber)){
            resulEquation = resulEquation.dropLast(1)
            resulEquation += "^"
            label.text = resulEquation
            tempNumber = "^"
            return
        }else if(IsBracket(tempEquation)){
            errorText = "You can not write pow after bracket symbol"
            errorLabel.text = errorText
            return
        }else if(tempNumber == "."){
            errorText = "you can not write an operator after dot"
            errorLabel.text = errorText
            return
        }

        tempEquation = ""
        tempNumber = "^"
        resulEquation += "^"
        label.text = resulEquation
        IsResult = false
    }

    fun equal(View: View){
        if(IsOperator(tempNumber)){
            errorText = "You can not calculate after an operator"
            errorLabel.text = errorText
            return
        }else if(tempNumber == "."){
            errorText = "You can not calculate after dot"
            errorLabel.text = errorText
            return
        }else if(tempNumber == "("){
            errorText = "You can not calculate after '(' symbol"
            errorLabel.text = errorText
            return
        }

        resulEquation = Calculate(resulEquation)
        label.text = resulEquation
        resulEquation = ""
        tempNumber = resulEquation
        IsResult = true
        return
    }

    fun dot(View: View){
        if(IsResult == true){
            resulEquation = ""
            label.text = resulEquation
            IsResult = false
        }

        if(tempEquation.contains(".")){
            errorText = "You can only write one dot in number"
            errorLabel.text = errorText
            return
        }

        if(!IsNumber(tempNumber)){
            tempEquation = "0."
            tempNumber = "0."
            resulEquation += "0."
            label.text = resulEquation
            return
        } else if(tempNumber == ""){
            errorText = "You can add dot only to numbers"
            errorLabel.text = errorText
            return
        }
        tempEquation += "."
        tempNumber = "."
        resulEquation += "."
        label.text = resulEquation
    }

    fun clear(View: View){
        tempEquation = ""
        tempNumber = ""
        resulEquation = ""
        label.text = resulEquation
        IsResult = false
    }

    fun deleteOne(View: View){
        if(tempEquation.count() > 1){
            tempEquation = tempEquation.dropLast(1)
        }else if(tempEquation.count() == 1){
            tempEquation = ""
        }
        if(resulEquation.count() > 1){
            resulEquation = resulEquation.dropLast(1)
        } else if(resulEquation.count() == 1){
            resulEquation = ""
        }

        if(resulEquation.count() == 0){
            tempNumber = ""
        }else{
            tempNumber = resulEquation.last().toString()
        }
        label.text = resulEquation
        IsResult = false
    }


}
