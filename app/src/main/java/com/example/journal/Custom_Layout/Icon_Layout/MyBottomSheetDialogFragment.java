package com.example.journal.Custom_Layout.Icon_Layout;

import com.example.journal.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static MyBottomSheetDialogFragment newInstance() {
        return new MyBottomSheetDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet, container, false);
    }

}
