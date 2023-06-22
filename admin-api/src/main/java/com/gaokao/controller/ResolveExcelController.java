package com.gaokao.controller;

import com.gaokao.common.meta.vo.admin.ExcelFileAndSheetParams;
import com.gaokao.common.service.admin.ResolveExcelService;
import jxl.read.biff.BiffException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.List;

/**
 * @author MaeYon-Z
 * date 2021-08-23
 */
@Slf4j
@RestController
@RequestMapping("/xhr/v1")
public class ResolveExcelController {
    @Autowired
    ResolveExcelService resolveExcelService;

    @RequestMapping("/resolve")
    public List<String> resolveExcel(@RequestBody ExcelFileAndSheetParams excelFileAndSheetParams) throws IOException, BiffException {
        String filePath = excelFileAndSheetParams.getFilePath();
        String sheetName = excelFileAndSheetParams.getSheetName();
        List<int[]> records = resolveExcelService.resolveExcel(filePath, sheetName);
        List<String> insertOrders = resolveExcelService.importExcel(records);

/*
        //下面是将所有插入指令输出到文件中
        File f=new File("D:\\a.txt");//指定文件
        FileOutputStream fos=new FileOutputStream(f);//创建输出流fos并以f为参数
        OutputStreamWriter osw=new OutputStreamWriter(fos);//创建字符输出流对象osw并以fos为参数
        BufferedWriter bw=new BufferedWriter(osw);//创建一个带缓冲的输出流对象bw，并以osw为参数
        insertOrders.forEach(insertOrder -> {
            try {
                bw.write(insertOrder);//使用bw写入一行文字，为字符串形式String
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bw.newLine();//换行
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bw.close();//关闭并保存
*/

        return insertOrders;
    }
}
