package jp.co.worksap.stm2018.jobhere.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by bwju on 2016/12/06.
 */
public class ExcelUtils {
    private static HSSFCellStyle cellstyle = null;

    public static void exportRecruitRecord(HttpServletRequest request, HttpServletResponse response, List<Map<String, String>> mapList) throws IOException {
//        String psth = ResourceUtils.getFile("classpath:RecruitRecord.xls").getPath();
//        Workbook webBook = readExcel(psth);
//        System.out.println(psth);

        Workbook webBook = new HSSFWorkbook();
        createCellStyle(webBook);
//        Sheet sheet = webBook.createSheet("Data");

        Map<String, Integer> sheetMap = new HashMap<>();
        int cnt = 0;
        for (Map<String, String> map : mapList) {
            String position = map.get("Position");
            if (!sheetMap.containsKey(position)) {
                sheetMap.put(position, cnt++);
                sheetMap.put(position + "-Row", 1);
            }
        }

        Sheet[] sheets = new Sheet[cnt];
        sheetMap.forEach((k, v) -> {
            if (!k.contains("-Row"))
                sheets[v] = webBook.createSheet(k);
        });

        for (Map<String, String> map : mapList) {
            int col = 0;
            String position = map.get("Position");
            int rowIdx = sheetMap.get(position + "-Row");
            Row row = sheets[sheetMap.get(position)].createRow(rowIdx);
            sheetMap.put(position + "-Row", ++rowIdx);

            System.out.println(rowIdx);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!entry.getKey().equals("Position")) {
                    row.createCell(col++).setCellValue(entry.getValue());
                }
            }
        }

/*        String[] arr = {"Position", "Cooperator", "Candidate", "Department", "Step", "Comment", "Pass", "Email",
                "Phone", "Interview", "Assessment", "Results"};

        List<String> fields = new ArrayList<>();

        for (int i = 1, size = mapList.size(); i < size; i++) {
            Map<String, String> map = mapList.get(i - 1);
            Row row = sheet.createRow(i);
            for (int j = 0; j < fields.size(); j++) {
                row.createCell(j).setCellValue(map.get(fields.get(j)));
            }
        }*/

        //将生成excel文件保存到指定路径下
        try {
            FileOutputStream fout = new FileOutputStream("/home/chao/Desktop/RecruitRecord.xls");
            webBook.write(fout);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Excel文件生成成功...");

        writeExcel(response, webBook, "RecruitRecord");


    }


    private static Workbook readExcel(String filePath) {
        InputStream in = null;
        Workbook work = null;
        try {
            in = new FileInputStream(filePath);
            work = new HSSFWorkbook(in);
        } catch (FileNotFoundException e) {
            System.out.println("文件路径错误");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("文件输入流错误");
            e.printStackTrace();
        }
        return work;
    }

    private static void writeExcel(HttpServletResponse response, Workbook work, String fileName) throws IOException {
        OutputStream out = null;
        try {
            out = response.getOutputStream();

            response.setContentType("application/octet-stream");
//            response.setHeader("Content-Disposition", "attachment;filename=statics.xls");
//            response.setContentType("application/ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(fileName + ".xls", "UTF-8"))));
            work.write(out);
        } catch (IOException e) {
            System.out.println("输出流错误");
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    private static Cell setCellStyleWithStyleAndValue(CellStyle style, Cell cell, String value) {
        cell.setCellStyle(style);
        cell.setCellValue(value);
        return cell;
    }

    private static Cell setCellStyleWithValue(Cell cell, String value) {
        cell.setCellStyle(cellstyle);
        cell.setCellValue(value);
        return cell;
    }


    private static Cell setCellStyleWithStyleAndValue(CellStyle style, Cell cell, RichTextString value) {
        cell.setCellStyle(style);
        cell.setCellValue(value);
        return cell;
    }

    private static Cell setCellStyleWithValue(Cell cell, int value) {
        cell.setCellStyle(cellstyle);
        cell.setCellValue(value);
        return cell;
    }

    private static Cell setCellStyleWithValue(Cell cell, double value) {
        cell.setCellStyle(cellstyle);
        cell.setCellValue(value);
        return cell;
    }

    private static HSSFCellStyle createCellStyle(Workbook wb) {
        cellstyle = (HSSFCellStyle) wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellstyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellstyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellstyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return cellstyle;
    }
}
