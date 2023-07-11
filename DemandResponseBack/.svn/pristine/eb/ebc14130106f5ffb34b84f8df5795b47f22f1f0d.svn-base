package com.xqxy.core.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class POIExcelUtil {

    static HSSFCellStyle cellStyle = null;
    static HSSFFont font = null;
    static HSSFCellStyle cellStyle1 = null;

    public static void generatorExcel(String excelName, List<Map<String, Object>> sheetList, HttpServletResponse response) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        cellStyle = workbook.createCellStyle();
        font = workbook.createFont();
        cellStyle1 = workbook.createCellStyle();
        for (Map<String, Object> map : sheetList) {
            //填充表格内容，设置样式
            setSheetContent(workbook, (String) map.get("titleName"), (String) map.get("sheetName"), (String[]) map.get("titleRow"), (String[][]) map.get("dataRows"));
        }

        exportStream(response, excelName, workbook);
    }

    public static void generatorExcelNoTitle(String excelName, String titleName, String sheetName, String[] titleRow, String[][] dataRows, HttpServletResponse response) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        cellStyle = workbook.createCellStyle();
        font = workbook.createFont();
        cellStyle1 = workbook.createCellStyle();
        //填充表格内容，设置样式
        setSheetContentNoTitle(workbook, titleName, sheetName, titleRow, dataRows);

        exportStream(response, excelName, workbook);
    }

    public static void generatorExcel(String excelName, String titleName, String sheetName, String[] titleRow, String[][] dataRows, HttpServletResponse response) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        cellStyle = workbook.createCellStyle();
        font = workbook.createFont();
        cellStyle1 = workbook.createCellStyle();
        //填充表格内容，设置样式
        setSheetContent(workbook, titleName, sheetName, titleRow, dataRows);

        exportStream(response, excelName, workbook);
    }

    public static void generatorExcel(String excelName, String titleName, String sheetName, String[] titleRow, String[] titleChiRow, String dateNow, String[][] dataRows, HttpServletResponse response) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        cellStyle = workbook.createCellStyle();
        font = workbook.createFont();
        cellStyle1 = workbook.createCellStyle();
        //填充表格内容，设置样式
        if(titleChiRow != null && titleChiRow.length != 0) {
            setSheetContent(workbook, titleName, sheetName, titleRow, titleChiRow, dateNow, dataRows);
        } else {
            setSheetContent(workbook, titleName, sheetName, titleRow, dateNow, dataRows);
        }

        exportStream(response, excelName, workbook);
    }

    public static void generatorExcel(String excelName, Map<String, Object> titleNames, String sheetName, Map<String, Map<String, Object>> headerRows,  String[][] dataRows, HttpServletResponse response) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        cellStyle = workbook.createCellStyle();
        font = workbook.createFont();
        cellStyle1 = workbook.createCellStyle();
        //填充表格内容，设置样式
        setSheetContent(workbook, titleNames, sheetName, headerRows, dataRows);

        exportStream(response, excelName, workbook);
    }

    public static void setSheetContent(HSSFWorkbook workbook, Map<String, Object> titleNames, String sheetName, Map<String, Map<String, Object>> headerRows,  String[][] dataRows) {

        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 创建表头
        HSSFRow title = sheet.createRow(0);
        sheet.setDefaultColumnWidth((short) 27);
        String[] titles = (String[]) titleNames.get("titleName");
        int len = (int) titleNames.get("colCount");
        int[] startRows = (int[]) titleNames.get("startRow");
        int[] endRows = (int[]) titleNames.get("endRow");
        int[] startCols = (int[]) titleNames.get("startCol");
        int[] endCols = (int[]) titleNames.get("endCol");

        for (int i = 0; i < titles.length; i ++) {
            HSSFCell titleCell = title.createCell(startCols[i]);
            titleCell.setCellValue(titles[i]);
            setTitleStyle(workbook, sheet, title, 810, i == 0 ? 27 : 21, startCols[i], startRows[i], endRows[i], startCols[i], endCols[i]);
        }

        // 创建标题行
        Map<String, Object> verticalMap = headerRows.get("vertical");
        Map<String, Object> levelMap = headerRows.get("level");
        Map<String, Object> normalMap = headerRows.get("normal");

        setHeaderContent(workbook, sheet, verticalMap, "vertical");
        setHeaderContent(workbook, sheet, levelMap, "level");
        setHeaderContent(workbook, sheet, normalMap, "normal");

        // 创建内容行
        for (int i = 0; i < dataRows.length; i ++) {
            HSSFRow dataRow = sheet.createRow(i + 3);
            for (int j = 0; j < dataRows[i].length; j ++) {
                HSSFCell dataCell = dataRow.createCell(j);
                if (dataRows[i][j] != null && !dataRows[i][j].equals("") && !dataRows[i][j].equals("null")) {
                    dataCell.setCellValue(dataRows[i][j]);
                } else {
                    dataCell.setCellValue("");
                }
            }
            setDataStyle(workbook, dataRow, len);
        }
    }

    public static void setSheetContent(HSSFWorkbook workbook, String titleName, String sheetName, String[] titleRow,  String[] titleChiRow, String dateNow, String[][] dataRows) {

        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 创建表头
        HSSFRow title = sheet.createRow(0);
        HSSFCell titleCell = title.createCell(0);
        titleCell.setCellValue(titleName);
        HSSFCell titleCell1 = title.createCell(titleChiRow.length-2);
        titleCell1.setCellValue("统计日期");
        HSSFCell titleCell2 = title.createCell(titleChiRow.length-1);
        titleCell2.setCellValue(dateNow);
        sheet.setDefaultColumnWidth((short) 27);
        setTitleStyle(workbook, sheet, title, 0, titleChiRow.length, 2);

        // 创建标题行
        HSSFRow headerRow1 = sheet.createRow(1);
        for (int i = 0; i < titleChiRow.length; i ++) {
            HSSFCell headerCell = headerRow1.createCell(i);
            if (i == 0) {
                headerCell.setCellValue(titleRow[0]);
            }
            if (i == 6) {
                headerCell.setCellValue(titleRow[1]);
            }
        }
        setHeaderStyle(workbook, headerRow1, titleChiRow.length);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 9));

        // 创建标题行
        HSSFRow headerRow = sheet.createRow(2);
        for (int i = 0; i < titleChiRow.length; i ++) {
            HSSFCell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(titleChiRow[i]);
        }
        setHeaderStyle(workbook, headerRow, titleChiRow.length);

        // 创建内容行
        for (int i = 0; i < dataRows.length; i ++) {
            HSSFRow dataRow = sheet.createRow(i + 3);
            for (int j = 0; j < dataRows[i].length; j ++) {
                HSSFCell dataCell = dataRow.createCell(j);
                if (dataRows[i][j] != null && !dataRows[i][j].equals("") && !dataRows[i][j].equals("null")) {
                    dataCell.setCellValue(dataRows[i][j]);
                } else {
                    dataCell.setCellValue("");
                }
            }
            setDataStyle(workbook, dataRow, titleChiRow.length);
        }
    }


    public static void setSheetContent(HSSFWorkbook workbook, String titleName, String sheetName, String[] titleRow, String dateNow, String[][] dataRows) {

        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 创建表头
        HSSFRow title = sheet.createRow(0);
        HSSFCell titleCell = title.createCell(0);
        titleCell.setCellValue(titleName);
        HSSFCell titleCell1 = title.createCell(titleRow.length-2);
        titleCell1.setCellValue("统计日期");
        HSSFCell titleCell2 = title.createCell(titleRow.length-1);
        titleCell2.setCellValue(dateNow);
        sheet.setDefaultColumnWidth((short) 27);
        setTitleStyle(workbook, sheet, title, 0, titleRow.length, 2);


        // 创建标题行
        HSSFRow headerRow = sheet.createRow(1);
        for (int i = 0; i < titleRow.length; i ++) {
            HSSFCell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(titleRow[i]);
        }
        setHeaderStyle(workbook, headerRow, titleRow.length);

        // 创建内容行
        for (int i = 0; i < dataRows.length; i ++) {
            HSSFRow dataRow = sheet.createRow(i + 2);
            for (int j = 0; j < dataRows[i].length; j ++) {
                HSSFCell dataCell = dataRow.createCell(j);
                if (dataRows[i][j] != null && !dataRows[i][j].equals("") && !dataRows[i][j].equals("null")) {
                    dataCell.setCellValue(dataRows[i][j]);
                } else {
                    dataCell.setCellValue("");
                }
            }
            setDataStyle(workbook, dataRow, titleRow.length);
        }
    }


    public static void setSheetContentNoTitle(HSSFWorkbook workbook, String titleName, String sheetName, String[] titleRow, String[][] dataRows) {

        HSSFSheet sheet = workbook.createSheet(sheetName);

        // 创建表头
        HSSFRow title = sheet.createRow(0);
        HSSFCell titleCell = title.createCell(0);
//        titleCell.setCellValue(titleName);
        sheet.setDefaultColumnWidth((short) 30);
//        setTitleStyle(workbook, sheet, title, 0, titleRow.length);

        // 创建标题行
        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < titleRow.length; i ++) {
            HSSFCell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(titleRow[i]);
        }
        setHeaderStyle(workbook, headerRow, titleRow.length);

        // 创建内容行
        for (int i = 0; i < dataRows.length; i ++) {
            HSSFRow dataRow = sheet.createRow(i + 1);
            for (int j = 0; j < dataRows[i].length; j ++) {
                HSSFCell dataCell = dataRow.createCell(j);
                if (dataRows[i][j] != null && !dataRows[i][j].equals("") && !dataRows[i][j].equals("null")) {
                    dataCell.setCellValue(dataRows[i][j]);
                } else {
                    dataCell.setCellValue("");
                }
            }
            setDataStyle(workbook, dataRow, titleRow.length);
        }
    }


    public static void setSheetContent(HSSFWorkbook workbook, String titleName, String sheetName, String[] titleRow, String[][] dataRows) {

        HSSFSheet sheet = workbook.createSheet(sheetName);

        // 创建表头
        HSSFRow title = sheet.createRow(0);
        HSSFCell titleCell = title.createCell(0);
        titleCell.setCellValue(titleName);
        sheet.setDefaultColumnWidth((short) 27);
        setTitleStyle(workbook, sheet, title, 0, titleRow.length);

        // 创建标题行
        HSSFRow headerRow = sheet.createRow(1);
        for (int i = 0; i < titleRow.length; i ++) {
            HSSFCell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(titleRow[i]);
        }
        setHeaderStyle(workbook, headerRow, titleRow.length);

        // 创建内容行
        for (int i = 0; i < dataRows.length; i ++) {
            HSSFRow dataRow = sheet.createRow(i + 2);
            for (int j = 0; j < dataRows[i].length; j ++) {
                HSSFCell dataCell = dataRow.createCell(j);
                if (dataRows[i][j] != null && !dataRows[i][j].equals("") && !dataRows[i][j].equals("null")) {
                    dataCell.setCellValue(dataRows[i][j]);
                } else {
                    dataCell.setCellValue("");
                }
            }
            setDataStyle(workbook, dataRow, titleRow.length);
        }
    }

    public static void setTitleStyle(HSSFWorkbook workbook, HSSFSheet sheet, HSSFRow titleRow, int height, int fontSize, int index, int startRow, int endRow, int startCol, int endCol) {

        if (endCol - startCol > 1) {
            sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCol, endCol));
        }
        titleRow.setHeight((short) height);
        titleRow.getCell(index).setCellStyle(getAlignCenter(workbook, (short) fontSize, true, false));
    }

    public static void setHeaderContent(HSSFWorkbook workbook, HSSFSheet sheet, Map<String, Object> map, String type) {

        String[] headerRow = (String[]) map.get("headerRow");
        int[] startRows = (int[]) map.get("startRow");
        int[] endRows = (int[]) map.get("endRow");
        int[] startCols = (int[]) map.get("startCol");
        int[] endCols = (int[]) map.get("endCol");
        int len = headerRow.length;

        if (type.equals("vertical")) {
            for (int j = startRows[0]; j <= endRows[0]; j ++) {
                HSSFRow headerMainRow = sheet.getRow(j);
                if (headerMainRow == null) {
                    headerMainRow = sheet.createRow(j);
                }
                for (int i = startCols[0]; i <= endCols[len - 1]; i ++) {
                    HSSFCell headerCell = headerMainRow.createCell(i);
                    if (j == startRows[0]) {
                        headerCell.setCellValue(headerRow[i]);
                    }
                }
                setHeaderStyle(workbook, headerMainRow, startCols[0], startCols[0] + len);
            }
            for (int i = startCols[0]; i <= endCols[len - 1]; i ++) {
                sheet.addMergedRegion(new CellRangeAddress(startRows[i], endRows[i], startCols[i], endCols[i]));
            }
        } else if (type.equals("level")) {
            for (int j = startRows[0]; j <= endRows[0]; j ++) {
                HSSFRow headerMainRow = sheet.getRow(j);
                if (headerMainRow == null) {
                    headerMainRow = sheet.createRow(j);
                }

                for (int i = startCols[0]; i <= endCols[len - 1]; i++) {
                    HSSFCell headerCell = headerMainRow.createCell(i);
                    for (int k = 0; k < len; k ++) {
                        if (i == startCols[k]) {
                            headerCell.setCellValue(headerRow[k]);
                        }
                    }
                }
                setHeaderStyle(workbook, headerMainRow, startCols[0], endCols[len - 1] + 1);

                for (int k = 0; k < len; k ++) {
                    sheet.addMergedRegion(new CellRangeAddress(startRows[k], endRows[k], startCols[k], endCols[k]));
                }
            }
        } else {
            for (int i = 0; i < len; i ++) {
                HSSFRow headerMainRow = sheet.getRow(startRows[i]);
                if (headerMainRow == null) {
                    headerMainRow = sheet.createRow(startRows[i]);
                }
                HSSFCell headerCell = headerMainRow.createCell(startCols[i]);
                headerCell.setCellValue(headerRow[i]);
                if (i == len - 1) {
                    setHeaderStyle(workbook, headerMainRow, startCols[0], startCols[0] + len);
                }
            }
        }
    }

    public static void setTitleStyle(HSSFWorkbook workbook, HSSFSheet sheet, HSSFRow titleRow, int rowNum, int colLen) {

        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, colLen - 1));
        titleRow.setHeight((short) 810);
        titleRow.getCell(0).setCellStyle(getAlignCenter(workbook, (short) 27, true));
    }

    public static void setTitleStyle(HSSFWorkbook workbook, HSSFSheet sheet, HSSFRow titleRow, int rowNum, int colLen, int param) {

        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, colLen - 3));
        titleRow.setHeight((short) 810);
        titleRow.getCell(0).setCellStyle(getAlignCenter(workbook, (short) 27, true));
        titleRow.getCell(colLen - 2).setCellStyle(getAlignCenter(workbook, (short) 18, true, false));
        titleRow.getCell(colLen - 1).setCellStyle(getAlignCenter(workbook, (short) 18, true, false));
    }

    public static void setHeaderStyle(HSSFWorkbook workbook, HSSFRow headerRow, int len) {

        headerRow.setHeight((short) 510);
        for (int i = 0; i < len; i ++) {
            headerRow.getCell(i).setCellStyle(getAlignCenter(workbook, (short) 15, true));
        }
    }

    public static void setHeaderStyle(HSSFWorkbook workbook, HSSFRow headerRow, int startIndex, int len) {

        headerRow.setHeight((short) 510);
        for (int i = startIndex; i < len; i ++) {
            headerRow.getCell(i).setCellStyle(getAlignCenter(workbook, (short) 15, true));
        }
    }

    public static void setDataStyle(HSSFWorkbook workbook, HSSFRow dataRow, int len) {

        dataRow.setHeight((short) 510);

        for (int i = 0; i < len; i ++) {
            dataRow.getCell(i).setCellStyle(getAlignCenter(workbook, (short) 15, false, "date"));
        }
    }

    public static HSSFCellStyle getAlignCenter(HSSFWorkbook workbook, short size, boolean isBold, String date) {

//        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(getFont(workbook, size, isBold));
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);

        return cellStyle;
    }

    public static HSSFCellStyle getAlignCenter(HSSFWorkbook workbook, short size, boolean isBold) {

        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(getFont(workbook, size, isBold));
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);

        return cellStyle;
    }

    public static HSSFCellStyle getAlignCenter(HSSFWorkbook workbook, short size, boolean isBold, boolean isBorder) {

//        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.CENTER);
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle1.setFont(getFont(workbook, size, isBold));

        return cellStyle1;
    }

    public static HSSFFont getFont(HSSFWorkbook workbook, short size, boolean isBold) {

//        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(size);
        font.setBold(isBold);
        font.setFontName("宋体");

        return font;
    }

    public static void exportStream(HttpServletResponse response, String excelName, HSSFWorkbook workbook) {

        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(excelName, "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
             e.printStackTrace();
        }
    }
}
