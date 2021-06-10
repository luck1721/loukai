package com.example.demo.bll.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lk
 * @date 2021/3/20
 */
public class Execl2WordUtils {

	public static void execl2Word () throws IOException, ParseException {
		File file = new File("d:/新桌面/13.xls");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		InputStream is = new FileInputStream(file);
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		HSSFSheet sheet = workbook.getSheetAt(0);
		OutputStream outputStream = new FileOutputStream("D:/a.docx");
		InputStream inputStream = new FileInputStream("D:/template.docx");
		//InputStream inputStream = Execl2WordUtils.class.getClassLoader().getResourceAsStream(tmpFile);
//        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(tmpFile);
		XWPFDocument document = null;
		try {
			ZipSecureFile.setMinInflateRatio(-1.0d);
			document = new XWPFDocument(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int n = 0;
		int j = 1;
		String num = "";
		for (int i = 1; i <= 942; i++) {
			HSSFRow row = sheet.getRow(i);
			//工程名称
			Object factoryName;
			System.out.println(i);
			if (row.getCell(4).getCellType() == (HSSFCell.CELL_TYPE_STRING)) {
				factoryName =row.getCell(4).getStringCellValue();
			}else {
				factoryName =(int) row.getCell(4).getNumericCellValue();
			}
			//生产号
			String prodNo;
			/*if (row.getCell(7).getCellType() == (HSSFCell.CELL_TYPE_STRING)) {
				prodNo =row.getCell(7).getStringCellValue();
			}else {
				prodNo =(int) row.getCell(7).getNumericCellValue();
			}*/
			//上报数
			Object reportNum;
			if (row.getCell(6).getCellType() == (HSSFCell.CELL_TYPE_STRING)) {
				reportNum =row.getCell(6).getStringCellValue();
			}else {
				reportNum =(int) row.getCell(6).getNumericCellValue();
			}
			//上报日期
			Date reportDate = row.getCell(5).getDateCellValue();
			String reportDateNum = reportDate.toInstant().toString();
			String year = reportDateNum.split("-")[0];
			String month = reportDateNum.split("-")[1];
			String yearMonth = year + month;
			if (yearMonth.equals(num)) {
				String d = Integer.toString(j).length() != 1 ? Integer.toString(j) : "0" + j;
				prodNo = yearMonth + d;
			}else {
				num = yearMonth;
				j = 1;
				prodNo = yearMonth + "0" + j;
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(reportDate);
			calendar.add(Calendar.DAY_OF_MONTH, -2);
			String date = sdf.format(calendar.getTime());
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(reportDate);
			if (Integer.parseInt(reportNum.toString()) < 10 ) {
				calendar2.add(Calendar.DAY_OF_MONTH, -5);
			} else if (Integer.parseInt(reportNum.toString()) > 10 ) {
				calendar2.add(Calendar.DAY_OF_MONTH, -5 - ((Integer.parseInt(reportNum.toString()) / 10) * 2));
			}
			String date2 = sdf.format(calendar2.getTime());
			//产品名称
			String name = row.getCell(2).getStringCellValue().split("->")[2];
			//型号
			String model = row.getCell(2).getStringCellValue().split("->")[3];
			Map map=new HashMap();
			map.put("prod_name",factoryName);
			map.put("prod_no",prodNo);
			map.put("date",date);
			map.put("date2",date2);
			map.put("name",name);
			map.put("model",model);
			map.put("report_num",reportNum);
			getBuild(document,map,outputStream,n);
			n++;
			j++;
		}
		//导出到文件
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			document.write(byteArrayOutputStream);
			outputStream.write(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
		Execl2WordUtils.execl2Word();
	}


}
