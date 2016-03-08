package XLSProcess;

import DBOperaciones.DBCrud;
import Log.LogEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Iterator;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * * @author Misael Recinos
 */
public class FileXLS_Process {

    String PATH = new GetParameters().getPath("P_PATH_FARINO");
    String PATH_O = new GetParameters().getPath("P_PATH_FARINO_OUT");
    //String PATH = "D:/Harisa/ServicioFarino/farino/";
    String[] valores_table1 = {"DisplayNameIDParametersUser", "DisplayNameIDParametersSample"};
    String[] valores_table3 = {"DisplayNameIDEvaluationPointsWaterAbsorptionReal", "DisplayNameIDEvaluationPointsStability",
        "DisplayNameIDEvaluationPointsToleranceIndex", "DisplayNameIDEvaluationPointsFQZ",
        "DisplayNameIDEvaluationPointsBreakDownPoint"};
    int index = 0;
    String row_principal = "";
    int first_row = 0;

    List resultados = new ArrayList<>();
    List graficos = new ArrayList<>();

    public void ReadExcel(String f) {
        String file_path = PATH + f + ".xls";
        try {
            FileInputStream file = new FileInputStream(new File(file_path));

            org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(file);

            org.apache.poi.ss.usermodel.Sheet table1 = workbook.getSheetAt(0);
            org.apache.poi.ss.usermodel.Sheet table2 = workbook.getSheetAt(1);
            org.apache.poi.ss.usermodel.Sheet table3 = workbook.getSheetAt(2);

            resultados.add(f);
            ReadTableXlS(table1, 1);
            //graficos = resultados;
            ReadTableXlS(table3, 2);
            //ReadTableXlSG(table2);
            file.close();
            new DBCrud().insertValues(resultados);
            findFileForCopy(f);
        } catch (IOException | InvalidFormatException e) {
            Logger.getLogger(FileXLS_Process.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("Error FileCSV_Process: " + e.getMessage());
            new LogEvent().LogFarino("Error FileCSV_Process: " + e.getMessage());
        }
    }

    public void ReadTableXlS(org.apache.poi.ss.usermodel.Sheet table, int array) {
        org.apache.poi.ss.usermodel.Sheet sheet = table;

        String[] valores = null;

        switch (array) {
            case 1:
                valores = valores_table1;
                new LogEvent().LogFarino("Lectura en tabla1");
                break;
            case 2:
                valores = valores_table3;
                break;
        }

        Iterator<Row> rowtable1 = sheet.iterator();
        while (rowtable1.hasNext()) {
            Row row = rowtable1.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            row_principal = "";
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                String celda_descripcion = cell.getStringCellValue();
                //System.out.print(cell.getStringCellValue() + "index: " + cell.getColumnIndex() + "\t");

                if (celda_descripcion.equals(valores[index])) {
                    row_principal = cell.getStringCellValue();
                    //System.out.print(cell.getStringCellValue() + "\t");
                }

                if (row_principal.equals(valores[index]) && cell.getColumnIndex() == 2) {
                    //System.out.println(cell.getStringCellValue() + "\t");
                    new LogEvent().LogFarino("Valor recuperado: " + cell.getStringCellValue());
                    resultados.add(cell.getStringCellValue());
                    index++;
                }

                if (index == valores.length) {
                    index = 0;
                }
            }
        }
    }

    public List getFilePath() {
        // Aquí la carpeta que queremos explorar
        String path = PATH;
        String files;

        int count = 0;
        List listaArchivos = new ArrayList<>();

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                files = listOfFile.getName();
                if (files.endsWith(".xls") || files.endsWith(".xlsx")) {
                    int c = files.indexOf(".");
                    String newFile = files.substring(0, c);

                    listaArchivos.add(newFile);
                    System.out.println(files);
                    count++;
                }
            }
        }
        return listaArchivos;
    }

    public void ProcessXLS() {
        List xls = getFilePath();

        for (int i = 0; i < xls.size(); i++) {
            new FindXLS().FileSearch(String.valueOf(xls.get(i)));
        }
    }

    public void findFileForCopy(String fileName) {
        // Aquí la carpeta que queremos explorar
        String path = PATH;
        String files;

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                files = listOfFile.getName();
                int c = files.indexOf(".");
                String newFile = files.substring(0, c);

                if (newFile.equals(fileName)) {

                    System.out.println(files);
                    copyFiles(files);
                }
            }
        }
    }

    public void copyFiles(String fileName) {

        String nfile = fileName;
        String PATH_IN = PATH + "/";
        String PATH_OUT = PATH_O;
        PATH_IN += nfile;
        PATH_OUT += nfile;

        boolean alreadyExists = new File(PATH_OUT).exists();

        Path FROM = Paths.get(PATH_IN);
        Path TO = Paths.get(PATH_OUT);

        CopyOption[] options = new CopyOption[]{
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.COPY_ATTRIBUTES
        };

        if (alreadyExists) {
            try {

                Files.deleteIfExists(TO);
                Files.copy(FROM, TO, options);
            } catch (Exception e) {
                System.err.println("Error en copia de registro");
                new LogEvent().LogFarino("Error en copia de registro " + e.getMessage());
            } catch (NoClassDefFoundError e) {
                System.err.println("Error en clases file_process copy: " + e.getMessage());
                new LogEvent().LogFarino("Error en clases file_process copy: " + e.getMessage());
            }
        } else {
            try {

                Files.copy(FROM, TO, options);
                new LogEvent().LogFarino("Copia de archivo: " + fileName);
                new LogEvent().LogFarino("Fin de operación");
                new LogEvent().LogFarino("--------------**************--------------");
            } catch (Exception e) {
                new LogEvent().LogFarino("Error en creación de documento: " + e.getMessage());
                System.err.println("Error en creacion de registro" + e.getMessage());
            } catch (NoClassDefFoundError e) {
                new LogEvent().LogFarino("Error en clases file_process create: " + e.getMessage());
                System.err.println("Error en clases file_process create: " + e.getMessage());
            }
        }
    }

    public void findFile() {
        // Aquí la carpeta que queremos explorar
        //String path = PATH;
        String path = PATH_O;
        String files;
        int count = 0;

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                files = listOfFile.getName();
                if (files.endsWith(".mdb") || files.endsWith(".pdf") || files.endsWith(".xls") || files.endsWith(".xml")) {
                    int c = files.indexOf(".");
                    String newFile = files.substring(0, c);
                    //System.out.println(newFile);
                    System.out.println(files);
                    exitingFile(files);
                    count++;
                }
            }
        }
        //System.out.println("Fin " + count);
    }

    public void exitingFile(String file) {
        String filename = file;
        String PATH_IN = PATH + "/";
        String PATH_OUT = PATH_O;
        PATH_IN += filename;
        PATH_OUT += filename;

        boolean alreadyExists = new File(PATH_OUT).exists();

        Path FROM = Paths.get(PATH_IN);

        if (alreadyExists) {
            try {
                Files.deleteIfExists(FROM);
                new LogEvent().LogFarino("Transferencia de documento a nuevo directorio: " + file);
            } catch (IOException ex) {
                System.err.println("Error en eliminar archivo");
                new LogEvent().LogFarino("Error en eliminar archivo" + ex.getMessage());
            }
        }
    }

    // Ingresar Gráficos de farino
   /* public void ReadTableXlSG(org.apache.poi.ss.usermodel.Sheet table) {
     org.apache.poi.ss.usermodel.Sheet sheet = table;

     Iterator<Row> rowtable1 = sheet.iterator();

     while (rowtable1.hasNext()) {
     Row row = rowtable1.next();
     Iterator<Cell> cellIterator = row.cellIterator();
     //System.out.println("fila cantidad: "+row.getPhysicalNumberOfCells());
     row_principal = "";
            
     while (cellIterator.hasNext()) {

     Cell cell = cellIterator.next();
                
     System.out.print(cell.getStringCellValue() + '\t');

     }
     first_row++;
     System.out.println("");
     }
     System.out.println("total filas: " + first_row);
     }*/
}
