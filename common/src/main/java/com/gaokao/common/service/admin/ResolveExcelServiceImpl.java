package com.gaokao.common.service.admin;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MaeYon-Z
 * date 2021-08-23
 */
@Slf4j
@Service
public class ResolveExcelServiceImpl implements ResolveExcelService{

    @Override
    public List<int[]> resolveExcel(String fileName, String sheetName){
        try {
            List<int[]> list = new ArrayList<>();
            InputStream inputStream = new FileInputStream(fileName);
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            int rows = sheet.getPhysicalNumberOfRows();
            for (int i = 3; i < rows; i++) {
                Row row = sheet.getRow(i);
                int[] cells = new int[row.getPhysicalNumberOfCells()];
                int index = 0;
                for (Cell cell : row) {
                    cells[index] = (int)cell.getNumericCellValue();
                    index++;
                }
                list.add(cells);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> importExcel(List<int[]> recordList){
        List<String> orders = new ArrayList<>();
        recordList.forEach(record ->{
/*
            String insert = "INSERT INTO tb_score_rank(`score`, `nums`, `total_nums`, `has_physics_nums`, `has_physics_total`, " +
                    "`has_chemistry_nums`, `has_chemistry_total`, `has_biology_nums`, `has_biology_total`, `has_politics_nums`, " +
                    "`has_politics_total`, `has_history_nums`, `has_history_total`, `has_geography_nums`, `has_geography_total`) VALUES(";
*/
            String insert = "INSERT INTO tb_score_rank VALUES(0,";
            int l = record.length;
            for(int i = 0; i < l; i++){
                if(i == l-1){
                    if(record[i] != 0)
                        insert = insert + record[i] + ");";
                    else
                        insert = insert + "null" + ");";
                }
                else{
                    if(record[i] != 0)
                        insert = insert + record[i] + ",";
                    else
                        insert = insert + "null" + ",";
                }
            }
            orders.add(insert);
        });
        return orders;
    }
}

