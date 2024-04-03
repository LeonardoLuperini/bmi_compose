package it.unipi.di.sam.m634318.bmi_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
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
    var bmi by remember { mutableStateOf("") }
    Bmi_composeTheme{
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize(),
            )
            {
                val (
                    headline,
                    inputH,
                    inputW,
                    heightField,
                    weightField,
                    button,
                    result
                ) = createRefs()

                val topGL = createGuidelineFromTop(0.1f)
                val botGL = createGuidelineFromBottom(0.1f)
                val staGL = createGuidelineFromStart(0.1f)
                val endGL = createGuidelineFromEnd(0.1f)
                val halfH = createGuidelineFromTop(0.5f)

                Text(
                    text = "BMI",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.constrainAs(headline) {
                        top.linkTo(topGL)
                        start.linkTo(staGL)
                        end.linkTo(endGL)
                    }
                )

                ConstraintLayout (
                    modifier = Modifier.constrainAs(inputH) {
                        top.linkTo(headline.bottom, margin = 30.dp)
                        start.linkTo(headline.start)
                        end.linkTo(headline.end)
                    }
                ){
                    MyTextField(
                        text = height,
                        unit = "m",
                        label = "height",
                        modifier = Modifier.constrainAs(heightField) {
                            end.linkTo(heightField.start)
                        }
                    ) {height = it}
                }

                ConstraintLayout (
                    modifier = Modifier.constrainAs(inputW) {
                        top.linkTo(inputH.bottom, margin = 20.dp)
                        start.linkTo(inputH.start)
                        end.linkTo(inputH.end)
                    }
                ){
                    MyTextField(
                        text = weight,
                        unit = "kg",
                        label = "weight",
                        modifier = Modifier.constrainAs(weightField) {
                            top.linkTo(heightField.bottom)
                        }
                    ) {weight = it}
                }

                Button(
                    modifier = Modifier.constrainAs(button) {
                        top.linkTo(inputW.bottom)
                        start.linkTo(headline.start)
                        end.linkTo(headline.end)
                        bottom.linkTo(halfH)
                },
                    onClick = {
                    val h = height.toDoubleOrNull()
                    val w = weight.toDoubleOrNull()
                    bmi = if (h != null && w != null && h > 0 && w >= 0 ) {
                        "%.2f".format(w/(h*h))
                    } else {
                        "INPUT NOT VALID"
                    }

                }) {
                    Text(text = "Calc")
                }

                Text(
                    text = bmi,
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.constrainAs(result) {
                        top.linkTo(halfH)
                        start.linkTo(headline.start)
                        end.linkTo(headline.end)
                        bottom.linkTo(botGL)
                    }
                )
            }
        }
    }
}

@Composable
fun MyTextField(text: String, unit: String, modifier: Modifier = Modifier, label: String = "", onValChange: (String) -> Unit) {
    val focusMan = LocalFocusManager.current
    TextField(
        value = text,
        onValueChange = onValChange,
        singleLine = true,
        label = {Text(label)},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusMan.clearFocus() }
        ),
        modifier = modifier,
        visualTransformation = UnitVisualTransformation(unit)
    )
}

class UnitVisualTransformation(private val mUnit: String) : VisualTransformation {
    private class UnitOffsetMapping(val txt: String): OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return if(offset < 0) 0
            else if (offset <= txt.length) offset
            else txt.length
        }

        override fun transformedToOriginal(offset: Int): Int {
            return originalToTransformed(offset)
        }
    }
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(AnnotatedString(text.text + " " + mUnit), UnitOffsetMapping(text.text))
    }
}