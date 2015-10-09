package com.bdevlin.apps.authenticator;

import android.widget.AutoCompleteTextView.Validator;



public class EmailAddressValidator implements Validator {
    @Override
    public CharSequence fixText(CharSequence invalidText) {
        return "";
    }

    @Override
    public boolean isValid(CharSequence text) {
        return true;//Address.parse(text.toString()).length == 1;
    }
}
