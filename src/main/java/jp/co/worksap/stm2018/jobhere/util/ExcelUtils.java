package jp.co.worksap.stm2018.jobhere.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by bwju on 2016/12/06.
 */
public class ExcelUtils {
    private static final String INSPECTIONRECORD_SURFACE_TEMPLET_PATH = "/asserts/templete/InspectionRecordSurface.xls";
    private static HSSFCellStyle cellstyle = null;

    public static void exportInspectionRecordSurface(HttpServletRequest request, HttpServletResponse response, Map map) throws IOException {
        //实现上文里有，改个函数名，写你的操作模板函数吧！
    }

    public static void exportRecruitRecord(HttpServletRequest request, HttpServletResponse response, Map map) throws IOException {
        //模板的路径，这个在自己的项目中很容易弄错，相对位置一定要写对啊
        String psth = request.getRealPath("/") + INSPECTIONRECORD_SURFACE_TEMPLET_PATH;
        Workbook webBook = readExcel(psth);
        createCellStyle(webBook);
        Sheet sheet = webBook.getSheetAt(0);
        //开始操作模板，找到某行某列（某个cell），需要注意的是这里有个坑，行和列的计数都是从0开始的
        //一次数据插入的位置不对，别灰心，多试几次就好啦，你要是能看懂我下面的代码，数据插在了什么位置，你就明白了
        int rows = 1;
        Row row = sheet.getRow(rows);
        row.createCell(1).setCellValue((String) map.get("sequence"));
        row.createCell(3).setCellValue((String) map.get("date"));
        row.createCell(9).setCellValue((String) map.get("chetaihao"));
        rows = 2;
        row = sheet.getRow(rows);
        row.createCell(1).setCellValue((String) map.get("productName"));
        row.createCell(3).setCellValue((String) map.get("specification"));
        row.createCell(9).setCellValue((String) map.get("memo"));
        //检验记录的插入业务相关，不展示，其实就是for循环在合适的行合适的列插入一个个对象的属性即可，你这么聪明，没问题的
        writeExcel(response, webBook, "表面质量检验记录");
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
            response.setContentType("application/ms-excel;charset=UTF-8");
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
