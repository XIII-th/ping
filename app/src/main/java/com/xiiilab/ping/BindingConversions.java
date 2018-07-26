package com.xiiilab.ping;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created by Sergey on 21.07.2018
 */
public class BindingConversions {

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.######");

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

    @InverseBindingAdapter(attribute = "android:text")
    public static float getFloat(TextView view) {
        try {
            return Float.parseFloat(view.getText().toString());
        } catch (NumberFormatException e) {
            return 0F;
        }
    }

    @BindingAdapter("android:text")
    public static void setFloat(TextView view, float number) {
        view.setText(DECIMAL_FORMAT.format(number));
    }
}
