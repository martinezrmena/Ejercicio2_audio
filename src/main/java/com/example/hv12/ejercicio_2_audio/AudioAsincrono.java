package com.example.hv12.ejercicio_2_audio;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 7/9/18.
 */

public class AudioAsincrono extends AsyncTask<Void,String,String> {

    Context context;
    TextView lblActual,lblFin;
    ProgressBar prbProgreso;

    MediaPlayer reproductorMusica;


    boolean pausa=false;

    boolean reinicio = false;

    private String VIGILANTE = "vigilante";

    public AudioAsincrono(Context context, TextView lblActual, TextView lblFin, ProgressBar progreso) {
        this.context = context;
        this.lblActual = lblActual;
        this.lblFin = lblFin;
        this.prbProgreso = progreso;

    }

    @Override
    protected String doInBackground(Void... voids) {
        reproductorMusica.start();

        while (reproductorMusica.isPlaying()){
            esperarUnSegundo();
            publishProgress(tiempo(reproductorMusica.getCurrentPosition()), reproductorMusica.getCurrentPosition()+ "");
            if(pausa==true){
                synchronized (VIGILANTE){
                    try {
                        /**realiza pausa  en el hilo**/
                        reproductorMusica.pause();
                        VIGILANTE.wait();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }/**sale del sincronized por lo que ya no hay pausa*/
                    pausa = false;
                    reproductorMusica.start();
                }

            }

            if (reinicio){
                reproductorMusica.seekTo(0);
                reinicio = false;
            }
        }

        return null;
    }


    public boolean esPausa(){
        return pausa;
    }

    public void pausarAudio(){
        pausa = true;
    }

    /** notifica a VIGILANTE en todas sus llamadas con syncronized**/
    public void reanudarAudio(){
        synchronized (VIGILANTE){
            VIGILANTE.notify();
        }
    }

    public void reiniciarReproductor(){
        reinicio = true;
    }


    private void esperarUnSegundo() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignore) {}
    }


    @Override
    protected void onProgressUpdate(String... values) {
        lblActual.setText(values[0]);
        int progress = Integer.parseInt(values[1]);
        prbProgreso.setProgress(progress);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        reproductorMusica = MediaPlayer.create(context,R.raw.tone);
        long fin = reproductorMusica.getDuration();

        prbProgreso.setMax((int)fin);

        lblFin.setText(tiempo(fin));
        super.onPreExecute();
    }

    private String tiempo(long t){
        long fin_min = TimeUnit.MILLISECONDS.toMinutes(t);
        long fin_sec = TimeUnit.MILLISECONDS.toSeconds(t) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(t));
        return fin_min+":"+fin_sec;
    }


}
