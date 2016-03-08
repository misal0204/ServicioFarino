/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serviciofarino;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Misael Recinos
 */
public class CopiaDatos {

    private final int tiempo_trabajo = 10;
    private final static long ONCE_PER_DAY = 1000 * 60 * 60 * 24;

    Timer tiempo_notificacion;

    public CopiaDatos() {
        tiempo_notificacion = new Timer();
    }

    private static Calendar getTimeExecute() {

        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 15);
        date.set(Calendar.MINUTE, 43);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        return date;
    }

    public void ServicioCopy() {

        TimerTask copiar_archivos = new TimerTask() {

            @Override
            public void run() {

                //FindTxt ftxt = new FindTxt();
                System.out.println("Inicio de Copia farino");
                //ftxt.findFile();
            }
        };
        tiempo_notificacion.schedule(copiar_archivos, getTimeExecute().getTime(), tiempo_trabajo * 60 * 1000);
    }
}
