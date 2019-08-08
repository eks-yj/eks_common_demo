package com.eks.other;

import com.eks.enumeration.DataTypeEnum;
import com.eks.enumeration.DatabaseDriverEnum;
import com.eks.utils.FileUtils;
import com.eks.utils.JdbcUtils;
import com.eks.utils.Pinyin4jUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class PoiTest {
    private static String getCellValue(DataTypeEnum dataTypeEnum, Cell cell){
        String cellValueString = cell.toString();
        if (cellValueString == null || "".equals(cellValueString)){
            return "'null'";
        }
        if (dataTypeEnum == DataTypeEnum.NUMBER){
            return cellValueString;
        }
        if(dataTypeEnum == DataTypeEnum.STRING){
            return "'" + cellValueString + "'";
        }
        throw new RuntimeException("Date type error.");
    }
    @Test
    public void test1() throws Exception {
        String filePathString = FileUtils.generatePathBaseProjectPath("extra/xlsx/eks_dictum_note.xls");
        //创建输入流，接受目标excel文件
        FileInputStream fileInputStream = new FileInputStream(filePathString);
        //得到POI文件系统对象
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fileInputStream);
        //得到Excel工作簿对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
        //得到Excel工作簿的第一页，即excel工作表对象
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        String baseInsertSqlString = "INSERT INTO 'main'.'tbl_dictum'('id', 'author', 'dictum') VALUES (";
        List<String> sqlStringList = new ArrayList<>();
        for(Row row : hssfSheet){
            Cell secondCell = row.getCell(1);
            if (secondCell == null){
                continue;
            }
            StringBuilder sqlStringBuilder = new StringBuilder(baseInsertSqlString);
            int rowNumInt = row.getRowNum();
            sqlStringBuilder.append(rowNumInt + 1);
            sqlStringBuilder.append(",");
            sqlStringBuilder.append(getCellValue(DataTypeEnum.STRING,row.getCell(0)));
            sqlStringBuilder.append(",");
            sqlStringBuilder.append(getCellValue(DataTypeEnum.STRING,secondCell));
            sqlStringBuilder.append(")");
            sqlStringList.add(sqlStringBuilder.toString());
        }
        for(String sqlString : sqlStringList){
            System.out.println(sqlString);
        }
        String dbFilePathString = FileUtils.generatePathBaseProjectPath("extra/db/sqlite.db");
        String urlString = "jdbc:sqlite:" + dbFilePathString;
        int[] updateCountIntArray = JdbcUtils.updateByJdbc(DatabaseDriverEnum.SQLITE, urlString, null, null, sqlStringList);
        if (updateCountIntArray != null){
            System.out.println("Success");
        }
    }
    @Test
    public void test2() throws Exception {
        //创建输入流，接受目标excel文件
        FileInputStream fileInputStream = new FileInputStream("D:\\Study_Classification\\Generalize\\test.xls");
        //得到POI文件系统对象
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fileInputStream);
        //得到Excel工作簿对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
        //得到Excel工作簿的第一页，即excel工作表对象
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        HSSFRow row = hssfSheet.getRow(0);
        for(Cell cell : row){
            System.out.println("//" + cell.toString());
            String fieldNameString = Pinyin4jUtils.getFieldNameFromHanzi(cell.toString(), true, "_");
            System.out.println("private String " + fieldNameString + ";");
        }
    }
}
