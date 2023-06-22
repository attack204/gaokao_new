package com.gaokao.common.service.admin;

import jxl.read.biff.BiffException;

import java.io.IOException;
import java.util.List;

/**
 * @author MaeYon-Z
 * date 2021-08-23
 */
public interface ResolveExcelService {

    List<int[]> resolveExcel(String filePath, String sheetName) throws IOException, BiffException;
    List<String> importExcel(List<int[]> list);
}
