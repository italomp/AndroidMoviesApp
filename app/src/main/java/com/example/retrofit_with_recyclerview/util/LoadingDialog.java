package com.example.retrofit_with_recyclerview.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.retrofit_with_recyclerview.R;

public class LoadingDialog {
    Activity activity;
    AlertDialog dialog;

    public LoadingDialog(Activity myActivity){
        this.activity = myActivity;
    }

    /**
     * Crio um builder de AlertDialog e passo uma view para ele.
     * Essa view é criada a partir de um layout (loading_dialog).
     * Defino que o usuário não pode cancelar a visualização do AlertDialog.
     * Crio e exibo o dialo.
     */
    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);

        // Obtendo o layout da activity
        LayoutInflater inflater = this.activity.getLayoutInflater();

        // Passando uma view para o builder, construída a partir do layout loading_dialog
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));

        // Impedindo que o usuário destrua a visualização do dialog
        builder.setCancelable(false);

        this.dialog = builder.create();
        this.dialog.show();
    }

    public void dismiss(){
        this.dialog.dismiss();
    }
}
