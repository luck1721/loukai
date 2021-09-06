package com.example.demo.bll.utils;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.math.BigInteger;

/**
 * @author lk
 * @date 2021/2/1
 */
public class WordUtils {
	private static final String HEAD = "标题 2";
	private static final int LEVEL = 2;
	/**
	 * 设置段落
	 * @param doc
	 * @param text
	 * @param paragraphAlignment
	 * @param position
	 * @param color
	 * @param fontSize
	 * @param head
	 */
	public static void createParagraph(XWPFDocument doc, String text, ParagraphAlignment paragraphAlignment, int position, String color, int fontSize, Integer indentationFirstLine, Boolean head) {
		XWPFParagraph p = doc.createParagraph();// 新建段落
		p.setAlignment(paragraphAlignment);// 设置段落的对齐方式
		if (head) {
			addCustomHeadingStyle(doc, HEAD, LEVEL);
			p.setStyle(HEAD);
		}
		if (indentationFirstLine != null) {
			p.setIndentationFirstLine(indentationFirstLine);
		}
		XWPFRun r = p.createRun();
		r.setTextPosition(position);//设置行间距
		r.setText(text);
		r.setBold(head);//设置为粗体
		r.setColor(color);//设置颜色
		r.setFontSize(fontSize);

	}

	/**
	 * 设置标题样式
	 *
	 * @param doc
	 * @param strStyleId
	 * @param headingLevel
	 */
	public static void addCustomHeadingStyle(XWPFDocument doc, String strStyleId, int headingLevel) {
		CTStyle ctStyle = CTStyle.Factory.newInstance();
		ctStyle.setStyleId(strStyleId);
		CTString styleName = CTString.Factory.newInstance();
		styleName.setVal(strStyleId);
		ctStyle.setName(styleName);
		CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
		indentNumber.setVal(BigInteger.valueOf(headingLevel));
		ctStyle.setUiPriority(indentNumber);
		CTOnOff onOffNull = CTOnOff.Factory.newInstance();
		ctStyle.setUnhideWhenUsed(onOffNull);
		ctStyle.setQFormat(onOffNull);
		CTPPr ppr = CTPPr.Factory.newInstance();
		ppr.setOutlineLvl(indentNumber);
		ctStyle.setPPr(ppr);
		XWPFStyle style = new XWPFStyle(ctStyle);
		XWPFStyles styles = doc.createStyles();
		style.setType(STStyleType.PARAGRAPH);
		styles.addStyle(style);
	}

	/**
	 * 创建默认的页脚(该页脚主要只居中显示页码)
	 *
	 * @param doc XWPFDocument文档对象
	 */
	public static void createDefaultFooter(final XWPFDocument doc) {
		CTP pageNo = CTP.Factory.newInstance();
		XWPFParagraph footer = new XWPFParagraph(pageNo, doc);
		CTPPr begin = pageNo.addNewPPr();
		begin.addNewJc().setVal(STJc.CENTER);
		pageNo.addNewR().addNewFldChar().setFldCharType(STFldCharType.BEGIN);
		pageNo.addNewR().addNewInstrText().setStringValue("PAGE   \\* MERGEFORMAT");
		pageNo.addNewR().addNewFldChar().setFldCharType(STFldCharType.SEPARATE);
		CTR end = pageNo.addNewR();
		CTRPr endRPr = end.addNewRPr();
		endRPr.addNewNoProof();
		end.addNewFldChar().setFldCharType(STFldCharType.END);
		CTSectPr sectPr = doc.getDocument().getBody().isSetSectPr() ? doc.getDocument().getBody().getSectPr() : doc.getDocument().getBody().addNewSectPr();
		XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(doc, sectPr);
		policy.createFooter(STHdrFtr.DEFAULT, new XWPFParagraph[]{footer});
	}
}
