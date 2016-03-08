package serviciofarino;

import XLSProcess.FileXLS_Process;

/**
 *
 * @author Misael Recinos
 */
public class ServicioFarino {

    public static void main(String[] args) {

        FileXLS_Process excel = new FileXLS_Process();
        //excel.ProcessXLS();
        excel.findFile();
    }
}
