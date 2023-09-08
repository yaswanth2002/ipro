package com.example.ipro

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        val rootView = LinearLayout(this)
        rootView.orientation = LinearLayout.VERTICAL
        rootView.gravity = Gravity.CENTER
        rootView.setBackgroundColor(Color.WHITE)

        // Edit Text 1
        val editText1 = EditText(this)
        editText1.hint = "  NAME"
        editText1.background = getDrawable(R.drawable.edit_text_background)

        // Edit Text 2 - Should allow only numbers
        val editText2 = EditText(this)
        editText2.hint = "  ID"
        editText2.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        editText2.background = getDrawable(R.drawable.edit_text_background)

        // Radio Button - to select boy or girl
        val radioGroup = RadioGroup(this)
        radioGroup.orientation = RadioGroup.HORIZONTAL

        val boyRadioButton = RadioButton(this)
        boyRadioButton.text = "Boy"
        val girlRadioButton = RadioButton(this)
        girlRadioButton.text = "Girl"

        radioGroup.addView(boyRadioButton)
        radioGroup.addView(girlRadioButton)

        // Spinner
        val spinner = Spinner(this)
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("ID PROOF", "Aadhar", "Driving Licence", "Voter ID", "")
        )
        spinner.adapter = spinnerAdapter

        // Checkbox
        val checkboxGroup = LinearLayout(this)
        checkboxGroup.orientation = LinearLayout.VERTICAL

        val checkboxes = mutableListOf<CheckBox>()
        val programmingLanguages = listOf("java","Kotlin","python")
        for (languages in programmingLanguages) {
            val checkBox = CheckBox(this)
            checkBox.text = languages
            checkboxes.add(checkBox)
            checkboxGroup.addView(checkBox)
        }

        // Button
        val validateButton = Button(this)
        validateButton.text = "Submit"
        validateButton.setOnClickListener {
            val editText1Text = editText1.text.toString()
            val editText2Text = editText2.text.toString()
            val spinnerSelectedPosition = spinner.selectedItemPosition
            val atLeastOneCheckboxChecked = checkboxes.any { it.isChecked }
            val radioSelectedId = radioGroup.checkedRadioButtonId

            viewModel.validateFields(
                editText1Text,
                editText2Text,
                spinnerSelectedPosition,
                atLeastOneCheckboxChecked,
                radioSelectedId
            )
        }

        rootView.addView(editText1)
        rootView.addView(editText2)
        rootView.addView(radioGroup)
        rootView.addView(spinner)
        rootView.addView(checkboxGroup)
        rootView.addView(validateButton)

        setContentView(rootView)
    }
}

class MyViewModel : ViewModel() {
    val validationErrors = MutableLiveData<List<String>>()

    fun validateFields(
        editText1Text: String,
        editText2Text: String,
        spinnerSelectedPosition: Int,
        isCheckboxChecked: Boolean,
        radioSelectedId: Int
    ) {
        val errors = mutableListOf<String>()

        if (editText1Text.isEmpty()) {
            errors.add("Edit Text 1 is required")
        } else if (!editText1Text.matches(Regex("[a-zA-Z]+"))) {
            errors.add("Edit Text 1 should contain only alphabets")
        }

        if (editText2Text.isEmpty()) {
            errors.add("Edit Text 2 is required")
        } else if (!editText2Text.matches(Regex("[0-9]+"))) {
            errors.add("Edit Text 2 should contain only numbers")
        }

        if (spinnerSelectedPosition == 0) {
            errors.add("Please select an item from the Spinner")
        }

        if (!isCheckboxChecked) {
            errors.add("Please select at least one Checkbox")
        }

        if (radioSelectedId == -1) {
            errors.add("Please select a Radio Button option")
        }

        validationErrors.value = errors
    }
}
