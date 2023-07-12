package com.game.myapp_2_0;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean firstLayout;

    private int sizeField;
    private int inRow;
    private int round;

    private String player;
    private String position;

    private Button actualButton;

    private Field field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("------------------------------------------");
        System.out.println(savedInstanceState);
        setContentView(R.layout.input_layout);
    }

    public void checkInputValues(View view) {
        EditText numInRowStr = (EditText)findViewById(R.id.editNumInRow);
        EditText numSizeStr = (EditText)findViewById(R.id.editLenghtOfSide);
        try {
            sizeField = Integer.parseInt(numSizeStr.getText().toString());
            inRow = Integer.parseInt(numInRowStr.getText().toString());
            checkIfInRange();
            createGame();
        } catch (NumberFormatException | ArithmeticException e) {
            popUpToast("Input integer between 1 and 100!");
        } catch (ArrayIndexOutOfBoundsException e) {
            popUpToast("Numbers in row can not \n  be large than length of field!");
        }
    }

    private void checkIfInRange() {
        if ((0 >= sizeField) || (sizeField >= 101) || (0 >= inRow) || (inRow >= 101)) {
            throw new ArithmeticException();
        }
        if (sizeField < inRow) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
    private void popUpToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("RestrictedApi")
    private void createGame() {
        field = new Field(inRow, sizeField);
        round = 1;
        setContentView(R.layout.activity_main);
        createGameLayout();
        choosePlayer();
        updateTitleText();
    }

    private void createGameLayout() {
        LinearLayout ll_vert = findViewById(R.id.ll_vert);
        ll_vert.setPadding(20,20,20,20);
        ll_vert.setGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
        float px = getDP();
        firstLayout = true;
        int sizeOfButton = (int) px * 3;
        int paddingOfButton = (int) px/9;
        int paddingOfRow = (int) px/6;
        for (int i=0; i<sizeField; i++) {
            LinearLayout ll_row = linearHorizontalSetUp(paddingOfRow);
            ll_vert.addView(ll_row);
            for (int j=0; j<sizeField; j++) {
                ll_row.addView(buttonSetUp(i,j,paddingOfButton, sizeOfButton));
            }
        }
    }

    private float getDP() {
        float dip = 14f;
        Resources r = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
    }

    private Button buttonSetUp(int i, int j, int paddingOfButton, int sizeOfButton) {
        Button btn = new Button(this);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(sizeOfButton, sizeOfButton);
        layoutParams.setMargins(paddingOfButton,0,paddingOfButton,0);
        btn.setLayoutParams(layoutParams);
        btn.setText(" ");
        btn.setTextScaleX(2.2F);
        btn.setTag(i +"," + j);
        btn.setBackgroundResource(R.drawable.button_style);
        btn.setOnClickListener(v -> {
            actualButton = btn;
            position = btn.getTag().toString();
            gameSequence();
        });
        return btn;
    }


    private LinearLayout linearHorizontalSetUp(int paddingOfRow){
        LinearLayout ll_row = new LinearLayout(this);
        ll_row.setOrientation(LinearLayout.HORIZONTAL);
        ll_row.setGravity(LinearLayout.TEXT_ALIGNMENT_GRAVITY);
        if (firstLayout) {
            ll_row.setPadding(0, 0, 0, 0);
            firstLayout = false;
        } else {
            ll_row.setPadding(0, paddingOfRow, 0, 0);
        }
        return ll_row;
    }

    private void gameSequence() {
        if (writeSymbolToField()){
            round++;
            if (field.checkIfInRow(position)) {
                gameEnd("Player " + player + " won");
            } else if (round > (field.getLengthRow() * field.getLengthColumn())) {
                gameEnd("It is draw!");
            } else {
                choosePlayer();
                updateTitleText();
            }
        } else {
            popUpToast("Box is already used!");
        }
    }

    private boolean writeSymbolToField() {
        if (field.writeToField(position, player)) {
            actualButton.setText(player);
            return true;
        } else {
            return false;
        }
    }

    private void choosePlayer() {
        if ((round % 2) == 0) {
            player = "X";
        } else {
            player = "O";
        }
    }

    private void updateTitleText() {
        getSupportActionBar().setTitle("Playing: " + player);
    }

    private void gameEnd(String announcement) {
        setContentView(R.layout.end_layout);
        TextView txtview = (TextView) findViewById(R.id.textAnnouncement);
        txtview.setText(announcement);
        getSupportActionBar().setTitle("Tic-tac-toe");
    }

    public void exitGame(View view) {finishAndRemoveTask();}

    public void repeatGame(View view) {setContentView(R.layout.input_layout);}
}
