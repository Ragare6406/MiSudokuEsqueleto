package com.example.sudokuesqueleto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogFinPartida extends DialogFragment {
    int n;
    private GameBoard gameBoard;
    GameBoard.OnGameOverListener dFinPartidaListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Enhorabuena!! Partida completada");
        builder.setMessage("¿Desea jugar otra partida?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (RemovedNumber removedNumber : gameBoard.removedNumbers) {
                    gameBoard.editableCells[removedNumber.row][removedNumber.col] = false; //evita que se modifiquen los numeros correctos una vez terminado el juego
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        });
        
        return builder.create();
    }
    // Setter para establecer el valor de n
     public void setN(int n) {
       this.n = n;
    }

    // Setter para establecer la referencia al GameBoard
    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            dFinPartidaListener = (GameBoard.OnGameOverListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
