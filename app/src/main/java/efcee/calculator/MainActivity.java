package efcee.calculator;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    String str = "";
    EditText showResult;
    RelativeLayout historyMenu;
    ListView historyList;
    ArrayList<String> inputHistory = new ArrayList<>();
    Button clearHistory;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<Character> operators = new ArrayList<>(Arrays.asList(
            '/','*','-','+'));

    // number buttons
    Button one, two, three, four, five, six, seven, eight, nine, zero;

    // other buttons
    Button  history, backspace, clear, parenthesis, percent,
            div, mul, sub, add, negate, decimal, equal;

    // function buttons page 1
    Button second, squareRoot, sin, cos, tan, natLog, log, reciprocal, eulerToTheX,
            squared, xToTheY, absolute, piConstant, euler;

    // function buttons page 2
    Button first, cubeRoot, iSin, iCos, iTan, sinh, cosh, tanh,
            iSinh, iCosh, iTanh, twoToTheX, cubed, factorial;

    // radian and degree button
    Button radianDegree;

    // phrases to be treated as a single character (functions)
    ArrayList<String> characterPhrases = new ArrayList<>(Arrays.asList(
            "sin(", "cos(", "tan(", "ln(", "log(", "abs(", "asin(", "acos(",
            "atan(", "sinh(", "cosh(", "tanh(", "asinh(", "acosh(", "atanh("));

    // boolean to check if in Radians mode
    boolean radians = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        enableFullscreen();

        // restore previous state
        if (savedInstanceState != null) {
            inputHistory = savedInstanceState.getStringArrayList("inputHistory");
            str = savedInstanceState.getString("str");
        }

        initIds();
        initOnClickListeners();

        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, inputHistory);
        historyList.setAdapter(arrayAdapter);
        historyList.setSelection(arrayAdapter.getCount() - 1);
        disableSoftInputFromAppearing(showResult);
    }

    private void initIds() {
        // misc ids
        showResult = (EditText) findViewById(R.id.result_id);
        historyMenu = (RelativeLayout) findViewById(R.id.history_menu);
        historyList = (ListView) findViewById(R.id.history_list);
        clearHistory = (Button) findViewById(R.id.clear_history);

        // number buttons
        one = (Button) this.findViewById(R.id.one);
        two = (Button) this.findViewById(R.id.two);
        three = (Button) this.findViewById(R.id.three);
        four = (Button) this.findViewById(R.id.four);
        five = (Button) this.findViewById(R.id.five);
        six = (Button) this.findViewById(R.id.six);
        seven = (Button) this.findViewById(R.id.seven);
        eight = (Button) this.findViewById(R.id.eight);
        nine = (Button) this.findViewById(R.id.nine);
        zero = (Button) this.findViewById(R.id.zero);

        // other buttons
        history = (Button) this.findViewById(R.id.history);
        backspace = (Button) this.findViewById(R.id.backspace);
        clear = (Button) this.findViewById(R.id.clear);
        parenthesis = (Button) this.findViewById(R.id.parenthesis);
        percent = (Button) this.findViewById(R.id.percent);
        div = (Button) this.findViewById(R.id.div);
        mul = (Button) this.findViewById(R.id.mul);
        sub = (Button) this.findViewById(R.id.sub);
        add = (Button) this.findViewById(R.id.add);
        negate = (Button) this.findViewById(R.id.negate);
        decimal = (Button) this.findViewById(R.id.decimal);
        equal = (Button) this.findViewById(R.id.equal);

        if (getResources().getConfiguration().orientation == 2) { // 1 == portrait; 2 == landscape
            // function buttons page 1
            second = (Button) this.findViewById(R.id.second);
            squareRoot = (Button) this.findViewById(R.id.squareRoot);
            sin = (Button) this.findViewById(R.id.sin);
            cos = (Button) this.findViewById(R.id.cos);
            tan = (Button) this.findViewById(R.id.tan);
            natLog = (Button) this.findViewById(R.id.natLog);
            log = (Button) this.findViewById(R.id.log);
            reciprocal = (Button) this.findViewById(R.id.reciprocal);
            eulerToTheX = (Button) this.findViewById(R.id.eulerToTheX);
            squared = (Button) this.findViewById(R.id.squared);
            xToTheY = (Button) this.findViewById(R.id.xToTheY);
            absolute = (Button) this.findViewById(R.id.absolute);
            piConstant = (Button) this.findViewById(R.id.piConstant);
            euler = (Button) this.findViewById(R.id.euler);

            // function buttons page 2
            first = (Button) this.findViewById(R.id.first);
            cubeRoot = (Button) this.findViewById(R.id.cubeRoot);
            iSin = (Button) this.findViewById(R.id.iSin);
            iCos = (Button) this.findViewById(R.id.iCos);
            iTan = (Button) this.findViewById(R.id.iTan);
            sinh = (Button) this.findViewById(R.id.sinh);
            cosh = (Button) this.findViewById(R.id.cosh);
            tanh = (Button) this.findViewById(R.id.tanh);
            iSinh = (Button) this.findViewById(R.id.iSinh);
            iCosh = (Button) this.findViewById(R.id.iCosh);
            iTanh = (Button) this.findViewById(R.id.iTanh);
            twoToTheX = (Button) this.findViewById(R.id.twoToTheX);
            cubed = (Button) this.findViewById(R.id.cubed);
            factorial = (Button) this.findViewById(R.id.factorial);

            // radian and degree button
            radianDegree = (Button) this.findViewById(R.id.radianDegree);
        }
    }

    private void initOnClickListeners() {
        // misc buttons
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHistory();
            }
        });

        // number buttons
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDigit('1');
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDigit('2');
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDigit('3');
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDigit('4');
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDigit('5');
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDigit('6');
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDigit('7');
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDigit('8');
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDigit('9');
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDigit('0');
            }
        });

        // other buttons
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { toggleHistoryMenu();
            }
        });
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backspaceSelected();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        parenthesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertParenthesis();
            }
        });
        percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPercentage();
            }
        });
        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertOperator('/');
            }
        });
        mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertOperator('*');
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertOperator('-');
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertOperator('+');
            }
        });
        negate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNegate();
            }
        });
        decimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDecimal();
            }
        });
        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });

        if (getResources().getConfiguration().orientation == 2) { // 1 == portrait; 2 == landscape
            // function buttons page 1
            second.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleFunctionPage();
                }
            });
            squareRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("sqrt("); }
            });  // function
            sin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("sin(");}
            }); // function
            cos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("cos(");}}); // function
            tan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("tan(");
                }
            }); // function
            natLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("ln(");
                }
            }); // function
            log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("log(");
                }
            }); // function
            reciprocal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertFunction("1/");
                }
            });
            eulerToTheX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertFunction("e^(");
                }
            });
            squared.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertFunction("^(2)");
                }
            });
            xToTheY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertFunction("^(");
                }
            });
            absolute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("abs(");
                }
            }); // function
            piConstant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strongInsert('\u03C0');
                }
            });
            euler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertFunction("e");
                }
            });

            // function buttons page 2
            first.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleFunctionPage();
                }
            });
            cubeRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("cbrt(");
                }
            }); // function
            iSin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("asin(");
                }
            }); // function
            iCos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("acos(");
                }
            }); // function
            iTan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("atan(");
                }
            }); // function
            sinh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("sinh(");
                }
            }); // function
            cosh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("cosh(");
                }
            }); // function
            tanh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("tanh(");
                }
            }); // function
            iSinh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("asinh(");
                }
            }); // function
            iCosh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("acosh(");
                }
            }); // function
            iTanh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("atanh(");
                }
            }); // function
            twoToTheX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertFunction("2^(");
                }
            });
            cubed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertFunction("^(3)");
                }
            });
            factorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {insertFunction("fact(");
                }
            }); // function

            // radian and degree button
            radianDegree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleRadianDegree();
                }
            });
        }
    }

    private void insert(char c) {
        vibrate();
    }

    private void insertOperator(char c) {
        vibrate();
        int selectionStart = showResult.getSelectionStart();
        int selectionEnd = showResult.getSelectionEnd();

        /* CASE: STR IS NOT EMPTY AND SELECTION_START IS GREATER THAN ZERO*/
        if (str.length() != 0 && selectionStart > 0) {
            char previousSelected = str.charAt(selectionStart - 1); // char before cursor
            /* CASE: PREVIOUS SELECTED IS A CLOSED PARENTHESIS, A PERCENTAGE,
             *       A DIGIT OR A DECIMAL */
            if (previousSelected == ')' || previousSelected == '%'
                    || Character.isDigit(previousSelected) || previousSelected == '.') {
                strongInsert(c);
            }
            /* CASE: PREVIOUS SELECTED IS AN OPERATOR */
            if (operators.contains(previousSelected)) {
                str = str.substring(0, selectionStart - 1)
                        + str.substring(selectionEnd);
                showResult.setSelection(selectionStart - 1);
                strongInsert(c);
            }
        }
    }

    private void insertPercentage() {
        vibrate();
        int selectionStart = showResult.getSelectionStart();
        /* CASE: STR IS NOT EMPTY AND SELECTION_START IS GREATER THAN ZERO*/
        if (str.length() != 0 && selectionStart > 0
                && (Character.isDigit(str.charAt(selectionStart - 1))
                    || str.charAt(selectionStart - 1) == '.')) {
            strongInsert('%');
        }
    }

    private void insertDigit(char c) {
        vibrate();
        int selectionStart = showResult.getSelectionStart();
        int selectionEnd = showResult.getSelectionEnd();
        char previousSelected =
                (str.isEmpty() || selectionStart == 0) ? ' ' : str.charAt(selectionStart - 1);

        /* CASE: PREVIOUS SELECTED IS A CLOSED PARENTHESIS, A PERCENTAGE,
         *       EULER'S, FACTORIAL, OR PI */
        if (previousSelected == ')' || previousSelected == '%' || previousSelected == 'e'
                || previousSelected == '!' || previousSelected == '\u03C0') {
            strongInsert('*');
            strongInsert(c);
        }
        /* CASE: PREVIOUS NUMBER IS ZERO */
        else if ((previousSelected == '0' && selectionStart - 2 < 0) ||
                (selectionStart > 1 && previousSelected == '0'
                && !Character.isDigit(str.charAt(selectionStart - 2))
                && str.charAt(selectionStart - 2) != '.')) {
            str = str.substring(0, selectionStart - 1)
                    + str.substring(selectionEnd);
            showResult.setSelection(selectionStart - 1);
            strongInsert(c);
        }
        else {
            strongInsert(c);
        }
    }

    private void insertParenthesis() {
        vibrate();
        int selectionStart = showResult.getSelectionStart();
        char previousSelection =
                (str.isEmpty() || selectionStart == 0) ? ' ' : str.charAt(selectionStart - 1);
        if (str.isEmpty() || selectionStart == 0
                || operators.contains(previousSelection)
                || previousSelection == '(') {
            strongInsert('(');
        }
        else {
            // determine the parenthesis level at the current selection
            char chars[] = str.substring(0, selectionStart).toCharArray();
            int parenLevel = 0;
            for (int i = selectionStart - 1; i >= 0; i--) {
                if (chars[i] == ')') {
                    parenLevel -= 1;
                } else if (chars[i] == '(') {
                    parenLevel += 1;
                }
            }
            // insert based on parenLevel
            if (parenLevel == 0) {
                strongInsert('*');
                strongInsert('(');
            } else {
                strongInsert(')');
            }
        }
    }

    private void insertDecimal() {
        vibrate();
        int selectionStart = showResult.getSelectionStart();
        char chars[] = str.toCharArray();
        // scan the currently selected number for any decimals and return if found.
        // scan beyond the current selection
        for (int i = selectionStart; i < str.length(); i++) {
            if (chars[i] == '.') { return; }
            else if (!Character.isDigit(chars[i])) { break; }
        }
        // scan behind the current selection
        for (int i = selectionStart - 1; i >= 0; i--) {
            if (chars[i] == '.') { return; }
            else if (!Character.isDigit(chars[i])) { break; }
        }
        // no decimals have been found so the format for placing is determined below
        char previousSelected = (str.isEmpty() || selectionStart == 0)
                ? ' ' : str.charAt(selectionStart - 1);
        if (str.isEmpty()
                || selectionStart == 0
                || operators.contains(previousSelected)
                || previousSelected == '(') {
            strongInsert('0');
            strongInsert('.');
        }
        else if (previousSelected == '%' || previousSelected == ')' || previousSelected == '!'
                || previousSelected == 'e' || previousSelected == '\u03C0' /* pi */) {
            strongInsert('*');
            strongInsert('0');
            strongInsert('.');
        }
        else {
            strongInsert('.');
        }
    }

    private void insertNegate() {
        vibrate();
        int selectionStart = showResult.getSelectionStart();

        /* CASE: STR IS EMPTY */
        if (str.length() == 0) {
            strongInsert('(');
            strongInsert('-');
        }
        /* CASE: STR IS NOT EMPTY */
        else if (str.length() != 0) {
            char previousSelected = str.charAt(selectionStart - 1); // char before cursor
            if (Character.isDigit(previousSelected) || previousSelected == '.') {
                char chars[] = str.substring(0, selectionStart).toCharArray();
                int offset = selectionStart;
                for (int i = selectionStart - 2; i >= 0; i--) {
                    if (!Character.isDigit(chars[i]) && chars[i] != '.') {
                        offset = (selectionStart - 1) - i;
                        break;
                    }
                }
                showResult.setSelection(selectionStart - offset);
                strongInsert('(');
                strongInsert('-');
                showResult.setSelection(selectionStart + 2);
            }
            else if (operators.contains(previousSelected)){
                strongInsert('(');
                strongInsert('-');
            }
            else {
                strongInsert('*');
                strongInsert('(');
                strongInsert('-');
            }
        }
    }

    private void insertFunction(String func) {
        for (char c : func.toCharArray()) {
            strongInsert(c);
        }
    }

    private void strongInsert(char c) {
        int selectionStart = showResult.getSelectionStart();
        int selectionEnd = showResult.getSelectionEnd();
        str = str.substring(0, selectionStart) + c + str.substring(selectionEnd);
        // change operator characters' colors to blue
        setResultText(str);
        showResult.setSelection(selectionStart + 1);
    }

    private void toggleFunctionPage() {
        vibrate();
        Button hiddenButtons[];
        Button visibleButtons[];
        if (second.getVisibility() == View.VISIBLE) {
            visibleButtons = new Button[]{
                    second, squareRoot, sin, cos, tan, natLog, log, reciprocal, eulerToTheX,
                    squared, xToTheY, absolute, piConstant, euler};
            hiddenButtons = new Button[]{
                    first, cubeRoot, iSin, iCos, iTan, sinh, cosh, tanh,
                    iSinh, iCosh, iTanh, twoToTheX, cubed, factorial};
        }
        else {
            visibleButtons = new Button[]{
                    first, cubeRoot, iSin, iCos, iTan, sinh, cosh, tanh,
                    iSinh, iCosh, iTanh, twoToTheX, cubed, factorial};
            hiddenButtons = new Button[]{
                    second, squareRoot, sin, cos, tan, natLog, log, reciprocal, eulerToTheX,
                    squared, xToTheY, absolute, piConstant, euler};
        }
        for (Button b : visibleButtons) {
            b.setVisibility(View.INVISIBLE);
        }
        for (Button b : hiddenButtons) {
            b.setVisibility(View.VISIBLE);
        }
    }

    private void toggleRadianDegree() {
        vibrate();
        if (radianDegree.getText() == getResources().getString(R.string.radian)) {
            radianDegree.setText(getResources().getString(R.string.degree));
            radians = true;
        }
        else {
            radianDegree.setText(getResources().getString(R.string.radian));
            radians = false;
        }
    }

    private void reset() {
        vibrate();
        str = "";
        showResult.setText("");
    }

    private void backspaceSelected() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        int selectionStart = showResult.getSelectionStart();
        int selectionEnd = showResult.getSelectionEnd();
        if (selectionStart == selectionEnd) {
            if (selectionStart > 0) {
                vibrator.vibrate(5);
                str = str.substring(0, selectionStart - 1)
                        + str.substring(selectionEnd);
                setResultText(str);
                showResult.setSelection(selectionStart - 1);
            }
        }
        else {
            vibrator.vibrate(5);
            str = str.substring(0, selectionStart) + str.substring(selectionEnd);
            showResult.setText(str);
        }
    }

    private void calculate() {
        vibrate();
        try {
            if (!str.isEmpty()) {
                String input = str;
                DecimalFormat df = new DecimalFormat("#,###,###,###,###,##0.##############");
                str = df.format(eval(str.replace("%", "/100"), radians));
                hideHistoryMenu();
                // if the input is not the same as the output
                if (!input.equals(str)) {
                    inputHistory.add(input + '\n' + '=' + str);
                    historyList.setSelection(arrayAdapter.getCount() - 1);

                    // change the results text color to blue
                    SpannableStringBuilder sb = new SpannableStringBuilder(str);
                    sb.setSpan(new ForegroundColorSpan(getColorRefHex(R.color.LightBlue)),
                            0, str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    showResult.setText(sb, TextView.BufferType.SPANNABLE);
                    showResult.setSelection(str.length());
                }
            }
        } catch (RuntimeException e) {
            alertMessage("Invalid format used.");
        }
    }

    private void setResultText(String str) {
        // change operator characters' colors to blue
        SpannableStringBuilder sb = new SpannableStringBuilder(str);
        Pattern p = Pattern.compile("[/*+%-](?<!\\(-)");
        Matcher m = p.matcher(str);
        while (m.find()){
            sb.setSpan(new ForegroundColorSpan(getColorRefHex(R.color.LightBlue)),
                    m.start(), m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        showResult.setText(sb, TextView.BufferType.SPANNABLE);
    }

    private void toggleHistoryMenu() {
        vibrate();
        if (historyMenu.isShown()) {
            historyMenu.setVisibility(View.GONE);
            history.setText("HISTORY");
        }
        else {
            historyMenu.setVisibility(View.VISIBLE);
            history.setText("KEYPAD");
        }
    }

    private void hideHistoryMenu() {
        historyMenu.setVisibility(View.GONE);
        history.setText("HISTORY");
    }

    private void clearHistory() {
        vibrate();
        inputHistory.clear();
        arrayAdapter.notifyDataSetChanged();
        hideHistoryMenu();
    }

    private int getColorRefHex(@android.support.annotation.ColorRes int id) {
        return ContextCompat.getColor(getApplicationContext(), id);
    }

    /**
     * Disable soft keyboard from appearing, use in conjunction with android:windowSoftInputMode="stateAlwaysHidden|adjustNothing"
     * @param editText
     */
    private static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }

    private void enableFullscreen() {
        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_main);
        }
        else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void alertMessage(String Message) {
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(5);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.enableFullscreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putStringArrayList("inputHistory", inputHistory);
        savedInstanceState.putString("str", str);
    }

    private static double eval(final String str, final boolean radians) {
        return new Object() {
            int pos = -1, ch;

            private double toRadians(double x) {
                if (radians) {
                    return x; // assume input is already in radians
                }
                else {
                    return Math.toRadians(x); // convert degrees to radians
                }
            }

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("fact")) x = MathPlus.fact((int) x);
                    else if (func.equals("cbrt")) x = Math.cbrt(x);
                    else if (func.equals("ln")) x = Math.log(x);
                    else if (func.equals("log")) x = Math.log10(x);
                    else if (func.equals("abs")) x = Math.abs(x);
                    else if (func.equals("sin")) x = Math.sin(toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(toRadians(x));
                    else if (func.equals("asin")) x = Math.asin(toRadians(x));
                    else if (func.equals("acos")) x = Math.acos(toRadians(x));
                    else if (func.equals("atan")) x = Math.atan(toRadians(x));
                    else if (func.equals("sinh")) x = Math.sinh(toRadians(x));
                    else if (func.equals("cosh")) x = Math.cosh(toRadians(x));
                    else if (func.equals("tanh")) x = Math.tanh(toRadians(x));
                    else if (func.equals("asinh")) x = MathPlus.asinh(toRadians(x));
                    else if (func.equals("acosh")) x = MathPlus.acosh(toRadians(x));
                    else if (func.equals("atanh")) x = MathPlus.atanh(toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}
