package com.delicate.file;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class ExcelUtil {

    public static void main(String[] args) throws Exception {

        // 参数校验
        if (args.length == 0) {
            throw new RuntimeException("请输入参数,文件名");
        }

        // 读取配置文件
        Properties properties = new Properties();
        String path = System.getProperty("user.dir");
        System.out.println("当前路径:" + path);
        BufferedReader bufferedReader0 = new BufferedReader(new FileReader(path + File.separator + "file_format.properties"));
        properties.load(bufferedReader0);

        String sourceName = args[0];
        System.out.println("源文件名:" + sourceName);

        String rule = properties.getProperty(sourceName);
        if (rule == null || rule.length() == 0) {
            throw new RuntimeException("文件" + sourceName + "规则不存在");
        }

        // 创建excel文件
        File excelFile = new File(sourceName + ".xlsx");
        if (excelFile.exists()) {
            System.out.println("文件已存在，删除");
            excelFile.delete();
        } else {
            System.out.println("文件不存在，创建");
            excelFile.createNewFile();
        }

        // 创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        FileOutputStream outputStream = new FileOutputStream(excelFile);

        // 创建第一行标题
        String[] colInfos = rule.split(";");
        Map<String, String> map = new LinkedHashMap<>();
        for (String colInfo : colInfos) {
            String[] s = colInfo.split(":");
            map.put(s[0], s[1]);
        }
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow row = sheet.createRow(0);
        int colSize = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            XSSFCell cell = row.createCell(colSize++, CellType.STRING);
            cell.setCellValue(entry.getValue());
        }

        // 写文件体到excel
        File file = new File(sourceName);
        if (!file.exists()) {
            throw new RuntimeException("源文件不存在:" + sourceName);
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            int rowNumber = 1;
            while ((line = bufferedReader.readLine()) != null) {
                XSSFRow row2 = sheet.createRow(rowNumber++);
                int colNumber = 0;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String k = entry.getKey();
                    XSSFCell cell = row2.createCell(colNumber++, CellType.STRING);
                    String[] range = k.split("-");
                    int start = Integer.parseInt(range[0]);
                    int end = Integer.parseInt(range[1]);
                    byte[] bytes = line.getBytes("GBK");
                    byte[] newBytes = new byte[end - start + 1];
                    System.arraycopy(bytes, start - 1, newBytes, 0, newBytes.length);
                    cell.setCellValue(new String(newBytes, "GBK"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            workbook.write(outputStream);
        }
        //关闭工作簿
        workbook.close();
    }
}
