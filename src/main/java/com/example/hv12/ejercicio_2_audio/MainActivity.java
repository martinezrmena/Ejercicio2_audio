package com.example.hv12.ejercicio_2_audio;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    TextView lblActual,lblFin;
    Button btnIniciar,btnReiniciar;
    ProgressBar progreso;
    AudioAsincrono audioAsincrono;
    boolean valor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lblActual    = findViewById(R.id.lblActual);
        lblFin       = findViewById(R.id.lblFin);
        btnIniciar   = findViewById(R.id.btnIniciar);
        btnReiniciar = findViewById(R.id.btnReiniciar);
        progreso = findViewById(R.id.prbProgreso);



        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciar();
            }
        });


        btnReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciar();
            }
        });

        btnReiniciar.setEnabled(false);

    }

    public void reiniciar(){
        audioAsincrono.reiniciarReproductor();

        if(audioAsincrono==null){
            audioAsincrono = new AudioAsincrono(this,lblActual,lblFin, progreso);
            audioAsincrono.execute();
            btnIniciar.setText("Pausar");
            /**si ha terminado de ejecutar el hilo -> se crea otro hilo **/
        }else if(audioAsincrono.getStatus()== AsyncTask.Status.FINISHED){
            audioAsincrono = new AudioAsincrono(this,lblActual,lblFin, progreso);
            audioAsincrono.execute();
            btnIniciar.setText("Pausar");
            /** si esta ejecutado y no esta pausado -> entonces se pausa**/
        }else if(audioAsincrono.getStatus()== AsyncTask.Status.RUNNING){
            btnIniciar.setText("Pausar");
            audioAsincrono.reanudarAudio();
        }
    }

    private void iniciar(){
        btnReiniciar.setEnabled(true);

        if(audioAsincrono==null){
            audioAsincrono = new AudioAsincrono(this,lblActual,lblFin, progreso);
            audioAsincrono.execute();
            btnIniciar.setText("Pausar");
            /**si ha terminado de ejecutar el hilo -> se crea otro hilo **/
        }else if(audioAsincrono.getStatus()== AsyncTask.Status.FINISHED){
            audioAsincrono = new AudioAsincrono(this,lblActual,lblFin, progreso);
            audioAsincrono.execute();
            btnIniciar.setText("Pausar");
            /** si esta ejecutado y no esta pausado -> entonces se pausa**/
        }else if(audioAsincrono.getStatus()== AsyncTask.Status.RUNNING && !audioAsincrono.esPausa()  ){
            audioAsincrono.pausarAudio();
            btnIniciar.setText("Iniciar");
            /** si no entro en las condiciones anteriores por defecto esta pausado -> se reanuda*/
        }else{
            btnIniciar.setText("Pausar");
            audioAsincrono.reanudarAudio();
        }
    }
}
