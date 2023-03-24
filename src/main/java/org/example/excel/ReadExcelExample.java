package org.example.excel;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Slf4j
public class ReadExcelExample {

    public static List<Person> readPersonExcel(File file) {
        List<Person> personList = new ArrayList<>();
        try {
            InputStream inputStream = new FileInputStream(file);
            Workbook workbook = getWorkbook(inputStream, file.getName());
            Sheet sheet= workbook.getSheet("0");
            Iterator<Row> iterator= sheet.iterator();
            while (iterator.hasNext()){
                Row nextRow=iterator.next();
                if(nextRow.getRowNum()==0){
                    continue;
                }
                //Get all cell
                Iterator<Cell> cellIterator=nextRow.iterator();
                int countIndex=0;
                while (cellIterator.hasNext()){
                    Person person=new Person();
                    Cell cell=cellIterator.next();
                    Object cellValue=getCellValue(cell);
                    if (cellValue==null||cellValue.toString().isEmpty())
                        continue;
                    switch (countIndex){
                        case 0:
                            person.setId((Integer) cellValue);
                        case 1:
                            person.setName((String)cellValue);
                        case 2:
                            person.setAddress((String)cellValue);
                        case 3:
                            person.setNumberPhone((String)cellValue);
                    }
                    personList.add(person);
                }
            }
            workbook.close();
            inputStream.close();
            return personList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //get workbook
    private static Workbook getWorkbook(InputStream inputStream, String fileName) throws IOException {
        Workbook workbook;
        if (fileName.endsWith(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (fileName.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    }

    //Get cell value
    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case FORMULA:
                Workbook workbook = cell.getSheet().getWorkbook();
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                cellValue = evaluator.evaluate(cell).getNumberValue();
                break;
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue();
            case _NONE:
            case BLANK:
            case ERROR:
            default:
                break;

        }
        return cellValue;
    }

    public static void main(String[] args) {
        String filePath="";
        List<Person> personList = readPersonExcel(new File(filePath));
        System.out.println(personList);
    }

}
