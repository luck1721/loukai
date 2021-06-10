package com.example.demo.bll.domain;

import java.util.List;
import java.util.Map;

/**
 * 常用字面常量
 *
 * @date 2018年12月11日
 * @author huanglj
 */
public interface Literal {

	String NULL = "null";
	Object NULL_VALUE = null;

	String TRUE = "true";
	boolean TRUE_VALUE = true;

	String FALSE = "false";
	boolean FALSE_VALUE = false;

	String ZERO = "0";
	int ZERO_VALUE = 0;

	String ONE = "1";
	int ONE_VALUE = 1;

	String TWO = "2";
	int TWO_VALUE = 2;

	String THREE = "3";
	int THREE_VALUE = 3;

	String FOUR = "4";
	int FOUR_VALUE = 4;

	String FIVE = "5";
	int FIVE_VALUE = 5;

	String SIX = "6";
	int SIX_VALUE = 6;

	String SEVEN = "7";
	int SEVEN_VALUE = 7;

	String EIGHT = "8";
	int EIGHT_VALUE = 8;

	String NINE = "9";
	int NINE_VALUE = 9;

	String TEN = "10";
	int TEN_VALUE = 10;

	String HUNDRED = "100";
	int HUNDRED_VALUE = 100;

	String THOUSAND = "1000";
	int THOUSAND_VALUE = 1000;

	String MILLION = "1000000";
	int MILLION_VALUE = 1000000;

	String BILLION = "1000000000";
	int BILLION_VALUE = 1000000000;

	String EMPTY = "";
	char EMPTY_VALUE = '\0';

	String A = "a";
	char A_VALUE = 'a';

	String B = "b";
	char B_VALUE = 'b';

	String C = "c";
	char C_VALUE = 'c';

	String D = "d";
	char D_VALUE = 'd';

	String E = "e";
	char E_VALUE = 'e';

	String F = "f";
	char F_VALUE = 'f';

	String UNDERLINE = "_";
	char UNDERLINE_VALUE = '_';

	String $ = "$";
	char $_VALUE = '$';

	String VOID = "void";
	Class<?> VOID_VALUE = void.class;

	String BOOLEAN = "boolean";
	Class<?> BOOLEAN_VALUE = boolean.class;

	String BYTE = "byte";
	Class<?> BYTE_VALUE = byte.class;

	String SHORT = "short";
	Class<?> SHORT_VALUE = short.class;

	String INT = "int";
	Class<?> INT_VALUE = int.class;

	String LONG = "long";
	Class<?> LONG_VALUE = long.class;

	String FLOAT = "float";
	Class<?> FLOAT_VALUE = float.class;

	String DOUBLE = "double";
	Class<?> DOUBLE_VALUE = double.class;

	String CHAR = "char";
	Class<?> CHAR_VALUE = char.class;

	String STRING = "String";
	Class<?> STRING_VALUE = String.class;

	String LIST = "List";
	Class<?> LIST_VALUE = List.class;

	String MAP = "Map";
	Class<?> MAP_VALUE = Map.class;

	String OBJECT = "Object";
	Class<?> OBJECT_VALUE = Object.class;

	String CLASS = "Class";
	Class<?> CLASS_VALUE = Class.class;

}
