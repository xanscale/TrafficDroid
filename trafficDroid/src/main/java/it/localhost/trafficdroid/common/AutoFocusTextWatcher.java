package it.localhost.trafficdroid.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class AutoFocusTextWatcher implements TextWatcher {
	private EditText editText;
	private int maxLength;

	public AutoFocusTextWatcher(EditText editText, int maxLength) {
		this.editText = editText;
		this.maxLength = maxLength;
		editText.addTextChangedListener(this);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s.length() == maxLength)
			editText.focusSearch(View.FOCUS_FORWARD).requestFocus();
	}
}
