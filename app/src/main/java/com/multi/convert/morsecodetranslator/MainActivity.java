package com.multi.convert.morsecodetranslator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    HashMap<Character, String> morse;
    HashMap<String,Character> theText;
    EditText n;
    EditText m;
    private String blockCharacterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0987654321";
    Button copyText;
    Button copyMorse;
    Button pasteMorse;
    String lastPressed;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        morse = new HashMap<Character, String>();
        lastPressed = "";
        counter = 0;
        n = (EditText)findViewById(R.id.normalText);
        m = (EditText) findViewById(R.id.morseText);
        copyText = (Button) findViewById(R.id.copyText);
        copyMorse = (Button) findViewById(R.id.copyMorse);
        theText = new HashMap<String, Character>();
        pasteMorse = (Button) findViewById(R.id.paste);

        copyText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(null, n.getText());
                if (clip != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_LONG).show();
                }
            }
        });

        copyMorse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard != null) {
                    ClipData clip = ClipData.newPlainText(null, m.getText());
                    if (clip != null) {
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getApplicationContext(), "Copied to Clipboard", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        pasteMorse.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard != null) {
                    ClipData.Item item = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                        item = clipboard.getPrimaryClip().getItemAt(0);
                        if (item != null) {
                            m.setText(item.getText().toString());
                        }
                    }
                }
            }
        });

        morse.put('a', ". -");
        morse.put('b', "- . . .");
        morse.put('c', "- . - .");
        morse.put('d', "- . .");
        morse.put('e', ".");
        morse.put('f', ". . - .");
        morse.put('g', "- - .");
        morse.put('h', ". . . .");
        morse.put('i', ". .");
        morse.put('j', ". - - -");
        morse.put('k', "- . -");
        morse.put('l', ". - . .");
        morse.put('m', "- -");
        morse.put('n', "- .");
        morse.put('o', "- - -");
        morse.put('p', ". - - .");
        morse.put('q', "- - . -");
        morse.put('r', ". - .");
        morse.put('s', ". . .");
        morse.put('t', "-");
        morse.put('u', ". . -");
        morse.put('v', ". . . -");
        morse.put('w', ". - -");
        morse.put('x', "- . . -");
        morse.put('y', "- . - -");
        morse.put('z', "- - . .");
        morse.put('0', "- - - - -");
        morse.put('1', ". - - - -");
        morse.put('2', ". . - - -");
        morse.put('3', ". . . - -");
        morse.put('4', ". . . . -");
        morse.put('5', ". . . . .");
        morse.put('6', "- . . . .");
        morse.put('7', "- - . . .");
        morse.put('8', "- - - . .");
        morse.put('9', "- - - - .");
        morse.put('.', ". - . - . -");
        morse.put(',', "- - . . - -");
        morse.put('?', ". . - - . .");
        morse.put('!', ". . - - .");
        morse.put(':', "- - - . . ,");
        morse.put('"', ". - . . - .");
        morse.put('\'', ". - - - - .");
        morse.put('=', "- . . . -");
        morse.put ('\n',"       ");
        morse.put('@', ". - - . - .");
        morse.put('-',". . - -");
        morse.put(' '," ");

        for (Map.Entry<Character, String> entry : morse.entrySet()) {
                  theText.put(entry.getValue(),entry.getKey());
        }
        m.setFilters(new InputFilter[] {filter});
    }

    public InputFilter filter = new InputFilter() {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        if (source != null && blockCharacterSet.contains(("" + source))) {
            return "";
        }
        return null;
              }
   };


public String toMorse(String text) {
        String output = "";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ' || text.charAt(i) == '\n') {
                output += "       ";
            }
            else {
                output += morse.get(text.charAt(i));
                output += "   ";
            }
        }
        return output;
    }

    public String toText(String mor) {
        String output = "";
        String[] split = mor.split("       ");
        for (String word : split) {
            String[] chars = word.split("   ");
            for (String character:chars ) {
                if (character.isEmpty()) {
                    continue;
                }
                if (theText.get(character) == null) {
                    output = "Error in Syntax at Character: " + "*" + character + "*";
                    break;
                }
                output += theText.get(character);
            }

            output += " ";
        }
        if (output == null) {
            output = "Unable to translate message: Incorrect Syntax";
        }
        return output;

    }
    public void encode(View v) {
        Editable input = n.getText();
        String in = input.toString();
        in = in.toLowerCase();
        String out = toMorse(in);
        m.setText(out);
        lastPressed = "encode";
    }
    public void decode(View v) {
        m.append("   ");

        CharSequence temp2 = m.getText();
        m.setText(temp2);
            Editable input = m.getText();
            String in = input.toString();
            String out = toText(in);
            n.setText(out);
            lastPressed = "decode";
        }

    public void clear1(View v) {
        n.setText("");
        lastPressed = "clear1";
        counter = 0;
    }
    public void clear2(View v) {
        m.setText("");
        lastPressed = "clear2";
        counter = 0;
    }

    public void addDot(View v) {

        m.append(".");
        m.append(" ");
        lastPressed = "dot";
        counter = 0;
    }
    public void addDash(View v) {
        m.append("-");
        m.append(" ");
        lastPressed = "dash";
        counter = 0;
    }
   public void newLetter(View v) {
       m.append("  ");
   }
   public void newWord(View v) {
       m.append("      ");
   }
    public void backSpace(View v) {
        CharSequence temp = m.getText();
        CharSequence temp2 = "";
        if (temp != "" && m.getText().length() > 0) {
            temp2 = temp.subSequence(0,temp.length() - 1);
            m.setText(temp2);
        }
        else {
            m.setText("");
        }
        lastPressed = "backspace";
    }

}
