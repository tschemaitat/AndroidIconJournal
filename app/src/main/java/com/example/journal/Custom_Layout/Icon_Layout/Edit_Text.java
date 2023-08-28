package com.example.journal.Custom_Layout.Icon_Layout;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.journal.Custom_Layout.Listener.String_Listener;
import com.example.journal.MainActivity;
import com.example.journal.R;

public class Edit_Text {
    private ConstraintLayout layout;
    private EditText editText;
    private TextView textView;
    private ImageButton edit_button;
    private ImageButton check_button;
    private ImageButton discard_button;
    private String_Listener listener;
    InputMethodManager imm;
    private String string_before_edit = null;
    public Edit_Text(String_Listener listener, String text, int size, Context context){
        this.listener = listener;
        layout = MainActivity.inflate_page(context, R.layout.edit_icon_title);
        textView = layout.findViewById(R.id.text_view);
        textView.setText(text);
        textView.setTextSize(size);
        editText = layout.findViewById(R.id.editTextTextPersonName);
        editText.setText(text);
        editText.setTextSize(size);
        editText.setVisibility(View.INVISIBLE);
        edit_button = layout.findViewById(R.id.edit_button);
        check_button = layout.findViewById(R.id.check_button);
        discard_button = layout.findViewById(R.id.discard_button);
        check_button.setVisibility(View.INVISIBLE);
        discard_button.setVisibility(View.INVISIBLE);
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                string_before_edit = editText.getText().toString();
                textView.setVisibility(View.INVISIBLE);
                edit_button.setVisibility(View.INVISIBLE);
                editText.setVisibility(View.VISIBLE);
                check_button.setVisibility(View.VISIBLE);
                discard_button.setVisibility(View.VISIBLE);
                editText.requestFocus();
                if (imm != null) {
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imm != null) {
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
                textView.setVisibility(View.VISIBLE);
                edit_button.setVisibility(View.VISIBLE);
                editText.setVisibility(View.INVISIBLE);
                check_button.setVisibility(View.INVISIBLE);
                discard_button.setVisibility(View.INVISIBLE);
                textView.setText(editText.getText().toString());
                listener.set_string(editText.getText().toString());
            }
        });

        discard_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imm != null) {
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
                textView.setVisibility(View.VISIBLE);
                edit_button.setVisibility(View.VISIBLE);
                editText.setVisibility(View.INVISIBLE);
                check_button.setVisibility(View.INVISIBLE);
                discard_button.setVisibility(View.INVISIBLE);
                editText.setText(string_before_edit);
                textView.setText(string_before_edit);
                string_before_edit = null;


            }
        });
    }

    public void set_text(String text){

    }

    public View get_root(){
        return layout;
    }
}
