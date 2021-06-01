package com.example.venuesnearby.ui.dialog;

import android.content.Context;

import com.example.venuesnearby.R;
import com.example.venuesnearby.data.model.app.CustomMessage;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SuccessAlert extends MaterialAlertDialogBuilder {

    public SuccessAlert(Context context, CustomMessage message) {
        super(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        init(message);
    }

    private void init(CustomMessage message) {
        setTitle(R.string.success);
        setIcon(R.drawable.ic_check_circle);
        setCancelable(false);
        setMessage(getContext().getString(message.getMessageResourceId(), message.getParams()));
        setPositiveButton(getContext().getString(R.string.ok), (dialog, which) -> dialog.dismiss());
    }
}