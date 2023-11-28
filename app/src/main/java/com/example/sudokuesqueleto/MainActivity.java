package com.example.sudokuesqueleto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

public class MainActivity extends AppCompatActivity implements GameBoard.OnGameOverListener {
    Button bt1, bt2, bt3, bt4, bt5, bt6, bt7,bt8,bt9;
    GameBoard gameBoard;
    int n;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.comoJugar){
            ComoJugarDialogFragment cjDialog = new ComoJugarDialogFragment();
            cjDialog.show(getSupportFragmentManager(),"Como jugar");

        } else if (id==R.id.facil){
            n=20;
            gameBoard.resetBoard(n);
        } else if (id==R.id.normal) {
            n=30;
            gameBoard.resetBoard(n);
        } else if (id==R.id.dificil) {
            n=40;
            gameBoard.resetBoard(n);
        } else if (id==R.id.nuevaPartida) {
            if(n==0){
                n=2;
                gameBoard.resetBoard(n);
            }else{
                gameBoard.resetBoard(n);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        bt4 = findViewById(R.id.bt4);
        bt5 = findViewById(R.id.bt5);
        bt6 = findViewById(R.id.bt6);
        bt7 = findViewById(R.id.bt7);
        bt8 = findViewById(R.id.bt8);
        bt9 = findViewById(R.id.bt9);
        gameBoard = findViewById(R.id.gameBoard);

        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onNumberButtonClick(1);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onNumberButtonClick(2);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onNumberButtonClick(3);
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onNumberButtonClick(4);
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onNumberButtonClick(5);
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onNumberButtonClick(6);
            }
        });
        bt7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onNumberButtonClick(7);
            }
        });
        bt8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onNumberButtonClick(8);
            }
        });
        bt9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onNumberButtonClick(9);
            }
        });
        gameBoard.setOnGameOverListener(this);
    }

    private void onNumberButtonClick(int num) {//metodo que recogera el numero pulsado
        gameBoard.setNumber(num);
    }

    @Override
    public void onGameOver() {
        if (gameBoard.isBoardValid()) {
            showFinPartidaDialog(); // Muestra el DialogFinPartida solo cuando el tablero está completo y correcto
        } else {
            showDialogoDerrota();// Muestra el DialogDerrota solo cuando el tablero está completo e incorrecto
        }
    }
    private void showFinPartidaDialog(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFinPartida dialogFinPartida= new DialogFinPartida();
        dialogFinPartida.setN(n);//Configura el valor n
        dialogFinPartida.setGameBoard(gameBoard);//Configura la referencia de GameBoard
        dialogFinPartida.show(fragmentManager, "DialogoFinPartida");
    }
    private void showDialogoDerrota(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogDerrota dialogDerrota= new DialogDerrota();
        dialogDerrota.show(fragmentManager, "DialogoDerrota");
    }
}