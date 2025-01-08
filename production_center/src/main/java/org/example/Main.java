package org.example;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.entities.*;
import org.example.simulation.Workshop;
import org.example.util.ConnectionManager;
import org.example.validator.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    private static int workersCount;
    private static int detailsCount;
    private static List<ProductionCenter> centers;
    private final static int maxWorkersCount = 40;
    private final static int maxDetailsCount = 2000;
    private final static double maxPerformance = 10.0;
    public static void main(String[] args) {
        centers = new ArrayList<>();
        try {
            parseExcel();
            ProductCenterListValidator validator = new ProductCenterListValidator(centers);
            validator.validate();
            Workshop workshop = new Workshop(workersCount, detailsCount, centers);
            workshop.run();
            writeCsv(workshop.getResult());
        } catch (IOException | InvalidFormatException | ProductCenterDataException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void parseExcel() throws IOException, InvalidFormatException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя файла с входными данными");
        String fileName = scanner.nextLine();
        File file = new File(fileName);
        if (!file.exists() || !file.canRead())
            throw new ProductCenterDataException("Неправильный файл");
        Workbook workbook = new XSSFWorkbook(file);
        parseScenario(workbook);
        parseProductionCenter(workbook);
        parseConnection(workbook);
        workbook.close();
    }
    private static void parseConnection(Workbook workbook) {
        //Sheet sheet3 = workbook.getSheetAt(2);
        String thirdSheetName = "Connection";
        Sheet sheet3 = workbook.getSheet(thirdSheetName);
        if (sheet3 == null)
            throw new ProductCenterDataException(String.format("Нет листа с именем \"%s\"", thirdSheetName));
        int dataRow = 1;
        if (sheet3.getFirstRowNum() == -1) throw new ProductCenterDataException("Пустой лист");
        if (sheet3.getRow(0).getCell(0).getStringCellValue().isEmpty()) {
            dataRow = 2;
        }
        ConnectionManager connectionManager = new ConnectionManager(centers);
        for (Row row : sheet3) {
            if (row.getRowNum() < dataRow) continue;
            String sourceCenter = row.getCell(0).getStringCellValue();
            String destinationCenter = row.getCell(1).getStringCellValue();
            connectionManager.linkCenters(sourceCenter, destinationCenter);
        }
    }
    private static void parseProductionCenter(Workbook workbook) {
        //Sheet sheet2 = workbook.getSheetAt(1);
        String secondSheetName = "ProductionCenter";
        Sheet sheet2 = workbook.getSheet(secondSheetName);
        if (sheet2 == null)
            throw new ProductCenterDataException(String.format("Нет листа с именем \"%s\"", secondSheetName));
        int dataRow = 1;
        if (sheet2.getFirstRowNum() == -1) throw new ProductCenterDataException("Пустой лист");
        if (sheet2.getRow(0).getCell(0).getStringCellValue().isEmpty()) {
            dataRow = 2;
        }
        int last = sheet2.getLastRowNum();
        for (int i = dataRow; i <= last; i++) {
            Row row = sheet2.getRow(i);
            String id = row.getCell(0).getStringCellValue();
            String name = row.getCell(1).getStringCellValue();
            double performance = row.getCell(2).getNumericCellValue();
            if (!checkPerformance(performance)) throw new ProductCenterDataException(String.format(
                        "Макс. время на обработку одной детали: %.1f мин, а также больше нуля", maxPerformance));
            int maxWorkers = (int) row.getCell(3).getNumericCellValue();
            centers.add(new ProductionCenter(id, name, performance, maxWorkers));
        }
    }
    private static void parseScenario(Workbook workbook) {
        //Sheet sheet1 = workbook.getSheetAt(0);
        String firstSheetName = "Scenario";
        Sheet sheet1 = workbook.getSheet(firstSheetName);
        if (sheet1 == null)
            throw new ProductCenterDataException(String.format("Нет листа с именем \"%s\"", firstSheetName));
        int dataRow = 1;
        if (sheet1.getFirstRowNum() == -1) throw new ProductCenterDataException("Пустой лист");
        if (sheet1.getRow(0).getCell(0).getStringCellValue().isEmpty()) {
            dataRow = 2;
        }
        int w = (int) sheet1.getRow(dataRow).getCell(0).getNumericCellValue();
        int d = (int) sheet1.getRow(dataRow).getCell(1).getNumericCellValue();
        if (!checkScenario(w, d))
            throw new ProductCenterDataException(String.format(
                    "Ожидаемая размерность данных (числа положительные): количество сотрудников - до %d, " +
                        "количество деталей - до %d", maxWorkersCount, maxDetailsCount));
        workersCount = w;
        detailsCount = d;
    }
    private static void writeCsv(List<OutputEntity> list) {
        String fileName = "result.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Time, ProductionCenter, WorkersCount, BufferCount");
            writer.newLine();
            for (OutputEntity row : list) {
                writer.write(row.toString());
                writer.newLine();
            }
            System.out.printf("Результат записан в файл \"%s\"%n", fileName);
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
        }
    }
    protected static boolean checkScenario(int w, int d) {
        boolean workersQuantity = w > 0 && w <= maxWorkersCount;
        boolean detailsQuantity = d > 0 && d <= maxDetailsCount;
        return workersQuantity && detailsQuantity;
    }
    protected static boolean checkPerformance(double performance) {
        return performance > 0 && performance <= maxPerformance;
    }
}
