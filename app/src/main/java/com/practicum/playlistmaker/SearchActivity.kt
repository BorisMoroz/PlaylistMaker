package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isVisible

class SearchActivity : AppCompatActivity() {

    private var inputValue : String? = INPUT_VALUE_DEF

    private lateinit var inputEditText : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBack = findViewById<ImageView>(R.id.button_back)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        inputEditText = findViewById<EditText>(R.id.inputEditText)

        buttonBack.setOnClickListener {
            finish()
            }

        clearButton.setOnClickListener {
            inputEditText.setText("")

            val inputMethod = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethod.hideSoftInputFromWindow(inputEditText.windowToken,0)
        }

        val inputTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                inputValue = s?.toString()
            }
        }
        inputEditText.addTextChangedListener(inputTextWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return s.isNullOrEmpty()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_VALUE, inputValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        inputValue = savedInstanceState.getString(INPUT_VALUE, INPUT_VALUE_DEF)
        inputEditText.setText(inputValue)
    }

    companion object {
        private const val INPUT_VALUE = "INPUT_VALUE"
        private const val INPUT_VALUE_DEF = ""
    }
}