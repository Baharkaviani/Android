package com.example.currencyexchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    // create LiveData
    private val _rialCurrency = MutableLiveData<Double>()
    private val rialCurrency: LiveData<Double>
        get() = _rialCurrency

    // define TextInputEditTexts for reaching each field after creation the activity
    private lateinit var rialText : TextInputEditText
    private lateinit var tomanText : TextInputEditText
    private lateinit var dollarText : TextInputEditText
    private lateinit var canadaDollarText : TextInputEditText
    private lateinit var poundText : TextInputEditText
    private lateinit var euroText : TextInputEditText
    private lateinit var dirhamText : TextInputEditText

    // currency conversion
    private val rialToToman = 0.1
    private val rialToDollar = 0.000023853035 // https://www.xe.com/currencyconverter/convert/?Amount=1&From=IRR&To=USD
    private val rialToCanadaDollar = 0.000032245751
    private val rialToPound = 0.000019749743
    private val rialToEuro = 0.000022419978
    private val rialToDirham = 0.000087611241

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _rialCurrency.value = 0.0

        rialText = findViewById(R.id.rial_input)
        tomanText = findViewById(R.id.toman_input)
        dollarText = findViewById(R.id.dollar_input)
        canadaDollarText = findViewById(R.id.canada_dollar_input)
        poundText = findViewById(R.id.pound_input)
        euroText = findViewById(R.id.euro_input)
        dirhamText = findViewById(R.id.dirham_input)

        rialText.addTextChangedListener(onTextChanged = {text, _, _, _ ->
            if (rialText.hasFocus())
                _rialCurrency.value =
                    if (text.toString().isNotEmpty())
                        text.toString().toDouble()
                    else 0.0
        })
        tomanText.addTextChangedListener(onTextChanged = {text, _, _, _ ->
            if (tomanText.hasFocus())
                _rialCurrency.value =
                    if (text.toString().isNotEmpty())
                        text.toString().toDouble() / rialToToman
                    else 0.0
        })
        dollarText.addTextChangedListener(onTextChanged = {text, _, _, _ ->
            if (dollarText.hasFocus())
                _rialCurrency.value =
                    if (text.toString().isNotEmpty())
                        text.toString().toDouble() / rialToDollar
                    else 0.0
        })
        canadaDollarText.addTextChangedListener(onTextChanged = {text, _, _, _ ->
            if (canadaDollarText.hasFocus())
                _rialCurrency.value =
                    if (text.toString().isNotEmpty())
                        text.toString().toDouble() / rialToCanadaDollar
                    else 0.0
        })
        poundText.addTextChangedListener(onTextChanged = {text, _, _, _ ->
            if (poundText.hasFocus())
                _rialCurrency.value =
                    if (text.toString().isNotEmpty())
                        text.toString().toDouble() / rialToPound
                    else 0.0
        })
        euroText.addTextChangedListener(onTextChanged = {text, _, _, _ ->
            if (euroText.hasFocus())
                _rialCurrency.value =
                    if (text.toString().isNotEmpty())
                        text.toString().toDouble() / rialToEuro
                    else 0.0
        })
        dirhamText.addTextChangedListener(onTextChanged = {text, _, _, _ ->
            if (dirhamText.hasFocus())
                _rialCurrency.value =
                    if (text.toString().isNotEmpty())
                        text.toString().toDouble() / rialToDirham
                    else 0.0
        })

        rialCurrency.observe({ lifecycle }, {
            if (!rialText.hasFocus())
                rialText.setText(DecimalFormat("#.##").format(it))
            else
                rialText.setSelection(rialText.length())

            if (!tomanText.hasFocus())
                tomanText.setText(DecimalFormat("#.##").format(it * rialToToman))
            else
                tomanText.setSelection(tomanText.length())

            if (!dollarText.hasFocus())
                dollarText.setText(DecimalFormat("#.##").format(it * rialToDollar))
            else
                dollarText.setSelection(dollarText.length())

            if (!canadaDollarText.hasFocus())
                canadaDollarText.setText(DecimalFormat("#.##").format(it * rialToCanadaDollar))
            else
                canadaDollarText.setSelection(canadaDollarText.length())

            if (!poundText.hasFocus())
                poundText.setText(DecimalFormat("#.##").format(it * rialToPound))
            else
                poundText.setSelection(poundText.length())

            if (!euroText.hasFocus())
                euroText.setText(DecimalFormat("#.##").format(it * rialToEuro))
            else
                euroText.setSelection(euroText.length())

            if (!dirhamText.hasFocus())
                dirhamText.setText(DecimalFormat("#.##").format(it * rialToDirham))
            else
                dirhamText.setSelection(dirhamText.length())
        })
    }
}