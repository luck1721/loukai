package com.example.demo.bll.utils;

import com.example.demo.bll.entity.Department;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lk
 * @date 2021/3/20
 */
public class Execl2WordUtils2 {

	public static void execl2Word () throws IOException, ParseException {
		File file = new File("d:/新桌面/test.xlsx");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		InputStream is = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		XSSFSheet sheet = workbook.getSheetAt(0);
		OutputStream outputStream = new FileOutputStream("D:/b.xlsx");
		InputStream inputStream = new FileInputStream("D:/template.docx");
		XWPFDocument document = null;
		try {
			ZipSecureFile.setMinInflateRatio(-1.0d);
			document = new XWPFDocument(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Department> list = new ArrayList<>();
		List<Department> newList = new ArrayList<>();
		for (int i = 1; i <= 1591; i++) {
			Row row = sheet.getRow(i);
			Object id;
			if (row.getCell(0).getCellType() == (HSSFCell.CELL_TYPE_STRING)) {
				id =row.getCell(0).getStringCellValue();
			}else {
				id =(int) row.getCell(0).getNumericCellValue();
			}
			//工程名称
			Object depId;
			if (row.getCell(1).getCellType() == (HSSFCell.CELL_TYPE_STRING)) {
				depId =row.getCell(1).getStringCellValue();
			}else {
				depId =(int) row.getCell(1).getNumericCellValue();
			}
			//生产号
			Object depName;
			if (row.getCell(2).getCellType() == (HSSFCell.CELL_TYPE_STRING)) {
				depName =row.getCell(2).getStringCellValue();
			}else {
				depName =(int) row.getCell(2).getNumericCellValue();
			}
			//上报数
			Object pid;
			if (row.getCell(3).getCellType() == (HSSFCell.CELL_TYPE_STRING)) {
				pid =row.getCell(3).getStringCellValue();
			}else {
				pid =(int) row.getCell(3).getNumericCellValue();
			}
			//上报日期
			String code = row.getCell(4).getStringCellValue();
			Department department = new Department();
			department.setId(id.toString());
			department.setDepId(depId.toString());
			department.setDepName(depName.toString());
			department.setPid(pid.toString());
			department.setCode(code);
			list.add(department);
		}
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			Department department = list.get(i);
			w :for (int j = 0; j < list.size(); j++) {
				if (!department.getPid().equals("0") && department.getPid().equals(list.get(j).getDepId())) {
					department.setId(Integer.toString(i+2));
					department.setPid(list.get(j).getId());
					break w;
				}
			}
		}
		String s[] = {"1","2","3","4","5"};
		ExportExcelUtil excelUtil = new ExportExcelUtil();
		excelUtil.exportExcel("123",s,list,outputStream,"2007");
		//导出到文件
		/*try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			document.write(byteArrayOutputStream);
			outputStream.write(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	/*void getFullPathId(List<String> fullPath, List<Department> list, String pid) {
		if (StringUtils.isBlank(pid)) {
			String pidId = "";
			for (Department department : list) {
				if (department.getId().equals(pid)) {
					fullPath.add(department.getDepName());
					pidId = department.getPid();
					break;
				}
			}
			getFullPathId(fullPath,list,pidId);
		}
	}*/

	public static void getBuild(XWPFDocument document, Map<String, String> contentMap, OutputStream outputStream,int n) throws IOException {

		List<XWPFTable> iterator = document.getTables();
		if (iterator.size() >= n) {
			// 读取文本内容
			replaceInPara(iterator.get(n), contentMap);
			// 替换内容
		/*for (Map.Entry<String, String> entry : contentMap.entrySet()) {
			bodyRange.replaceText("${" + entry.getKey() + "}", entry.getValue());
		}*/
		}
	}

	/**
	 * 替换段落里面的变量
	 *
	 * @param table
	 *            要替换的段落
	 * @param params
	 *            参数
	 */
	private static void replaceInPara(XWPFTable table, Map<String, String> params) {
		List<XWPFRun> runs;
		Matcher matcher;
		String runText = "";
		List<XWPFTableRow> rows = table.getRows();
		//读取每一行数据
		for (int i = 0; i < rows.size(); i++) {
			XWPFTableRow  row = rows.get(i);
			//读取每一列数据
			List<XWPFTableCell> cells = row.getTableCells();
			for (int j = 0; j < cells.size(); j++) {
				XWPFTableCell cell = cells.get(j);
				//输出当前的单元格的数据
				System.out.print(cell.getText() + "\t");
				if (matcher(cell.getText()).find()) {
					matcher = matcher(cell.getText());
					if (matcher.find()) {
						runText = matcher.replaceFirst(String.valueOf(params.get(matcher.group(1))));
						XWPFParagraph newPara = new XWPFParagraph(cell.getCTTc().addNewP(), cell);
						XWPFRun r1 = newPara.createRun();
						r1.setText(runText);
						cell.removeParagraph(0);
						cell.setParagraph(newPara);
					}
				}
			}
		}


	}

	private static Matcher matcher(String str) {
		Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		return matcher;
	}

	public static void main(String[] args) throws IOException, ParseException {
		Execl2WordUtils2.execl2Word();
	}


}
