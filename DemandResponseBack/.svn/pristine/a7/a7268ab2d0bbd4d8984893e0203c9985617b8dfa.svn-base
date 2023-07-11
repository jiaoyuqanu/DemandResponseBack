package com.xqxy.dr.modular.event.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;


/**
 *
 */

public class PoiExcelUtils {

    static HSSFCellStyle cellStyleHeader = null; // excel表头样式
    static HSSFCellStyle cellStyleViceHeader = null; // excel副表头样式  二级表头
    static HSSFCellStyle cellStyleTitle = null; // excel标题样式  数据标题
    static HSSFCellStyle cellStyleData = null; // excel数据样式  数据
    static HSSFFont font = null;


    // 自定义格式表格  不带副表头
    public static void customFormatExcel(String excelName, String sheetName, Map<String, Object> headerNames, Map<String, Map<String, Object>> titleRows, String[][] dataRows, HttpServletResponse response){
        HSSFWorkbook workbook = new HSSFWorkbook();
        cellStyleHeader = workbook.createCellStyle();
        cellStyleTitle = workbook.createCellStyle();
        cellStyleData = workbook.createCellStyle();
        font = workbook.createFont();

        //填充表格内容，设置样式
        setSheetContent(workbook, sheetName, headerNames, titleRows, dataRows);
        // 导出表格
        exportStream(response, excelName, workbook);
    }

    // 自定义格式表格  合并数据单元格
    public static void customFormatExcel2(String excelName, String sheetName, Map<String, Object> headerNames, Map<String, Map<String, Object>> titleRows, String[][] dataRows, HttpServletResponse response){
        HSSFWorkbook workbook = new HSSFWorkbook();
        cellStyleHeader = workbook.createCellStyle();
        cellStyleTitle = workbook.createCellStyle();
        cellStyleData = workbook.createCellStyle();
        font = workbook.createFont();

        //填充表格内容，设置样式
        setSheetContent2(workbook, sheetName, headerNames, titleRows, dataRows);
        // 导出表格
        exportStream(response, excelName, workbook);
    }

    // 自定义格式表格  带副表头
    public static void customFormatExcelWithVice(String excelName, String sheetName, Map<String, Object> headerNames, Map<String, Object> viceHeaderNames,Map<String, Map<String, Object>> titleRows, String[][] dataRows, HttpServletResponse response){
        HSSFWorkbook workbook = new HSSFWorkbook();
        cellStyleHeader = workbook.createCellStyle();
        cellStyleViceHeader = workbook.createCellStyle();
        cellStyleTitle = workbook.createCellStyle();
        cellStyleData = workbook.createCellStyle();
        font = workbook.createFont();

        //填充表格内容，设置样式
        setSheetContentWithVice(workbook, sheetName, headerNames, viceHeaderNames,titleRows, dataRows);
        // 导出表格
        exportStream(response, excelName, workbook);
    }

    // 不带副表头Content
    public static void setSheetContent(HSSFWorkbook workbook, String sheetName, Map<String, Object> headerNames, Map<String, Map<String, Object>> headerRows, String[][] dataRows) {

        HSSFSheet sheet = workbook.createSheet(sheetName);      //创建一个Excel的Sheet
        sheet.setDefaultColumnWidth((short) 27);

        // 创建表头
        HSSFRow header = sheet.createRow(0);
        // 获取表头数据
        String[] headers = (String[]) headerNames.get("headerName");
        int len = (int) headerNames.get("colCount");             // 列数
        int titleRows = (int) headerNames.get("rowCount");       //标题行数  data内容在下一行开始
        int[] startRows = (int[]) headerNames.get("startRow");
        int[] endRows = (int[]) headerNames.get("endRow");
        int[] startCols = (int[]) headerNames.get("startCol");
        int[] endCols = (int[]) headerNames.get("endCol");


        // 设置表头元素
        for (int i = 0; i < headers.length; i++) {
            HSSFCell headerCell = header.createCell(startCols[i]);
            headerCell.setCellValue(headers[i]);
            setHeaderStyleBorder(workbook, sheet, header, 810, i == 0 ? 27 : 21, len, startRows[i], endRows[i], startCols[i], endCols[i]);
        }


        // 创建标题行
        for (int i = 0; i < headerRows.size(); i++){
            Map<String, Object> map = headerRows.get("level"+i);
            setTitleContent(workbook, sheet, map);
        }

        // 创建内容行
        for (int i = 0; i < dataRows.length; i++) {
            HSSFRow dataRow = sheet.createRow(i + titleRows);
            for (int j = 0; j < dataRows[i].length; j++) {
                HSSFCell dataCell = dataRow.createCell(j);
                if (dataRows[i][j] != null && !dataRows[i][j].equals("") && !dataRows[i][j].equals("null")) {
                    dataCell.setCellValue(dataRows[i][j]);
                } else {
                    dataCell.setCellValue("");
                }
            }
            setDataStyle(workbook, dataRow, len);  // 设置数据单元格样式
        }
    }

    // 合并数据单元格
    public static void setSheetContent2(HSSFWorkbook workbook, String sheetName, Map<String, Object> headerNames, Map<String, Map<String, Object>> headerRows, String[][] dataRows) {

        HSSFSheet sheet = workbook.createSheet(sheetName);      //创建一个Excel的Sheet
        sheet.setDefaultColumnWidth((short) 27);

        // 创建表头
        HSSFRow header = sheet.createRow(0);
        // 获取表头数据
        String[] headers = (String[]) headerNames.get("headerName");
        int len = (int) headerNames.get("colCount");             // 列数
        int titleRows = (int) headerNames.get("rowCount");       //标题行数  data内容在下一行开始
        int[] startRows = (int[]) headerNames.get("startRow");
        int[] endRows = (int[]) headerNames.get("endRow");
        int[] startCols = (int[]) headerNames.get("startCol");
        int[] endCols = (int[]) headerNames.get("endCol");


        // 设置表头元素
        for (int i = 0; i < headers.length; i++) {
            HSSFCell headerCell = header.createCell(startCols[i]);
            headerCell.setCellValue(headers[i]);
            setHeaderStyleBorder(workbook, sheet, header, 810, i == 0 ? 27 : 21, len, startRows[i], endRows[i], startCols[i], endCols[i]);
        }


        // 创建标题行
        for (int i = 0; i < headerRows.size(); i++){
            Map<String, Object> map = headerRows.get("level"+i);
            setTitleContent(workbook, sheet, map);
        }

        // 创建内容行
        for (int i = 0; i < dataRows.length; i++) {
            HSSFRow dataRow = sheet.createRow(i + titleRows);
            for (int j = 0; j < dataRows[i].length; j++) {
                HSSFCell dataCell = dataRow.createCell(j);
                if (dataRows[i][j] != null && !dataRows[i][j].equals("") && !dataRows[i][j].equals("null")) {
                    dataCell.setCellValue(dataRows[i][j]);
                } else {
                    dataCell.setCellValue("");
                }
            }
            setDataStyle(workbook, dataRow, len);  // 设置数据单元格样式
            sheet.addMergedRegion(new CellRangeAddress(i+titleRows, i+titleRows, 10, 11));  // 合并单元格
            sheet.addMergedRegion(new CellRangeAddress(i+titleRows, i+titleRows, 20, 21));  // 合并单元格
        }


    }

    // 带副表头Content
    public static void setSheetContentWithVice(HSSFWorkbook workbook, String sheetName, Map<String, Object> headerNames, Map<String, Object> viceHeaderNames,Map<String, Map<String, Object>> headerRows, String[][] dataRows) {

        HSSFSheet sheet = workbook.createSheet(sheetName);      //创建一个Excel的Sheet
        sheet.setDefaultColumnWidth((short) 27);

        // 创建表头
        HSSFRow header = sheet.createRow(0);
        // 获取表头数据
        String[] headers = (String[]) headerNames.get("headerName");
        int len = (int) headerNames.get("colCount");             // 列数
        int titleRows = (int) headerNames.get("rowCount");       //标题行数  data内容在下一行开始
        int[] startRows = (int[]) headerNames.get("startRow");
        int[] endRows = (int[]) headerNames.get("endRow");
        int[] startCols = (int[]) headerNames.get("startCol");
        int[] endCols = (int[]) headerNames.get("endCol");

        // 创建副表头
        HSSFRow viceHeader = sheet.createRow(1);
        // 获取表头数据
        String[] viceHeaders = (String[]) viceHeaderNames.get("headerName");
        int[] viceStartRows = (int[]) viceHeaderNames.get("startRow");
        int[] viceEndRows = (int[]) viceHeaderNames.get("endRow");
        int[] viceStartCols = (int[]) viceHeaderNames.get("startCol");
        int[] viceEndCols = (int[]) viceHeaderNames.get("endCol");

        // 设置表头元素
        for (int i = 0; i < headers.length; i++) {
            HSSFCell headerCell = header.createCell(startCols[i]);
            headerCell.setCellValue(headers[i]);
            setHeaderStyleBorder(workbook, sheet, header, 810, i == 0 ? 27 : 21, len, startRows[i], endRows[i], startCols[i], endCols[i]);
        }

        // 设置副表头元素
        for (int i = 0; i < viceHeaders.length; i++) {
            HSSFCell viceHeaderCell = viceHeader.createCell(startCols[i]);
            viceHeaderCell.setCellValue(viceHeaders[i]);
            setViceHeaderStyle(workbook, sheet, viceHeader, 510, 21, len, viceStartRows[i], viceEndRows[i], viceStartCols[i], viceEndCols[i]);
        }

        // 创建标题行
        for (int i = 0; i < headerRows.size(); i++){
            Map<String, Object> map = headerRows.get("level"+i);
            setTitleContent(workbook, sheet, map);
        }

        // 创建内容行
        for (int i = 0; i < dataRows.length; i++) {
            HSSFRow dataRow = sheet.createRow(i + titleRows);
            for (int j = 0; j < dataRows[i].length; j++) {
                HSSFCell dataCell = dataRow.createCell(j);
                if (dataRows[i][j] != null && !dataRows[i][j].equals("") && !dataRows[i][j].equals("null")) {
                    dataCell.setCellValue(dataRows[i][j]);
                } else {
                    dataCell.setCellValue("");
                }
            }
            setDataStyle(workbook, dataRow, len);  // 设置数据单元格样式
        }
    }

    // 创建标题行
    public static void setTitleContent(HSSFWorkbook workbook, HSSFSheet sheet, Map<String, Object> map) {

        String[] titleRow = (String[]) map.get("titleRow");
        int[] startRows = (int[]) map.get("startRow");
        int[] endRows = (int[]) map.get("endRow");
        int[] startCols = (int[]) map.get("startCol");
        int[] endCols = (int[]) map.get("endCol");
        int len = titleRow.length;

        for (int k = 0; k < len; k++){
            for (int i = startRows[k]; i <= endRows[k]; i++){
                HSSFRow titleMainRow = sheet.getRow(i);
                if (titleMainRow == null) {
                    titleMainRow = sheet.createRow(i);
                }
                for (int j = startCols[k]; j <= endCols[k]; j++){
                    HSSFCell titleCell = titleMainRow.createCell(j);
                    if (i == startRows[k] && j == startCols[k]) {
                        titleCell.setCellValue(titleRow[k]);
                    }
                }
                setTitleStyle(workbook, titleMainRow, startCols[k], endCols[k]);
            }
            if (endRows[k] > startRows[k] || endCols[k] > startCols[k]){
                sheet.addMergedRegion(new CellRangeAddress(startRows[k], endRows[k], startCols[k], endCols[k]));
            }
        }
    }

    // 设置表头行样式  无边框
    public static void setHeaderStyle(HSSFWorkbook workbook, HSSFSheet sheet, HSSFRow headerRow, int height, int fontSize, int index, int startRow, int endRow, int startCol, int endCol) {

        if (endCol - startCol > 1) {
            sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCol, endCol));  // 合并单元格
        }
        headerRow.setHeight((short) height);  // 设置行高
        headerRow.getCell(index).setCellStyle(getAlignCenterHeader(workbook, (short) fontSize, true, false)); //设置单元格样式
    }

    // 设置表头行样式  带边框
    public static void setHeaderStyleBorder(HSSFWorkbook workbook, HSSFSheet sheet, HSSFRow headerRow, int height, int fontSize, int len, int startRow, int endRow, int startCol, int endCol) {

        headerRow.setHeight((short) height);  // 设置行高

        for (int i = 0; i < len; i++){
            HSSFCell headerCell = headerRow.getCell(i);
            if (headerCell == null){
                headerRow.createCell(i);
            }

            headerRow.getCell(i).setCellStyle(getAlignCenterHeader(workbook, (short) fontSize, true, true)); //设置单元格样式
        }

        if (endCol - startCol > 1) {
            sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCol, endCol));  // 合并单元格
        }


    }

    // 设置副表头行样式
    public static void setViceHeaderStyle(HSSFWorkbook workbook, HSSFSheet sheet, HSSFRow headerRow, int height, int fontSize, int len, int startRow, int endRow, int startCol, int endCol) {

        headerRow.setHeight((short) height);  // 设置行高

        for (int i = 0; i < len; i++){
            HSSFCell headerCell = headerRow.getCell(i);
            if (headerCell == null){
                headerRow.createCell(i);
            }

            headerRow.getCell(i).setCellStyle(getAlignRightHeader(workbook, (short) fontSize, true)); //设置单元格样式
        }

        if (endCol - startCol > 1) {
            sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCol, endCol));  // 合并单元格
        }

    }

    // 设置标题行样式
    public static void setTitleStyle(HSSFWorkbook workbook, HSSFRow titleRow, int startIndex, int len) {

        titleRow.setHeight((short) 510);
        for (int i = startIndex; i <= len; i++) {
            titleRow.getCell(i).setCellStyle(getAlignCenterTitle(workbook, (short) 15, true));
        }
    }

    // 设置数据行样式
    public static void setDataStyle(HSSFWorkbook workbook, HSSFRow dataRow, int len) {

        dataRow.setHeight((short) 510);     // 设置内容行高

        for (int i = 0; i < len; i++) {
            dataRow.getCell(i).setCellStyle(getAlignCenterData(workbook, (short) 15, false));
        }
    }

    // 表头行单元格样式
    public static HSSFCellStyle getAlignCenterHeader(HSSFWorkbook workbook, short size, boolean isBold, boolean isBorder) {

        cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);        // 水平居中
        cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);  // 垂直居中
        cellStyleHeader.setFont(getFont(workbook, size, isBold));
        if (isBorder){
            cellStyleHeader.setBorderTop(BorderStyle.THIN);
            cellStyleHeader.setBorderRight(BorderStyle.THIN);
            cellStyleHeader.setBorderBottom(BorderStyle.THIN);
            cellStyleHeader.setBorderLeft(BorderStyle.THIN);
        }

        return cellStyleHeader;
    }

    // 副表头行单元格样式
    public static HSSFCellStyle getAlignRightHeader(HSSFWorkbook workbook, short size, boolean isBold) {

        cellStyleViceHeader.setAlignment(HorizontalAlignment.RIGHT);        // 水平靠右
        cellStyleViceHeader.setVerticalAlignment(VerticalAlignment.CENTER);  // 垂直居中
        cellStyleViceHeader.setFont(getFont(workbook, size, isBold));
        cellStyleViceHeader.setBorderTop(BorderStyle.THIN);
        cellStyleViceHeader.setBorderRight(BorderStyle.THIN);
        cellStyleViceHeader.setBorderBottom(BorderStyle.THIN);
        cellStyleViceHeader.setBorderLeft(BorderStyle.THIN);

        return cellStyleViceHeader;
    }

    // 标题行单元格样式
    public static HSSFCellStyle getAlignCenterTitle(HSSFWorkbook workbook, short size, boolean isBold) {

        cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
        cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleTitle.setFont(getFont(workbook, size, isBold));
        cellStyleTitle.setBorderTop(BorderStyle.THIN);
        cellStyleTitle.setBorderRight(BorderStyle.THIN);
        cellStyleTitle.setBorderBottom(BorderStyle.THIN);
        cellStyleTitle.setBorderLeft(BorderStyle.THIN);

        return cellStyleTitle;
    }

    // 设置数据行单元格样式
    public static HSSFCellStyle getAlignCenterData(HSSFWorkbook workbook, short size, boolean isBold) {

        cellStyleData.setAlignment(HorizontalAlignment.CENTER);
        cellStyleData.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleData.setFont(getFont(workbook, size, isBold));
        cellStyleData.setBorderTop(BorderStyle.THIN);
        cellStyleData.setBorderRight(BorderStyle.THIN);
        cellStyleData.setBorderBottom(BorderStyle.THIN);
        cellStyleData.setBorderLeft(BorderStyle.THIN);

        return cellStyleData;
    }

    // 设置字体
    public static HSSFFont getFont(HSSFWorkbook workbook, short size, boolean isBold) {

        font.setFontHeightInPoints(size);
        font.setBold(isBold);
        font.setFontName("宋体");

        return font;
    }

    // 导出
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
