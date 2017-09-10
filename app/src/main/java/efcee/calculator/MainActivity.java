package efcee.calculator;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    String str = "";
    int parenLevel = 0;
    EditText showResult;
    RelativeLayout historyMenu;
    ArrayList<Character> operators = new ArrayList<Character>(Arrays.asList(
            '/','*','-','+'));

    // number buttons
    Button one, two, three, four, five, six, seven, eight, nine, zero;

    // other buttons
    Button  history, backspace, clear, parenthesis, percent,
            div, mul, sub, add, negate, decimal, equal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

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

        showResult = (EditText) findViewById(R.id.result_id);
        historyMenu = (RelativeLayout) findViewById(R.id.history_menu);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('1');
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('2');
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('3');
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('4');
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('5');
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('6');
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('7');
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('8');
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('9');
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('0');
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(5);
                if (historyMenu.isShown()) {
                    historyMenu.setVisibility(View.GONE);
                    history.setText("HISTORY");
                }
                else {
                    historyMenu.setVisibility(View.VISIBLE);
                    history.setText("KEYPAD");
                }
            }
        });

        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                int selectionStart = showResult.getSelectionStart();
                int selectionEnd = showResult.getSelectionEnd();
                if (selectionStart == selectionEnd) {
                    if (selectionStart > 0) {
                        vibrator.vibrate(5);
                        str = str.substring(0, selectionStart - 1)
                                + str.substring(selectionEnd);
                        showResult.setText(str);
                        showResult.setSelection(selectionStart - 1);
                    }
                }
                else {
                    vibrator.vibrate(5);
                    str = str.substring(0, selectionStart) + str.substring(selectionEnd);
                    showResult.setText(str);
                }
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
                insert('(');
            }
        });
        percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('%');
            }
        });

        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('/');
            }
        });
        mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('*');
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('-');
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('+');
            }
        });

        negate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        decimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert('.');
            }
        });
        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate();
            }
        });
    }

    private void reset() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(5);
        parenLevel = 0;
        str = "";
        showResult.setText("");
    }

    private void insert(char c) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(5);
        int selectionStart = showResult.getSelectionStart();
        int selectionEnd = showResult.getSelectionEnd();

        /* CASE: STR IS EMPTY */
        if (str.length() == 0) {
            if (Character.isDigit(c) || c == '(' || c == '.') {
                strongInsert(c);
            }
        }
        /* CASE: STR IS NOT EMPTY */
        else if (str.length() != 0) {
            char previousSelected = str.charAt(selectionStart - 1); // char before cursor
            /* CASE: PREVIOUS SELECTED IS AN OPERATOR OR OPEN PARENTHESIS */
            if (operators.contains(previousSelected) || previousSelected == '(') {
                if (Character.isDigit(c) || c == '(' || c == '.') {
                    strongInsert(c);
                }
            }
            /* CASE: PREVIOUS SELECTED IS A CLOSED PARENTHESIS */
            else if (previousSelected == ')') {
                // CASE: C IS A OPERATOR
                if (operators.contains(c)) {
                    strongInsert(c);
                }
                /* CASE: C IS A DIGIT, DECIMAL, OR OPEN PARENTHESIS */
                else if (Character.isDigit(c) || c == '.' || c == '(') {
                    strongInsert('*');
                    strongInsert(c);
                }
            }
            /* CASE: PREVIOUS SELECTED IS A DIGIT OR A DECIMAL */
            else if (Character.isDigit(previousSelected) || previousSelected == '.') {
                /* CASE: C IS AN OPERATOR */
                if (operators.contains(c)) {
                    strongInsert(c);
                }
                /* CASE: C IS A DIGIT */
                else if (Character.isDigit(c)) {
                    /* CASE: STR IS OF LENGTH ONE */
                    if (str.length() == 1) {
                        if (previousSelected == '0' && c != '.') {
                            str = "";
                            showResult.setSelection(selectionStart - 1);
                            strongInsert(c);
                        }
                        else {
                            strongInsert(c);
                        }
                    }
                    /* CASE: STR IS NOT OF LENGTH ONE */
                    else {
                        if (previousSelected == '0' && c != '.'
                                && !Character.isDigit(str.charAt(selectionStart - 2))
                                && str.charAt(selectionStart - 2) != '.') {
                            str = str.substring(0, selectionStart - 1)
                                    + str.substring(selectionEnd);
                            showResult.setSelection(selectionStart - 1);
                            strongInsert(c);
                        }
                        else {
                            strongInsert(c);
                        }
                    }
                }
                /* CASE: C IS AN OPEN PARENTHESIS */
                if (c == '(') {
                    // determine the parenthesis level at the current selection
                    char chars[] = str.substring(0, selectionStart).toCharArray();
                    int parenLevel = 0;
                    for (int i = selectionStart - 1; i >= 0; i--) {
                        if (chars[i] == ')') {
                            parenLevel -= 1;
                        }
                        else if (chars[i] == '(') {
                            parenLevel += 1;
                        }
                    }
                    // insert based on parenLevel
                    if (parenLevel == 0) {
                        strongInsert('*');
                        strongInsert(c);
                    }
                    else {
                        strongInsert(')');
                    }
                }
                /* CASE: C IS A DECIMAL */
                else if (c == '.') {
                    char chars[] = str.substring(0, selectionStart).toCharArray();
                    for (int i = selectionStart - 1; i >= 0; i--) {
                        if (chars[i] == '.') {
                            break;
                        }
                        else if (!Character.isDigit(chars[i]) || i == 0) {
                            strongInsert(c);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void strongInsert(char c) {
        int selectionStart = showResult.getSelectionStart();
        int selectionEnd = showResult.getSelectionEnd();
        str = str.substring(0, selectionStart) + c + str.substring(selectionEnd);
        showResult.setText(str);
        showResult.setSelection(selectionStart + 1);
    }

    private void calculate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(5);
        if (!str.isEmpty()) {
            str = Double.toString(this.eval(str));
            showResult.setText(str);
            showResult.setSelection(str.length());
        }
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

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

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
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
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
