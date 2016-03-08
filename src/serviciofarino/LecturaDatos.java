/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serviciofarino;

import XLSProcess.FileXLS_Process;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Misael Recinos
 */
public class LecturaDatos {

    private final int tiempo_trabajo = 5;
    private final static long ONCE_PER_DAY = 1000 * 60 * 60 * 24;

    Timer tiempo_notificacion;

    public LecturaDatos() {
        tiempo_notificacion = new Timer();
    }

    private static Calendar getTimeExecute() {

        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 15);
        date.set(Calendar.MINUTE, 40);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        return date;
    }

    public void ServicioRead() {

        TimerTask recoger_muestras = new TimerTask() {

            @Override
            public void run() {

                FileXLS_Process excel = new FileXLS_Process();
                System.out.println("Inicio de lectura farino");
                excel.ProcessXLS();
            }
        };
        tiempo_notificacion.schedule(recoger_muestras, getTimeExecute().getTime(), tiempo_trabajo * 60 * 1000);
    }
}
