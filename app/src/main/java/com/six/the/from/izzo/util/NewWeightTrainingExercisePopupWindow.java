package com.six.the.from.izzo.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.six.the.from.izzo.R;


public class NewWeightTrainingExercisePopupWindow extends android.widget.PopupWindow {
    Context ctx;
    Button btnDismiss;
    // TextView lblText;
    View popupView;

    public NewWeightTrainingExercisePopupWindow(Context context, final View backgroundLayout, int width, int height) {
        super(context);

        ctx = context;
        popupView = LayoutInflater.from(context).inflate(R.layout.popup_new_weighttraining_exercise,
                null);
        setContentView(popupView);

        btnDismiss = (Button) popupView.findViewById(R.id.btn_dismiss);

        setHeight(width);
        setWidth(height);

        // Closes the popup window when touch outside of it - when looses focus
        setOutsideTouchable(true);
        setFocusable(true);

        // Removes default black background
        setBackgroundDrawable(new BitmapDrawable());

        btnDismiss.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                backgroundLayout.setVisibility(View.GONE);
            }
        });
    }

    public void show(View anchor, int x, int y) {
        showAtLocation(anchor, Gravity.CENTER, x, y);
    }
}