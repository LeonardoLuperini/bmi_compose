package it.unipi.di.sam.m634318.bmi_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import it.unipi.di.sam.m634318.bmi_compose.ui.theme.Bmi_composeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StartPage()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartPage() {
    var height by remember { mutableStateOf("")}
    var weight by remember { mutableStateOf("")}
    var bmi by remember { mutableStateOf("25.5") }
    Bmi_composeTheme{
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Text("BMI")

                TextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = {Text("height")},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text(bmi)
            }
        }
    }
}

@Composable
fun TextInputMUnit(text: String, measurementUnit: String, label: String = "") {
    Row {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = {Text(label)},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(measurementUnit)
    }
}