package jp.co.worksap.stm2018.jobhere.util;

import io.swagger.models.auth.In;
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

        Workbook webBook = new HSSFWorkbook();

        Map<Integer, Short> colorMap = new HashMap<>();
        colorMap.put(0, IndexedColors.LIME.getIndex());
        colorMap.put(1, IndexedColors.SKY_BLUE.getIndex());
        colorMap.put(2, IndexedColors.ROSE.getIndex());
        colorMap.put(3, IndexedColors.YELLOW.getIndex());

        Map<String, Integer> sheetMap = new HashMap<>();
        Map<String, Integer[]> interviewerMap = new HashMap<>();

        int cnt = 0;
        for (Map<String, String> map : mapList) {
            String position = map.get("Position");
            if (!sheetMap.containsKey(position)) {
                sheetMap.put(position, cnt++);
                sheetMap.put(position + "@Row", 1);
                sheetMap.put(position + "@Col", map.size());
            } else {
                if (sheetMap.get(position + "@Col") < map.size())
                    sheetMap.put(position + "@Col", map.size());
            }
        }

        Sheet[] sheets = new Sheet[cnt + 1];
        sheetMap.forEach((k, v) -> {
            if (!k.contains("@")) {
                sheets[v] = webBook.createSheet(k);
                sheets[v].autoSizeColumn(1, true);
            }
        });

        sheets[cnt] = webBook.createSheet("Statistics");

        for (Map<String, String> map : mapList) {

            String position = map.get("Position");
            int rowIdx = sheetMap.get(position + "@Row");
            Row row = sheets[sheetMap.get(position)].createRow(rowIdx);
            sheetMap.put(position + "@Row", ++rowIdx);

            if (map.size() == sheetMap.get(position + "@Col")) {
                Row head = sheets[sheetMap.get(position)].createRow(0);

                int headCol = 0;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (!entry.getKey().equals("Position")) {
                        Cell cell = head.createCell(headCol++);
                        String title = entry.getKey();
                        if (title.contains(".")) {
                            int step = (int) Double.valueOf(title.substring(title.lastIndexOf(" "))).doubleValue();
                            CellStyle style = webBook.createCellStyle();
                            style.setFillForegroundColor(colorMap.get(step % 4));
                            style.setFillPattern(CellStyle.SOLID_FOREGROUND);

                            cell.setCellValue(title);
                            cell.setCellStyle(style);
                        } else
                            cell.setCellValue(title);
                    }
                }
                sheetMap.put(position + "@Col", Integer.MAX_VALUE);
            }

            int col = 0;
            String interviewer = null;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!entry.getKey().equals("Position")) {
                    Cell cell = row.createCell(col++);
                    cell.setCellValue(entry.getValue());


                    if (entry.getKey().contains("Cooperator")) {
                        interviewer = entry.getValue();
                        if (!interviewerMap.containsKey(interviewer)) {
                            Integer[] stat = {0, 0};
                            interviewerMap.put(interviewer, stat);
                        }
                    }

                    String mark = entry.getValue();
                    if (mark != null && mark.length() > 0 && mark.length() <= 3 && '0' <= mark.charAt(0) && mark.charAt(0) <= '9') {
                        Integer[] stat = interviewerMap.get(interviewer);
                        stat[0] += Integer.valueOf(mark);
                        stat[1]++;
                        interviewerMap.put(interviewer, stat);
                    }
                }
            }
        }

        //Write date to the sheet of statistics

        Row head = sheets[cnt].createRow(0);
        head.createCell(0).setCellValue("Name");
        head.createCell(1).setCellValue("Average Score");

        int interviewerRow = 1;
        for (Map.Entry<String, Integer[]> entry : interviewerMap.entrySet()) {
            Integer[] stat = entry.getValue();
            if (stat[1] > 0) {
                Row rows = sheets[cnt].createRow(interviewerRow++);
                rows.createCell(0).setCellValue(entry.getKey());
                rows.createCell(1).setCellValue(Double.valueOf(stat[0].toString()) / stat[1]);
            }
        }


        writeExcel(response, webBook, "RecruitRecord");


        //将生成excel文件保存到指定路径下
//        try {
//            FileOutputStream fout = new FileOutputStream("/home/chao/Desktop/RecruitRecord.xls");
//            webBook.write(fout);
//            fout.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Excel文件生成成功...");

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
