package com.xiiilab;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Sergey on 21.07.2018
 */
public class BindingConversions {

    @InverseBindingAdapter(attribute = "android:text")
    public static int getInt(TextView view) {
        try {
            return Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @BindingAdapter("android:text")
    public static void setInt(TextView view, int number) {
        view.setText(String.format(Locale.getDefault(), "%d", number));
    }
}
