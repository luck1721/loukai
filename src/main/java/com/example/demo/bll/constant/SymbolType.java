package com.example.demo.bll.constant;

/**
 * @author lk
 * @date 2021/1/22
 */
public final class SymbolType {

	/**
	 * 分号
	 */
	public static final String SEMICOLON = ";";
	/**
	 * 分号 - 中文
	 */
	public static final String SEMICOLON_CHINESE = "；";

	/**
	 * 空字符串
	 */
	public static final String EMPTY = "";

	/**
	 * 逗号
	 */
	public static final String COMMA = ",";
	/**
	 * 逗号 - 中文
	 */
	public static final String COMMA_CHINESE = ",";

	/**
	 * 点
	 */
	public static final String DOT = ".";
	/**
	 * 下划线
	 */
	public static final String UNDERSCORE = "_";
	/**
	 * 连接符 -
	 */
	public static final String HYPHEN = "-";
	/**
	 * 冒号
	 */
	public static final String COLON = ":";
	/**
	 * 冒号 - 中文
	 */
	public static final String COLON_CHINESE = "：";
	/**
	 * @ 符号
	 */
	public static final String AT = "@";
	/**
	 * 斜线
	 */
	public static final String SLASH = "/";

	/**
	 * 反斜杠
	 */
	public static final String BACKSLASH = "\\";

	/**
	 * 美元符号
	 */
	public static final String DOLLAR = "$";

	/**
	 * 空格符
	 */
	public static final String SPACE = " ";

	/**
	 * 制表符，为了在不使用表格的情况下，上下对齐，table的意思。
	 */
	public static final String TAB = "\t";

	/**
	 * 回车符，返回到这一行的开头，return的意思。
	 */
	public static final String CR = "\r";

	/**
	 * 换行符，到下一行的同一位置，纵坐标相同，new line的意思。
	 */
	public static final String LF = "\n";

	/**
	 * Enter 相当于\n\r，所以Enter的标识为 往下，再往前。当然，\n\r等价于\r\n。
	 */
	public static final String CRLF = "\r\n";

	/**
	 * 引号
	 */
	public static final String QUOTES = "\"";

	/**
	 * 引号 - 中文 - 左
	 */
	public static final String QUOTES_CHINESE_LEFT = "“";

	/**
	 * 引号 - 中文 - 右
	 */
	public static final String QUOTES_CHINESE_RIGHT = "”";

	/**
	 * 问号
	 */
	public static final String QUESTION_MARK = "?";

	/**
	 * 问号 - 中文
	 */
	public static final String QUESTION_MARK_CHINESE = "？";

	/**
	 * 尖括号 - 左
	 */
	public static final String ANGLE_BRACKETS_LEFT = "<";

	/**
	 * 尖括号 - 中文 - 左
	 */
	public static final String ANGLE_BRACKETS_CHINESE_LEFT = "《";

	/**
	 * 尖括号 - 右
	 */
	public static final String ANGLE_BRACKETS_RIGHT = ">";

	/**
	 * 尖括号 - 中文 - 右
	 */
	public static final String ANGLE_BRACKETS_CHINESE_RIGHT = "》";

	/**
	 * 小括号 - 左
	 */
	public static final String PARENTHESES_LEFT = "(";

	/**
	 * 小括号 - 中文 - 左
	 */
	public static final String PARENTHESES_CHINESE_LEFT = "（";

	/**
	 * 小括号 - 右
	 */
	public static final String PARENTHESES_RIGHT = ")";

	/**
	 * 小括号 - 中文 - 右
	 */
	public static final String PARENTHESES_CHINESE_RIGHT = "）";

	/**
	 * 大括号 - 左
	 */
	public static final String CURLY_BRACES_LEFT = "{";

	/**
	 * 大括号 - 右
	 */
	public static final String CURLY_BRACES_RIGHT = "}";

	/**
	 * 竖线
	 */
	public static final String VERTICAL_VIRGULE = "|";

	/**
	 * 数组数据类型的后缀
	 */
	public static final String ARRAY_SUFFIX = "[]";

	/**
	 * Java基本数据类型的前缀
	 * byte[].class, char[].class, short[].class, int[].class, long[].class, boolean[].class, float[].class, double[].class
	 * class [B	class [C      class [S       class [I     class [J      class [Z         class [F       class [D
	 * <p>
	 * System.out.println(byte[].class);					-- 结果：class [B
	 * System.out.println(byte[].class.getName());			-- 结果：[B
	 * System.out.println(byte[].class.getTypeName());		-- 结果：byte[]
	 * System.out.println(byte[].class.getComponentType());-- 结果：byte
	 */
	public static final String PRIMITIVE_ARRAY_PREFIX = "[";

	/**
	 * 非Java基本数据类型的数组的数组前缀
	 * String[].class
	 * class [Ljava.lang.String;
	 * <p>
	 * System.out.println(String[].class);						-- 结果：class [Ljava.lang.String;
	 * System.out.println(String[].class.getName());			-- 结果：[Ljava.lang.String;
	 * System.out.println(String[].class.getTypeName());		-- 结果：java.lang.String[]
	 * System.out.println(String[].class.getComponentType());	-- 结果：class java.lang.String
	 */
	public static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";

	/**
	 * Cglib生成的代理类包含 $$ 符号
	 */
	public static final String CGLIB_CLASS_SEPARATOR = "$$";

	/**
	 * Jar路径以及内部文件路径的分界符: "!/"
	 */
	public static final String JAR_URL_SEPARATOR = "!/";

	/**
	 * WAR路径及内部文件路径分界符
	 */
	public static final String WAR_URL_SEPARATOR = "*/";


	public static void main(String[] args) {
		System.out.println(String[].class);
		System.out.println(String[].class.getName());
		System.out.println(String[].class.getTypeName());
		System.out.println(String[].class.getComponentType());
		System.out.println(byte[].class.getComponentType());
	}
}
