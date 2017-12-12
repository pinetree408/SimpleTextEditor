package com.pinetree408.research.simpletexteditor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.StrikethroughSpan;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    EditText textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream is = this.getResources().openRawResource(R.raw.test);
        textView = (EditText) findViewById(R.id.test_text_view);
        textView.setText(convertStreamToString(is));
        textView.setCustomSelectionActionModeCallback(new StyleCallback());
        textView.setInputType(InputType.TYPE_NULL);
        textView.setSingleLine(false);
        textView.setTextIsSelectable(true);
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private class StyleCallback implements ActionMode.Callback {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.clear();
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.style, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            menu.removeItem(android.R.id.selectAll);
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int start = textView.getSelectionStart();
            int end = textView.getSelectionEnd();
            SpannableStringBuilder ssb = new SpannableStringBuilder(textView.getText());

            switch(item.getItemId()) {
                case R.id.marking:
                    CharacterStyle cs = new StrikethroughSpan();
                    ssb.setSpan(cs, start, end, 1);
                    textView.setText(ssb);
                    return true;

                case R.id.undo:
                    Object spansToRemove[] = ssb.getSpans(start, end, Object.class);
                    for(Object span: spansToRemove){
                        if(span instanceof CharacterStyle)
                            ssb.removeSpan(span);
                    }
                    textView.setText(ssb);
                    return true;
            }
            return false;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }
    }
}
