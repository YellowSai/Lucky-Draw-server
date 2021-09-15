package cn.jzcscw.commons.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellBase;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * @author lixue
 */
@Slf4j
public class ExcelUtil {

    public static String getStringValue(CellBase cell) {
        if (cell == null) {
            return null;
        }
        String value = null;
        if (CellType.NUMERIC.equals(cell.getCellType())) {
            double num = cell.getNumericCellValue();
            NumberFormat format = NumberFormat.getInstance();
            format.setGroupingUsed(false);
            value = format.format(num);
        } else {
            value = cell.getStringCellValue();
        }
        return value;
    }

    public static double getDoubleValue(CellBase cell) {
        if (cell == null) {
            return 0;
        }

        if (CellType.NUMERIC.equals(cell.getCellType())) {
            return cell.getNumericCellValue();
        } else if (CellType.STRING.equals(cell.getCellType())) {
            String num = ExcelUtil.getStringValue(cell);
            if (NumberUtil.isNumber(num)) {
                return Double.parseDouble(num);
            }
        }
        return 0;
    }

    public static long getLongValue(CellBase cell) {
        if (cell == null) {
            return 0;
        }
        if (CellType.NUMERIC.equals(cell.getCellType())) {
            return (long) cell.getNumericCellValue();
        } else if (CellType.STRING.equals(cell.getCellType())) {
            String num = ExcelUtil.getStringValue(cell);
            if (NumberUtil.isNumber(num)) {
                return Long.parseLong(num);
            }
        }
        return 0;
    }

    public static int getIntValue(CellBase cell) {
        if (cell == null) {
            return 0;
        }
        if (CellType.NUMERIC.equals(cell.getCellType())) {
            return (int) cell.getNumericCellValue();
        } else if (CellType.STRING.equals(cell.getCellType())) {
            String num = ExcelUtil.getStringValue(cell);
            if (NumberUtil.isNumber(num)) {
                return Integer.parseInt(num);
            }
        }
        return 0;
    }

    public static Date getDateValue(CellBase cell) {
        return ExcelUtil.getDateValue(cell, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date getDateValue(CellBase cell, String format) {
        if (cell == null) {
            return null;
        }
        if (CellType.NUMERIC.equals(cell.getCellType())) {
            return cell.getDateCellValue();
        } else if (CellType.STRING.equals(cell.getCellType())) {
            String str = ExcelUtil.getStringValue(cell);
            if (StringUtil.isEmpty(format)) {
                format = "yyyy-MM-dd HH:mm:ss";
            }
            return DateUtil.parse(str, format);
        }
        return null;
    }

    public static byte[] generateExcel(String[] columnList, List<List<Object>> dataList) {
        SXSSFWorkbook wb = null;
        try {
            wb = new SXSSFWorkbook();
            Sheet sheet = wb.createSheet("Sheet1");
            sheet.setDefaultColumnWidth(20);

            //标题栏
            Row row0 = sheet.createRow(0);

            // 样式对象
            CellStyle titleCellStyle = wb.createCellStyle();
            titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
            titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleCellStyle.setBorderBottom(BorderStyle.NONE);
            titleCellStyle.setBorderLeft(BorderStyle.NONE);//
            titleCellStyle.setBorderRight(BorderStyle.NONE);//
            titleCellStyle.setBorderTop(BorderStyle.NONE);//
            titleCellStyle.setWrapText(true);
            // 创建字体
            Font ff = wb.createFont();
            ff.setFontHeightInPoints((short) 12);// 字体大小
            ff.setBold(true);
            titleCellStyle.setFont(ff);

            for (int i = 0; i < columnList.length; i++) {
                String title = columnList[i];
                Cell cell = row0.createCell(i);
                cell.setCellValue(title);
                cell.setCellStyle(titleCellStyle);
            }
            CellStyle contentCellStyle = wb.createCellStyle();
            contentCellStyle.setAlignment(HorizontalAlignment.CENTER);
            contentCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentCellStyle.setBorderBottom(BorderStyle.NONE);
            contentCellStyle.setBorderLeft(BorderStyle.NONE);//
            contentCellStyle.setBorderRight(BorderStyle.NONE);//
            contentCellStyle.setBorderTop(BorderStyle.NONE);//
            contentCellStyle.setWrapText(true);

            CellStyle dateCellStyle = wb.createCellStyle();
            dateCellStyle.setAlignment(HorizontalAlignment.CENTER);
            dateCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dateCellStyle.setBorderBottom(BorderStyle.NONE);
            dateCellStyle.setBorderLeft(BorderStyle.NONE);//
            dateCellStyle.setBorderRight(BorderStyle.NONE);//
            dateCellStyle.setBorderTop(BorderStyle.NONE);//
            dateCellStyle.setWrapText(true);

            //内容行
            // 创建字体
            Font ff2 = wb.createFont();
            ff2.setFontHeightInPoints((short) 12);// 字体大小
            contentCellStyle.setFont(ff2);// 放入样式中
            dateCellStyle.setFont(ff2);// 放入样式中

            CreationHelper createHelper = wb.getCreationHelper();
            short dateFormat = createHelper.createDataFormat().getFormat("yyy-mm-dd hh:mm:ss");
            dateCellStyle.setDataFormat(dateFormat);

            int i = 1;
            for (List<Object> subList : dataList) {
                Row row = sheet.createRow(i++);
                for (int j = 0; j < subList.size(); j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellStyle(contentCellStyle);
                    Object data = subList.get(j);
                    if (data == null) {
                        cell.setCellValue("");
                    } else if (data instanceof Date) {
                        cell.setCellValue((Date) data);
                        cell.setCellStyle(dateCellStyle);
                    } else {
                        cell.setCellValue(String.valueOf(data));
                    }
                }
            }
            // 先写到字节数组，在从字节数组读出
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                wb.write(os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] content = os.toByteArray();
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (wb != null) {
                wb.dispose();
            }
        }
        return null;
    }
}
