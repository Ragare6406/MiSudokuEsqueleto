package com.example.sudokuesqueleto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ComoJugarDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setTitle("¿CÓMO JUGAR?");
        builder.setMessage("Cada fila, columna y cuadrado (9 espacios cada uno) debe completarse con los números del 1 al 9, " +
                "sin repetir ningún número dentro de la fila, columa o cuadrado.")

                .setNeutralButton("Aceptar", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                         dialog.dismiss();}
        });
        return builder.create();
        }
    }