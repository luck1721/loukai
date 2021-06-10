package com.example.demo.bll.util;


import com.example.demo.bll.domain.BaseBean;
import com.example.demo.bll.domain.Literal;
import javassist.Modifier;
import javassist.*;
import javassist.bytecode.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射类型工具类
 * 
 * @created 2016-7-19
 * @author  huanglj
 */
public class ClassUtils {
	/** 日志对象 */
	private static final Logger log = LoggerFactory.getLogger(ClassUtils.class);
	/** 类型和类型信息映射 */
	private static final Map<Class<?>, ClassInfo> classInfoMapping = new ConcurrentHashMap<>();
	/** 继承类型实际类型参数缓存，key: 原始类型，value.key: 继承类型 */
	private static final Map<Class<?>, Map<Type, Class<?>[]>> extendActualTypeArgumentsCache = new ConcurrentHashMap<>();
	/** 类的属性和读方法的映射关系缓存 */
	private static final Map<Class<?>, Map<String, Method>> readMethodMappingCache = new ConcurrentHashMap<>();
	/** 类的属性和写方法的映射关系缓存 */
	private static final Map<Class<?>, Map<String, Method>> writeMethodMappingCache = new ConcurrentHashMap<>();
	/** 类的实例属性缓存 */
	private static final Map<Class<?>, List<Field>> instanceFieldCache = new ConcurrentHashMap<>();
	/** 方法调用栈元素缓存 */
	private static final Map<StackTraceElement, Method> stackTraceMethodCache = new ConcurrentHashMap<>();
	/** 获取方法调用栈元素的方法反射对象 */
	private static final Method getStackTraceElementMethod;
	/**
	 * 初始化getStackTraceElementMethod并将其设为可访问的
	 */
	static {
		try {
			getStackTraceElementMethod = Throwable.class.getDeclaredMethod("getStackTraceElement", int.class);
			getStackTraceElementMethod.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** 数组类型格式字符串 */
	public static final String ARRAY_TYPE_FORMAT = "[]";

	/**
	 * Constructor
	 *
	 * @date 2018年11月7日
	 * @author huanglj
	 */
	private ClassUtils() {
	}

	/**
	 * 获得类型修饰词对象
	 * 
	 * @param clazz
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static Modifiers getClassModifiers(Class<?> clazz) {
		return getClassInfo(clazz).getModifiers();
	}
	
	/**
	 * 获得属性修饰词对象
	 * 
	 * @param field
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static Modifiers getFieldModifiers(Field field) {
		FieldInfo fieldInfo = getFieldInfo(field);
		if(fieldInfo == null) {
			return null;
		}
		return fieldInfo.getModifiers();
	}
	
	/**
	 * 获得构造方法修饰词对象
	 * 
	 * @param constructor
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static Modifiers getConstructorModifiers(Constructor<?> constructor) {
		ConstructorInfo constructorInfo = getConstructorInfo(constructor);
		if(constructorInfo == null) {
			return null;
		}
		return constructorInfo.getModifiers();
	}

	/**
	 * 获得方法修饰词对象
	 * 
	 * @param method
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static Modifiers getMethodModifiers(Method method) {
		MethodInfo methodInfo = getMethodInfo(method);
		if(methodInfo == null) {
			return null;
		}
		return methodInfo.getModifiers();
	}

	/**
	 * 获得构造方法参数名称
	 * 
	 * @param constructor 构造方法反射对象
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static String[] getConstructorParameterNames(Constructor<?> constructor) {
		ConstructorInfo constructorInfo = getConstructorInfo(constructor);
		if(constructorInfo == null) {
			return null;
		}
		return constructorInfo.getParameterNames();
	}

	/**
	 * 获得构造方法第index个参数名称
	 * 
	 * @param constructor  构造方法反射对象
	 * @param index        参数位置索引
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static String getConstructorParameterName(Constructor<?> constructor, int index) {
		String[] constructorParameterNames = getConstructorParameterNames(constructor);
		if(constructorParameterNames == null || constructorParameterNames.length <= index) {
			return null;
		}
		return constructorParameterNames[index];
	}

	/**
	 * 获得方法参数名称
	 * 
	 * @param method    方法反射对象
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static String[] getMethodParameterNames(Method method) {
		MethodInfo methodInfo = getMethodInfo(method);
		if(methodInfo == null) {
			return null;
		}
		return methodInfo.getParameterNames();
	}

	/**
	 * 获得方法第index个参数名称
	 * 
	 * @param method    方法反射对象
	 * @param index     参数位置索引
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static String getMethodParameterName(Method method, int index) {
		String[] methodParameterNames = getMethodParameterNames(method);
		if(methodParameterNames == null || methodParameterNames.length <= index) {
			return null;
		}
		return methodParameterNames[index];
	}

	/**
	 * 获得类型信息
	 * 
	 * @param clazz
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static ClassInfo getClassInfo(Class<?> clazz) {
		ClassInfo classInfo = classInfoMapping.get(clazz);
		if (classInfo == null) {
			try {
				classInfo = createClassInfo(clazz);
			} catch (Exception e) {
				classInfo = new ClassInfo(clazz, null, null);
			}
			classInfoMapping.put(clazz, classInfo);
		}
		return classInfo;
	}

	/**
	 * 获得属性信息
	 * 
	 * @param field
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static FieldInfo getFieldInfo(Field field) {
		return getClassInfo(field.getDeclaringClass()).getFieldInfoMapping().get(field);
	}

	/**
	 * 获得构造方法信息
	 * 
	 * @param constructor
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static ConstructorInfo getConstructorInfo(Constructor<?> constructor) {
		return getClassInfo(constructor.getDeclaringClass()).getConstructorInfoMapping().get(constructor);
	}

	/**
	 * 获得方法信息
	 * 
	 * @param method
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static MethodInfo getMethodInfo(Method method) {
		return getClassInfo(method.getDeclaringClass()).getMethodInfoMapping().get(method);
	}

	/**
	 * 获得当前方法的方法调用栈的第stackTraceIndex层元素，此方法和getStackTraceMethodInfo(stackTraceIndex)配合使用
	 * 
	 * @param stackTraceIndex  调用栈层级，当前方法为0，调用该方法的方法为1，依次类推
	 * @return
	 * @created 2016年7月29日
	 * @author  huanglj
	 */
	public static StackTraceElement getStackTraceElement(int stackTraceIndex) {
		try {
			return (StackTraceElement)getStackTraceElementMethod.invoke(new Throwable(), stackTraceIndex);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获得当前方法的方法调用栈的第stackTraceIndex层的方法调用信息
	 * 
	 * @param stackTraceIndex  调用栈层级，当前方法为0，调用该方法的方法为1，依次类推
	 * @return
	 * @created 2016年7月29日
	 * @author  huanglj
	 */
	public static StackTraceMethodInfo getStackTraceMethodInfo(int stackTraceIndex) {
		try {//为使调用栈层级一致，此处不能调用getStackTraceElement(stackTraceIndex)获取StackTraceElement
			StackTraceElement stackTraceElement = (StackTraceElement)getStackTraceElementMethod.invoke(new Throwable(), stackTraceIndex);
			Class<?> clazz = org.apache.commons.lang3.ClassUtils.getClass(stackTraceElement.getClassName());
			Map<Method, MethodInfo> methodInfoMapping = getClassInfo(clazz).getMethodInfoMapping();
			for (Entry<Method, MethodInfo> entry : methodInfoMapping.entrySet()) {
				Method method = entry.getKey(); 
				MethodInfo methodInfo = entry.getValue();
				if(methodInfo != null && method.getName().equals(stackTraceElement.getMethodName()) &&
						stackTraceElement.getLineNumber() >= methodInfo.getStartLineNumber() &&
						stackTraceElement.getLineNumber() <= methodInfo.getEndLineNumber()) {
					StackTraceMethodInfo stackTraceMethodInfo = new StackTraceMethodInfo();
					stackTraceMethodInfo.setMethodInfo(methodInfo);
					stackTraceMethodInfo.setLineNumber(stackTraceElement.getLineNumber());
					return stackTraceMethodInfo;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	/**
	 * 获得当前方法的方法调用栈中标有atClass注解的方法调用信息，没有找到返回null
	 * 
	 * @param atClass  注解类型
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static StackTraceMethodInfo getStackTraceMethodInfo(Class<? extends Annotation> atClass) {
		StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
		for (int i = 1; i < stackTraceElements.length; i++) {
			StackTraceElement stackTraceElement = stackTraceElements[i];
			try {
				Class<?> clazz = org.apache.commons.lang3.ClassUtils.getClass(stackTraceElement.getClassName());
				Map<Method, MethodInfo> methodInfoMapping = getClassInfo(clazz).getMethodInfoMapping();
				for (Entry<Method, MethodInfo> entry : methodInfoMapping.entrySet()) {
					Method method = entry.getKey();
					MethodInfo methodInfo = entry.getValue();
					if(methodInfo != null && method.getName().equals(stackTraceElement.getMethodName()) 
							&& method.getAnnotation(atClass) != null &&
							stackTraceElement.getLineNumber() >= methodInfo.getStartLineNumber() &&
							stackTraceElement.getLineNumber() <= methodInfo.getEndLineNumber()) {
						StackTraceMethodInfo stackTraceMethodInfo = new StackTraceMethodInfo();
						stackTraceMethodInfo.setMethodInfo(methodInfo);
						stackTraceMethodInfo.setLineNumber(stackTraceElement.getLineNumber());
						return stackTraceMethodInfo;
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	/**
	 * 格式化type类型为字符串
	 * 
	 * @param type  类型
	 * @return
	 * @created 2016年6月24日
	 * @author  huanglj
	 */
	public static String formatType(Type type) {
		if (!(type instanceof Class)) {
			return type.toString();
		}
		Class<?> clazz = (Class<?>) type;
		if (clazz.isArray()) {
			return formatType(clazz.getComponentType()) + ARRAY_TYPE_FORMAT;
		}
		return clazz.getName();
	}

	/**
	 * 解析str指定的类型，支持泛型类型解析
	 * 
	 * @param str
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static Type parseType(String str) {
		return parseType(str, null);
	}

	/**
	 * 解析str指定的类型，支持泛型类型和泛型变量解析
	 * 
	 * @param str
	 * @param typeVariableMapping 泛型变量与其上界映射
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static Type parseType(String str, Map<String, Class<?>> typeVariableMapping) {
		if(StringUtils.isBlank(str)) {
			return null;
		}
		str = str.replaceAll("\\s", Literal.EMPTY);
		try {
			if(str.matches("\\[\\]$")) {
				return getArrayType(parseType(str.substring(0, str.length()-2), typeVariableMapping));
			}
			if(str.matches("^.+<.+>$")) {
				int ltIndex = str.indexOf('<');
				ParameterizedTypeImpl type = new ParameterizedTypeImpl(getClass(str.substring(0, ltIndex)));
				int tStackSize = 0;
				int startIndex = ltIndex + 1;
				for (int endIndex = startIndex; endIndex < str.length()-1; endIndex++) {
					char c = str.charAt(endIndex);
					if(c == '<') {
						tStackSize++;
					} else if(c == '>') {
						tStackSize--;
					}
					if(tStackSize == 0 && c == ',') {
						if(startIndex < endIndex) {
							type.addActualTypeArguments(parseType(str.substring(startIndex, endIndex), typeVariableMapping));
						}
						startIndex = endIndex + 1;
					}
				}
				if(startIndex < str.length()-1) {
					type.addActualTypeArguments(parseType(str.substring(startIndex, str.length()-1), typeVariableMapping));
				}
				return type;
			}
			if(str.equals("?")) {
				return new WildcardTypeImpl();
			}
			if(str.startsWith("?extends")) {
				return new WildcardTypeImpl(parseType(str.substring(8), typeVariableMapping));
			} else if(str.startsWith("?super")) {
				return new WildcardTypeImpl(null, parseType(str.substring(6), typeVariableMapping));
			}
			if(typeVariableMapping != null && typeVariableMapping.keySet().contains(str)) {
				return typeVariableMapping.get(str);
			}
			return getClass(str);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获得泛型类型对象
	 * 
	 * @param rawType               原始类型
	 * @param actualTypeArguments   泛型参数类型集合
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static ParameterizedType getParameterizedType(Class<?> rawType, Type... actualTypeArguments) {
		return new ParameterizedTypeImpl(rawType, actualTypeArguments);
	}

	/**
	 * 获得List的泛型类型对象
	 * 
	 * @param elementType  List元素的类型
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static ParameterizedType getListType(Type elementType) {
		return getParameterizedType(List.class, elementType);
	}
	
	/**
	 * 获得Map的泛型类型对象，key为String类型，value为elementType指定类型
	 * 
	 * @param elementType  Map元素的类型
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static ParameterizedType getMapType(Type elementType) {
		return getParameterizedType(Map.class, String.class, elementType);
	}

	/**
	 * 获得元素类型为componentType的数组类型
	 * 
	 * @param componentType 指定的数组元素类型
	 * @return
	 * @created 2016年7月11日
	 * @author  huanglj
	 */
	public static Type getArrayType(Type componentType) {
		try {
			if(componentType instanceof Class) {
				return org.apache.commons.lang3.ClassUtils.getClass(
						formatType(componentType) + ARRAY_TYPE_FORMAT);
			}
			return new GenericArrayTypeImpl(componentType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获得type的原始类型对象
	 * 
	 * @param type  List元素的类型
	 * @param typeProviders
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static Class<?> getRawClass(Type type, Type... typeProviders) {
		if(type instanceof Class) {
			return (Class<?>)type;
		}
		if(type instanceof GenericArrayType) {
			return (Class<?>)getArrayType(getRawClass(((GenericArrayType)type).getGenericComponentType(), typeProviders));
		}
		if(type instanceof ParameterizedType) {
			return getRawClass(((ParameterizedType)type).getRawType(), typeProviders);
		}
		if(type instanceof WildcardType) {
			WildcardType wildcard = (WildcardType)type;
			Type[] lowerBounds = wildcard.getLowerBounds();
			return getRawClass(lowerBounds.length == 0 ? wildcard.getUpperBounds()[0] : lowerBounds[0], typeProviders);
		}
		if(type instanceof TypeVariable) {
			TypeVariable<? extends GenericDeclaration> typeVariable = (TypeVariable<?>)type;
			Class<?> rawClass = getRawClass(typeVariable.getBounds()[0], typeProviders);
			if(typeProviders != null && typeProviders.length > 0 && typeVariable.getGenericDeclaration() instanceof Class) {
				Class<?> genericDeclaration = (Class<?>)typeVariable.getGenericDeclaration();
				for (Type typeProvider : typeProviders) {
					if(genericDeclaration.isAssignableFrom(getRawClass(typeProvider))) {
						for (int i = 0; i < genericDeclaration.getTypeParameters().length; i++) {
							if(typeVariable.equals(genericDeclaration.getTypeParameters()[i])) {
								Class<?> actualType = getActualTypeArguments(genericDeclaration, typeProvider, typeProviders)[i];
								if(actualType != rawClass) {
									return actualType;
								}
								break;
							}
						}
						
					}
				}
			}
			return rawClass;
		}
		throw new RuntimeException("Unknown type for " + type + "!");
	}

	/**
	 * 根据继承类型获得原始类型的实际类型参数(没有指定取泛型上界)
	 * 
	 * @param rawType     原始类型
	 * @param extendType  继承类型
	 * @param typeProviders
	 * @return
	 * @created 2016年7月14日
	 * @author  huanglj
	 */
	public static Class<?>[] getActualTypeArguments(Class<?> rawType, Type extendType, Type... typeProviders) {
		if(!rawType.isAssignableFrom(getRawClass(extendType))) {
			return null;
		}
		TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
		if(typeParameters.length == 0) {
			return new Class<?>[0];
		}
		Class<?>[] actualTypeArguments = null;
		Map<Type, Class<?>[]> actualTypeArgumentsCache = extendActualTypeArgumentsCache.get(rawType);
		if(actualTypeArgumentsCache != null) {
			actualTypeArguments = actualTypeArgumentsCache.get(extendType);
		} else {
			actualTypeArgumentsCache = new HashMap<>();
			extendActualTypeArgumentsCache.put(rawType, actualTypeArgumentsCache);
		}
		if(actualTypeArguments != null) {
			return actualTypeArguments;
		}
		actualTypeArguments = new Class<?>[typeParameters.length];
		List<Type> extendLink = new ArrayList<>();
		Type itemType = extendType;
		do {
			extendLink.add(itemType);
		} while ((itemType = checkTypeDirectCompatible(rawType, itemType)) != null);
		extendLink.addAll(getDirectCompatibleExtendLink(rawType, extendLink.remove(extendLink.size()-1)));
		//key: rawType -> extendType迭代的参数下标，rawType的参数下标
		Map<Integer, Integer> indexMapping = new HashMap<>();
		for (int i = 0; i < actualTypeArguments.length; i++) {
			indexMapping.put(i, i);
		}
		for (int i = extendLink.size()-1; i >= 0; i--) {
			if(indexMapping.isEmpty() || !(extendLink.get(i) instanceof ParameterizedType)) {
				break;
			}
			Type[] params = ((ParameterizedType)extendLink.get(i)).getActualTypeArguments();
			Map<Integer, Integer> nextIndexMapping = new HashMap<Integer, Integer>();
			for (Entry<Integer, Integer> entry : indexMapping.entrySet()) {
				Integer j = entry.getKey();
				Type param = params[j];
				actualTypeArguments[entry.getValue()] = getRawClass(param, typeProviders);
				if(i > 0 && (param instanceof TypeVariable) && (extendLink.get(i-1) instanceof ParameterizedType)) {
					TypeVariable<?>[] vars = getRawClass(extendLink.get(i-1)).getTypeParameters();
					for (int k = 0; k < vars.length; k++) {
						if(vars[k].equals(param)) {
							nextIndexMapping.put(k, entry.getValue());
							break;
						}
					}
				}
			}
			indexMapping = nextIndexMapping;
		}
		for (int i = 0; i < actualTypeArguments.length; i++) {
			if(actualTypeArguments[i] == null) {
				actualTypeArguments[i] = getRawClass(typeParameters[i], typeProviders);
			}
		}
		actualTypeArgumentsCache.put(extendType, actualTypeArguments);
		return actualTypeArguments;
	}

	/**
	 * 获得clazz指定类型的属性和读方法的映射关系
	 * 
	 * @param clazz                类型对象
	 * @return Map<String, Method> key: 属性名，value： 读方法反射对象
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static Map<String, Method> getReadMethodMapping(Class<?> clazz) {
		Map<String, Method> getLowerMapping = readMethodMappingCache.get(clazz);
		if(getLowerMapping != null) {
			return getLowerMapping;
		}
		getLowerMapping = new HashMap<>();
		Map<String, Method> getUpperMapping = new HashMap<>();
		Map<String, Method> isLowerMapping = new HashMap<>();
		Map<String, Method> isUpperMapping = new HashMap<>();
		for (Method method : clazz.getMethods()) {
			if(Modifier.isStatic(method.getModifiers()) || method.getParameterTypes().length > 0) {
				continue;
			}
			String methodName = method.getName();
			if(method.getReturnType() == boolean.class && methodName.startsWith("is")) {
				if(methodName.matches("^is[a-z_].*$")) {
					putReadMethodToMap(isLowerMapping, methodName.substring(2), method);
				} else if(methodName.matches("^is[A-Z].*$")) {
					putReadMethodToMap(isUpperMapping, methodName.substring(2, 3).toLowerCase() + methodName.substring(3), method);
				}
			} else {
				if(methodName.matches("^get[a-z_].*$")) {
					putReadMethodToMap(getLowerMapping, methodName.substring(3), method);
				} else if(methodName.matches("^get[A-Z].*$")) {
					putReadMethodToMap(getUpperMapping, methodName.substring(3, 4).toLowerCase() + methodName.substring(4), method);
				}
			}
		}
		for (Entry<String, Method> entry : getUpperMapping.entrySet()) {
			getLowerMapping.put(entry.getKey(), entry.getValue());
		}
		for (Entry<String, Method> entry : isLowerMapping.entrySet()) {
			getLowerMapping.put(entry.getKey(), entry.getValue());
		}
		for (Entry<String, Method> entry : isUpperMapping.entrySet()) {
			getLowerMapping.put(entry.getKey(), entry.getValue());
		}
		getLowerMapping = Collections.unmodifiableMap(getLowerMapping);
		readMethodMappingCache.put(clazz, getLowerMapping);
		return getLowerMapping;
	}

	/**
	 * 获得clazz指定类型的属性和写方法的映射关系
	 * 
	 * @param clazz                类型对象
	 * @return Map<String, Method> key: 属性名，value： 写方法反射对象
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static Map<String, Method> getWriteMethodMapping(Class<?> clazz) {
		Map<String, Method> setLowerMapping = writeMethodMappingCache.get(clazz);
		if(setLowerMapping != null) {
			return setLowerMapping;
		}
		setLowerMapping = new HashMap<>();
		Map<String, Method> setUpperMapping = new HashMap<>();
		for (Method method : clazz.getMethods()) {
			if(Modifier.isStatic(method.getModifiers()) || method.getParameterTypes().length != 1) {
				continue;
			}
			String methodName = method.getName();
			if(methodName.matches("^set[a-z_].*$")) {
				putWriteMethodToMap(setLowerMapping, methodName.substring(3), method);
			} else if(methodName.matches("^set[A-Z].*$")) {
				putWriteMethodToMap(setUpperMapping, methodName.substring(3, 4).toLowerCase() + methodName.substring(4), method);
			}
		}
		for (Entry<String, Method> entry : setUpperMapping.entrySet()) {
			setLowerMapping.put(entry.getKey(), entry.getValue());
		}
		setLowerMapping = Collections.unmodifiableMap(setLowerMapping);
		writeMethodMappingCache.put(clazz, setLowerMapping);
		return setLowerMapping;
	}

	/**
	 * 获得类的所有实例(非静态)属性
	 * 
	 * @param clazz
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static List<Field> getInstanceFields(Class<?> clazz) {
		List<Field> fields = instanceFieldCache.get(clazz);
		if(fields != null) {
			return fields;
		}
		fields = new ArrayList<>();
		for (Field field : clazz.getDeclaredFields()) {
			Modifiers modifiers = getFieldModifiers(field);
			if(modifiers != null && !modifiers.isStatic()) {
				fields.add(field);
			}
		}
		Class<?> superClass = clazz.getSuperclass();
		if(superClass != null) {
			fields.addAll(getInstanceFields(superClass));
		}
		instanceFieldCache.put(clazz, fields);
		return fields;
	}

	/**
	 * 根据类名获取类型对象，默认解析包：java.lang、java.util、java.math
	 * 
	 * @param className  类名
	 * @return
	 * @created 2016年6月19日
	 * @author  huanglj
	 */
	public static Class<?> getClass(String className) {
		try {
			return org.apache.commons.lang3.ClassUtils.getClass(className);
		} catch (ClassNotFoundException e) {
			if(className.matches("^[A-Z]\\w{2,}$")) {
				try {
					return org.apache.commons.lang3.ClassUtils.getClass("java.lang." + className);
				} catch (ClassNotFoundException e2) {
					try {
						return org.apache.commons.lang3.ClassUtils.getClass("java.util." + className);
					} catch (ClassNotFoundException e3) {
						try {
							return org.apache.commons.lang3.ClassUtils.getClass("java.math." + className);
						} catch (ClassNotFoundException e4) {
						}
					}
				}
			}
			throw new RuntimeException(e);
		}
	}

	/***
	 * 获得方法调用栈元素所在的方法对象
	 *
	 * @param stackTraceElement
	 * @return
	 * @date 2018年9月8日
	 * @author huanglj
	 */
	public static Method getMethod(StackTraceElement stackTraceElement) {
		Method method = stackTraceMethodCache.get(stackTraceElement);
		if(method == null) {
			Map<Method, MethodInfo> methodInfoMapping = getClassInfo(getClass(stackTraceElement.getClassName())).getMethodInfoMapping();
			for (Entry<Method, MethodInfo> entry : methodInfoMapping.entrySet()) {
				Method m = entry.getKey();
				if(!m.getName().equals(stackTraceElement.getMethodName())) {
					continue;
				}
				MethodInfo methodInfo = entry.getValue();
				if(stackTraceElement.getLineNumber() >= methodInfo.getStartLineNumber() && stackTraceElement.getLineNumber() <= methodInfo.getEndLineNumber()) {
					method = m;
					stackTraceMethodCache.put(stackTraceElement, method);
					break;
				}
			}
		}
		return method;
	}

	/**
	 * 检查继承类型和原始类型的直接兼容性(即原始类型为继承类型自身或继承类型实现的接口)，如果不兼容返回继承类型的父类型，否则返回null
	 * 
	 * @param rawType     原始类型
	 * @param extendType  继承类型
	 * @return
	 * @created 2016年7月13日
	 * @author  huanglj
	 */
	private static Type checkTypeDirectCompatible(Class<?> rawType, Type extendType) {
		Class<?> extendClass = getRawClass(extendType);
		if(rawType == extendClass) {
			return null;
		}
		if(rawType.isInterface()) {
			for (Class<?> itemClass : extendClass.getInterfaces()) {
				if(rawType == itemClass) {
					return null;
				}
				if(org.apache.commons.lang3.ClassUtils.getAllInterfaces(itemClass).contains(rawType)) {
					return null;
				}
			}
		}
		return extendClass.getGenericSuperclass();
	}

	/**
	 * 获得直接兼容继承链
	 * 
	 * @param rawType     原始类型
	 * @param extendType  继承类型
	 * @return
	 * @created 2016年7月14日
	 * @author  huanglj
	 */
	private static List<Type> getDirectCompatibleExtendLink(Class<?> rawType, Type extendType) {
		List<Type> extendLink = new ArrayList<>();
		extendLink.add(extendType);
		Class<?> extendClass = getRawClass(extendType);
		if(rawType == extendClass) {
			return extendLink;
		}
		for (Type itemType : extendClass.getGenericInterfaces()) {
			Class<?> itemClass = getRawClass(itemType);
			if(rawType == itemClass || org.apache.commons.lang3.ClassUtils.getAllInterfaces(itemClass).contains(rawType)) {
				extendLink.addAll(getDirectCompatibleExtendLink(rawType, itemType));
				break;
			}
		}
		return extendLink;
	}

	/**
	 * 创建指定类型的类型信息对象
	 * 
	 * @param clazz
	 * @return
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	private static ClassInfo createClassInfo(Class<?> clazz) throws Exception {
		//创建clazz的CtClass对象
		CtClass ctClass = createCtClass(clazz);
		//创建clazz的ClassInfo对象
		ClassInfo classInfo = new ClassInfo(clazz, ctClass, new SsistModifier(ctClass.getModifiers()));
		for (TypeVariable<?> typeVariable : clazz.getTypeParameters()) {
			classInfo.getTypeVariableMapping().put(typeVariable.getName(), getRawClass(typeVariable));
		}
		//设置泛型变量与其上界映射为只读
		classInfo.setTypeVariableMapping(Collections.unmodifiableMap(classInfo.getTypeVariableMapping()));
		for (CtField ctField : ctClass.getDeclaredFields()) {
			Field field = clazz.getDeclaredField(ctField.getName());
			FieldInfo fieldInfo = new FieldInfo(field, ctField, new SsistModifier(ctField.getModifiers()));
			classInfo.getFieldInfoMapping().put(field, fieldInfo);
		}
		//设置属性信息映射为只读
		classInfo.setFieldInfoMapping(Collections.unmodifiableMap(classInfo.getFieldInfoMapping()));
		for (CtConstructor ctConstructor : ctClass.getDeclaredConstructors()) {
			ConstructorInfo constructorInfo = createConstructorInfo(classInfo, ctConstructor);
			classInfo.getConstructorInfoMapping().put(constructorInfo.getOwnerConstructor(), constructorInfo);
		}
		//设置构造方法信息映射为只读
		classInfo.setConstructorInfoMapping(Collections.unmodifiableMap(classInfo.getConstructorInfoMapping()));
		for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
			MethodInfo methodInfo = createMethodInfo(classInfo, ctMethod);
			classInfo.getMethodInfoMapping().put(methodInfo.getOwnerMethod(), methodInfo);
		}
		//设置方法信息映射为只读
		classInfo.setMethodInfoMapping(Collections.unmodifiableMap(classInfo.getMethodInfoMapping()));
		return classInfo;
	}

	private static CtClass createCtClass(Class<?> clazz) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		String name = clazz.getName();
		try {
			return pool.get(name);
		} catch (Exception e) {
			Class<?> type = clazz;
			while (type.isArray()) {
				type = type.getComponentType();
			}
			try {
				pool.insertClassPath(new ClassClassPath(type));
				return pool.get(name);
			} catch (Exception e2) {
				try {
					pool.insertClassPath(type.getProtectionDomain().getCodeSource().getLocation().getPath());
					return pool.get(name);
				} catch (Exception e3) {
					pool.insertClassPath(new LoaderClassPath(type.getClassLoader()));
					return pool.get(name);
				}
			}
		}
	}

	private static ConstructorInfo createConstructorInfo(ClassInfo classInfo, CtConstructor ctConstructor) throws Exception {
		CtClass[] ctParameterTypes = ctConstructor.getParameterTypes();
		Class<?>[] parameterTypes = new Class<?>[ctParameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			parameterTypes[i] = org.apache.commons.lang3.ClassUtils.getClass(ctParameterTypes[i].getName());
		}
		Constructor<?> constructor = classInfo.getOwnerClass().getDeclaredConstructor(parameterTypes);
		ConstructorInfo constructorInfo = new ConstructorInfo(constructor, ctConstructor, new SsistModifier(ctConstructor.getModifiers()));
		try {
			String[] parameterNames = new String[parameterTypes.length];
			constructorInfo.setParameterNames(parameterNames);
			constructorInfo.setStartLineNumber(ctConstructor.getMethodInfo().getLineNumber(Integer.MIN_VALUE));
			constructorInfo.setEndLineNumber(ctConstructor.getMethodInfo().getLineNumber(Integer.MAX_VALUE));
			if(ctConstructor.getMethodInfo().getCodeAttribute() == null) {
				return constructorInfo;
			}
			LocalVariableAttribute localVariableAttribute = (LocalVariableAttribute)ctConstructor
					.getMethodInfo().getCodeAttribute().getAttribute(LocalVariableAttribute.tag);
			if(localVariableAttribute == null) {
				return constructorInfo;
			}
			Map<Integer, String> localVariableNames = new HashMap<>();
			for (int i = 0; i < localVariableAttribute.tableLength(); i++) {
				localVariableNames.put(localVariableAttribute.index(i), localVariableAttribute.variableName(i));
			}
			List<Integer> localVariableNameIndexs = new ArrayList<>(localVariableNames.keySet());
			Collections.sort(localVariableNameIndexs);
			for (int i = 0; i < parameterNames.length && i + 1 < localVariableNameIndexs.size(); i++) {
				parameterNames[i] = localVariableNames.get(localVariableNameIndexs.get(i + 1));
			}
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return constructorInfo;
	}

	private static MethodInfo createMethodInfo(ClassInfo classInfo, CtMethod ctMethod) throws Exception {
		CtClass[] ctParameterTypes = ctMethod.getParameterTypes();
		Class<?>[] parameterTypes = new Class<?>[ctParameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			parameterTypes[i] = org.apache.commons.lang3.ClassUtils.getClass(ctParameterTypes[i].getName());
		}
		Method method = classInfo.getOwnerClass().getDeclaredMethod(ctMethod.getName(), parameterTypes);
		MethodInfo methodInfo = new MethodInfo(method, ctMethod, new SsistModifier(ctMethod.getModifiers()));
		try {
			Map<String, Class<?>> typeVariableMapping = new HashMap<>();
			typeVariableMapping.putAll(classInfo.getTypeVariableMapping());
			for (TypeVariable<?> typeVariable : method.getTypeParameters()) {
				Class<?> bound = getRawClass(typeVariable);
				methodInfo.getTypeVariableMapping().put(typeVariable.getName(), bound);
				typeVariableMapping.put(typeVariable.getName(), bound);
			}
			//设置方法泛型变量与其上界映射为只读
			methodInfo.setTypeVariableMapping(Collections.unmodifiableMap(methodInfo.getTypeVariableMapping()));
			if(typeVariableMapping.isEmpty()) {
				typeVariableMapping = null;
			}
			String[] parameterNames = new String[parameterTypes.length];
			methodInfo.setParameterNames(parameterNames);
			methodInfo.setStartLineNumber(ctMethod.getMethodInfo().getLineNumber(Integer.MIN_VALUE));
			methodInfo.setEndLineNumber(ctMethod.getMethodInfo().getLineNumber(Integer.MAX_VALUE));
			CodeAttribute codeAttribute = ctMethod.getMethodInfo().getCodeAttribute();
			if(codeAttribute == null) {
				return methodInfo;
			}
			LocalVariableAttribute localVariableAttribute = (LocalVariableAttribute)codeAttribute.getAttribute(LocalVariableAttribute.tag);
			if(localVariableAttribute == null) {
				return methodInfo;
			}
			LineNumberAttribute lineNumberAttribute = (LineNumberAttribute)codeAttribute.getAttribute(LineNumberAttribute.tag);
			List<Integer> lineNumbers = new ArrayList<>();
			if(lineNumberAttribute != null) {
				Map<Integer, Integer> lineNumberMapping = new HashMap<>();
				for (int i = 0; i < lineNumberAttribute.tableLength(); i++) {
					lineNumberMapping.put(lineNumberAttribute.startPc(i), lineNumberAttribute.lineNumber(i));
				}
				List<Integer> startPcs = new ArrayList<>(lineNumberMapping.keySet());
				Collections.sort(startPcs);
				for (Integer startPc : startPcs) {
					lineNumbers.add(lineNumberMapping.get(startPc));
				}
			}
			Map<String, List<LocalVariableInfo>> localVariableInfoMapping = new HashMap<>();
			int maxIndex = 0;
			for (int i = 0; i < localVariableAttribute.tableLength(); i++) {
				LocalVariableInfo localVariableInfo = new LocalVariableInfo();
				localVariableInfo.setName(localVariableAttribute.variableName(i));
				localVariableInfo.setType(parseType(Descriptor.toString(localVariableAttribute.descriptor(i)), typeVariableMapping));
				localVariableInfo.setOwnerMethod(method);
				int index = localVariableAttribute.index(i);
				localVariableInfo.setIndex(index);
				if(index > maxIndex) {
					maxIndex = index;
				}
				if(lineNumberAttribute != null) {
					int startPc = localVariableAttribute.startPc(i);
					localVariableInfo.setStartLineNumber(lineNumberAttribute.toLineNumber(startPc));
					localVariableInfo.setLineNumber(lineNumberAttribute.toLineNumber(startPc-1));
					if(localVariableInfo.getLineNumber() < methodInfo.getStartLineNumber()) {
						localVariableInfo.setLineNumber(methodInfo.getStartLineNumber());
					}
					if(localVariableInfo.getLineNumber() < localVariableInfo.getStartLineNumber()) {
						for (int j = lineNumbers.size() - 1; j >= 0; j--) {
							if(lineNumbers.get(j) == localVariableInfo.getLineNumber()) {
								for (int k = j - 1; k >= 0; k--) {
									if(lineNumbers.get(k) > localVariableInfo.getLineNumber()) {
										localVariableInfo.setInitLineNumber(lineNumbers.get(k));
										break;
									}
								}
								break;
							}
						}
					}
					if(localVariableInfo.getInitLineNumber() == 0) {
						localVariableInfo.setInitLineNumber(localVariableInfo.getLineNumber());
					} else if(localVariableInfo.getInitLineNumber() > localVariableInfo.getStartLineNumber()) {
						localVariableInfo.setInitLineNumber(localVariableInfo.getStartLineNumber());
					}
				}
				List<LocalVariableInfo> localVariableInfos = localVariableInfoMapping.get(localVariableInfo.getName());
				if(localVariableInfos == null) {
					localVariableInfos = new ArrayList<>();
					localVariableInfoMapping.put(localVariableInfo.getName(), localVariableInfos);
				}
				localVariableInfos.add(localVariableInfo);
			}
			methodInfo.setLocalVariableInfoMapping(Collections.unmodifiableMap(localVariableInfoMapping));
			Map<Integer, LocalVariableInfo> localVariableMapping = new HashMap<>();
			for (List<LocalVariableInfo> localVariableInfos : localVariableInfoMapping.values()) {
				for (LocalVariableInfo localVariableInfo : localVariableInfos) {
					localVariableMapping.put(localVariableInfo.getLineNumber() * (maxIndex + 1) + localVariableInfo.getIndex(), localVariableInfo);
				}
			}
			List<Integer> localVariableKeys = new ArrayList<>(localVariableMapping.keySet());
			Collections.sort(localVariableKeys);
			int offset = methodInfo.getModifiers().isStatic() ? 0 : 1;
			for (int i = 0; i < parameterNames.length && i + offset < localVariableKeys.size(); i++) {
				parameterNames[i] = localVariableMapping.get(localVariableKeys.get(i + offset)).getName();
			}
			if(lineNumberAttribute == null) {
				return methodInfo;
			}
			Map<Integer, List<LocalVariableInfo>> localVariableLineNumberMapping = new HashMap<>();
			for (int i = offset + parameterTypes.length; i < localVariableKeys.size(); i++) {
				LocalVariableInfo localVariableInfo = localVariableMapping.get(localVariableKeys.get(i));
				List<LocalVariableInfo> localVariableInfos = localVariableLineNumberMapping.get(localVariableInfo.getLineNumber());
				if(localVariableInfos == null) {
					localVariableInfos = new ArrayList<>();
					localVariableLineNumberMapping.put(localVariableInfo.getLineNumber(), localVariableInfos);
				}
				localVariableInfos.add(localVariableInfo);
			}
			methodInfo.setLocalVariableLineNumberMapping(Collections.unmodifiableMap(localVariableLineNumberMapping));
			LocalVariableTypeAttribute localVariableTypeAttribute = (LocalVariableTypeAttribute)codeAttribute.getAttribute(LocalVariableTypeAttribute.tag);
			if(localVariableTypeAttribute == null) {
				return methodInfo;
			}
			for (int i = 0; i < localVariableTypeAttribute.tableLength(); i++) {
				List<LocalVariableInfo> localVariableInfos = localVariableInfoMapping.get(localVariableTypeAttribute.variableName(i));
				if(CollectionUtils.isEmpty(localVariableInfos)) {
					continue;
				}
				int lineNumber = lineNumberAttribute.toLineNumber(localVariableTypeAttribute.startPc(i)-1);
				if(lineNumber < methodInfo.getStartLineNumber()) {
					lineNumber = methodInfo.getStartLineNumber();
				}
				for (LocalVariableInfo localVariableInfo : localVariableInfos) {
					if(localVariableInfo.getLineNumber() == lineNumber) {
						localVariableInfo.setType(parseType(SignatureAttribute.toFieldSignature(
								localVariableTypeAttribute.descriptor(i)).toString(), typeVariableMapping));
						break;
					}
				}
			}
		} catch (Exception e) {
			log.warn(e.getMessage());
		}
		return methodInfo;
	}

	private static void putReadMethodToMap(Map<String, Method> map, String key, Method value) {
		Method last = map.get(key);
		if(last == null || last.getReturnType().isAssignableFrom(value.getReturnType())) {
			map.put(key, value);
		}
	}

	private static void putWriteMethodToMap(Map<String, Method> map, String key, Method value) {
		Method last = map.get(key);
		if(last == null || last.getParameterTypes()[0].isAssignableFrom(value.getParameterTypes()[0])) {
			map.put(key, value);
		}
	}

	/**
	 * 修饰词接口
	 * 
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public interface Modifiers {
		public int intValue();
		public boolean isPublic();
		public boolean isPrivate();
		public boolean isProtected();
		public boolean isFriendly();
		public boolean isStatic();
		public boolean isFinal();
		public boolean isSynchronized();
		public boolean isVolatile();
		public boolean isTransient();
		public boolean isNative();
		public boolean isInterface();
		public boolean isAbstract();
		public boolean isStrict();
		public boolean isAnnotation();
		public boolean isEnum();
	}

	/**
	 * 局部变量信息
	 * 
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static class LocalVariableInfo extends BaseBean {
		private static final long serialVersionUID = -7352784906112404245L;

		/** 变量名称 */
		private String name;
		/** 变量类型 */
		private Type type;
		/** 所属方法 */
		private Method ownerMethod;
		/** 变量索引 */
		private int index;
		/** 变量定义所在行号 */
		private int lineNumber;
		/** 变量初始化终止行号 */
		private int initLineNumber;
		/** 变量有效使用起始行号 */
		private int startLineNumber;

		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public Type getType() {
			return type;
		}
		
		public void setType(Type type) {
			this.type = type;
		}
		
		public Method getOwnerMethod() {
			return ownerMethod;
		}
		
		public void setOwnerMethod(Method ownerMethod) {
			this.ownerMethod = ownerMethod;
		}
		
		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getLineNumber() {
			return lineNumber;
		}

		public void setLineNumber(int lineNumber) {
			this.lineNumber = lineNumber;
		}

		public int getInitLineNumber() {
			return initLineNumber;
		}

		public void setInitLineNumber(int initLineNumber) {
			this.initLineNumber = initLineNumber;
		}

		public int getStartLineNumber() {
			return startLineNumber;
		}

		public void setStartLineNumber(int startLineNumber) {
			this.startLineNumber = startLineNumber;
		}
	}

	/**
	 * 方法调用信息
	 * 
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static class StackTraceMethodInfo extends BaseBean {
		private static final long serialVersionUID = -6001283443701028914L;
		/** 调用的方法信息 */
		private MethodInfo methodInfo;
		/** 该方法调用下一层方法所在的行号 */
		private int lineNumber;

		public MethodInfo getMethodInfo() {
			return methodInfo;
		}

		public void setMethodInfo(MethodInfo methodInfo) {
			this.methodInfo = methodInfo;
		}

		public int getLineNumber() {
			return lineNumber;
		}
		
		public void setLineNumber(int lineNumber) {
			this.lineNumber = lineNumber;
		}
	}

	/**
	 * 类型信息
	 * 
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static class ClassInfo extends BaseBean {
		private static final long serialVersionUID = 5689505157119916615L;

		/** 所属类型 */
		private final Class<?> ownerClass;
		/** javassist类型 */
		private final CtClass ctClass;
		/** 类型修饰词 */
		private final Modifiers modifiers;
		/** 泛型变量与其上界映射 */
		private Map<String, Class<?>> typeVariableMapping = new HashMap<>();
		/** 属性信息映射 */
		private Map<Field, FieldInfo> fieldInfoMapping = new HashMap<>();
		/** 构造方法信息映射 */
		private Map<Constructor<?>, ConstructorInfo> constructorInfoMapping = new HashMap<>();
		/** 方法映射 */
		private Map<Method, MethodInfo> methodInfoMapping = new HashMap<>();

		protected ClassInfo(Class<?> ownerClass, CtClass ctClass, Modifiers modifiers) {
			this.ownerClass = ownerClass;
			this.ctClass = ctClass;
			this.modifiers = modifiers;
		}

		public Class<?> getOwnerClass() {
			return ownerClass;
		}

		public CtClass getCtClass() {
			return ctClass;
		}

		public Modifiers getModifiers() {
			return modifiers;
		}

		public Map<String, Class<?>> getTypeVariableMapping() {
			return typeVariableMapping;
		}

		public void setTypeVariableMapping(Map<String, Class<?>> typeVariableMapping) {
			this.typeVariableMapping = typeVariableMapping;
		}

		public Map<Field, FieldInfo> getFieldInfoMapping() {
			return fieldInfoMapping;
		}

		public void setFieldInfoMapping(Map<Field, FieldInfo> fieldInfoMapping) {
			this.fieldInfoMapping = fieldInfoMapping;
		}

		public Map<Constructor<?>, ConstructorInfo> getConstructorInfoMapping() {
			return constructorInfoMapping;
		}

		public void setConstructorInfoMapping(
				Map<Constructor<?>, ConstructorInfo> constructorInfoMapping) {
			this.constructorInfoMapping = constructorInfoMapping;
		}

		public Map<Method, MethodInfo> getMethodInfoMapping() {
			return methodInfoMapping;
		}

		public void setMethodInfoMapping(
				Map<Method, MethodInfo> methodInfoMapping) {
			this.methodInfoMapping = methodInfoMapping;
		}
	}

	/**
	 * 属性信息
	 * 
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static class FieldInfo extends BaseBean {
		private static final long serialVersionUID = 5105953637476456205L;

		/** 所属属性 */
		private final Field ownerField;
		/** javassist属性 */
		private final CtField ctField;
		/** 属性修饰词 */
		private final Modifiers modifiers;

		protected FieldInfo(Field ownerField, CtField ctField, Modifiers modifiers) {
			this.ownerField = ownerField;
			this.ctField = ctField;
			this.modifiers = modifiers;
		}

		public Field getOwnerField() {
			return ownerField;
		}

		public CtField getCtField() {
			return ctField;
		}

		public Modifiers getModifiers() {
			return modifiers;
		}
	}

	/**
	 * 构造方法信息
	 * 
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static class ConstructorInfo extends BaseBean {
		private static final long serialVersionUID = -698315292692319778L;

		/** 所属构造方法 */
		private final Constructor<?> ownerConstructor;
		/** javassist构造方法 */
		private final CtConstructor ctConstructor;
		/** 构造方法修饰词 */
		private final Modifiers modifiers;
		/** 构造方法参数名称 */
		private String[] parameterNames;
		/** 方法体起始行号 */
		private int startLineNumber;
		/** 方法体结束行号 */
		private int endLineNumber;

		protected ConstructorInfo(Constructor<?> ownerConstructor, CtConstructor ctConstructor, Modifiers modifiers) {
			this.ownerConstructor = ownerConstructor;
			this.ctConstructor = ctConstructor;
			this.modifiers = modifiers;
		}

		public Constructor<?> getOwnerConstructor() {
			return ownerConstructor;
		}

		public CtConstructor getCtConstructor() {
			return ctConstructor;
		}

		public Modifiers getModifiers() {
			return modifiers;
		}

		public String[] getParameterNames() {
			return parameterNames;
		}

		public void setParameterNames(String[] parameterNames) {
			this.parameterNames = parameterNames;
		}
		
		public int getStartLineNumber() {
			return startLineNumber;
		}

		public void setStartLineNumber(int startLineNumber) {
			this.startLineNumber = startLineNumber;
		}

		public int getEndLineNumber() {
			return endLineNumber;
		}

		public void setEndLineNumber(int endLineNumber) {
			this.endLineNumber = endLineNumber;
		}
	}

	/**
	 * 方法信息
	 * 
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static class MethodInfo extends BaseBean {
		private static final long serialVersionUID = 3603284559940163538L;

		/** 所属方法 */
		private final Method ownerMethod;
		/** javassist方法 */
		private final CtMethod ctMethod;
		/** 方法修饰词 */
		private final Modifiers modifiers;
		/** 泛型变量与其上界映射 */
		private Map<String, Class<?>> typeVariableMapping = new HashMap<>();
		/** 方法参数名称 */
		private String[] parameterNames;
		/** 方法体起始行号 */
		private int startLineNumber;
		/** 方法体结束行号 */
		private int endLineNumber;
		/** 变量名和局部变量信息映射 */
		private Map<String, List<LocalVariableInfo>> localVariableInfoMapping;
		/** 行号和局部变量信息映射 */
		private Map<Integer, List<LocalVariableInfo>> localVariableLineNumberMapping;

		protected MethodInfo(Method ownerMethod, CtMethod ctMethod, Modifiers modifiers) {
			this.ownerMethod = ownerMethod;
			this.ctMethod = ctMethod;
			this.modifiers = modifiers;
		}

		public Method getOwnerMethod() {
			return ownerMethod;
		}

		public CtMethod getCtMethod() {
			return ctMethod;
		}

		public Modifiers getModifiers() {
			return modifiers;
		}

		public Map<String, Class<?>> getTypeVariableMapping() {
			return typeVariableMapping;
		}

		public void setTypeVariableMapping(Map<String, Class<?>> typeVariableMapping) {
			this.typeVariableMapping = typeVariableMapping;
		}

		public String[] getParameterNames() {
			return parameterNames;
		}

		public void setParameterNames(String[] parameterNames) {
			this.parameterNames = parameterNames;
		}
		
		public int getStartLineNumber() {
			return startLineNumber;
		}

		public void setStartLineNumber(int startLineNumber) {
			this.startLineNumber = startLineNumber;
		}

		public int getEndLineNumber() {
			return endLineNumber;
		}

		public void setEndLineNumber(int endLineNumber) {
			this.endLineNumber = endLineNumber;
		}

		public Map<String, List<LocalVariableInfo>> getLocalVariableInfoMapping() {
			return localVariableInfoMapping;
		}

		public void setLocalVariableInfoMapping(Map<String, List<LocalVariableInfo>> localVariableInfoMapping) {
			this.localVariableInfoMapping = localVariableInfoMapping;
		}

		public Map<Integer, List<LocalVariableInfo>> getLocalVariableLineNumberMapping() {
			return localVariableLineNumberMapping;
		}

		public void setLocalVariableLineNumberMapping(Map<Integer, List<LocalVariableInfo>> localVariableLineNumberMapping) {
			this.localVariableLineNumberMapping = localVariableLineNumberMapping;
		}
	}

	/**
	 * 泛型数组类型反射接口实现类
	 * 
	 * @created 2016年7月17日
	 * @author  huanglj
	 */
	public static class GenericArrayTypeImpl extends BaseBean implements GenericArrayType {
		private static final long serialVersionUID = -3916486580940722199L;
		/** 泛型元素类型 */
		private final Type genericComponentType;

		public GenericArrayTypeImpl(Type genericComponentType) {
			this.genericComponentType = genericComponentType;
		}

		@Override
		public Type getGenericComponentType() {
			return genericComponentType;
		}

		@Override
		public String toString() {
			return formatType(genericComponentType) + ARRAY_TYPE_FORMAT;
		}
	}

	/**
	 * 泛型类型反射接口实现类
	 * 
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	public static class ParameterizedTypeImpl extends BaseBean implements ParameterizedType {
		private static final long serialVersionUID = -8390472798246953112L;

		/** 原始类型 */
		private final Class<?> rawType;
		/** 泛型参数类型集合 */
		private final List<Type> actualTypeArguments = new ArrayList<>();

		public ParameterizedTypeImpl(Class<?> rawType, List<Type> actualTypeArguments) {
			this.rawType = rawType;
			this.actualTypeArguments.addAll(actualTypeArguments);
		}

		public ParameterizedTypeImpl(Class<?> rawType, Type... actualTypeArguments) {
			this.rawType = rawType;
			addActualTypeArguments(actualTypeArguments);
		}

		public void addActualTypeArguments(Type... actualTypeArguments) {
			for (Type type : actualTypeArguments) {
				this.actualTypeArguments.add(type);
			}
		}

		@Override
		public Type getRawType() {
			return rawType;
		}

		@Override
		public Type[] getActualTypeArguments() {
			return actualTypeArguments.toArray(new Type[0]);
		}

		@Override
		public Type getOwnerType() {
			return null;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(rawType.getName());
			sb.append("<");
			sb.append(formatType(actualTypeArguments.get(0)));
			for (int i = 1; i < actualTypeArguments.size(); i++) {
				sb.append(", ");
				sb.append(formatType(actualTypeArguments.get(i)));
			}
			sb.append(">");
			return sb.toString();
		}
	}

	/**
	 * 通配符类型反射接口实现类
	 * 
	 * @created 2016年7月17日
	 * @author  huanglj
	 */
	public static class WildcardTypeImpl extends BaseBean implements WildcardType {
		private static final long serialVersionUID = 525356433596372448L;
		private static final Type[] EMPTY_LOWER_BOUNDS = new Type[0];
		/** 上边界，默认为Object */
		private final Type upperBound;
		/** 下边界 */
		private final Type lowerBound;

		public WildcardTypeImpl() {
			this(null);
		}

		public WildcardTypeImpl(Type upperBound) {
			this(upperBound, null);
		}

		public WildcardTypeImpl(Type upperBound, Type lowerBound) {
			this.upperBound =  upperBound == null ? Object.class : upperBound;
			this.lowerBound = lowerBound;
		}

		@Override
		public Type[] getUpperBounds() {
			return new Type[]{upperBound};
		}

		@Override
		public Type[] getLowerBounds() {
			return lowerBound == null ? EMPTY_LOWER_BOUNDS : new Type[]{lowerBound};
		}
		
		@Override
		public String toString() {
			if(upperBound == Object.class) {
				return lowerBound == null ? "?" : "? super " + formatType(lowerBound);
			}
			return "? extends " + formatType(upperBound);
		}
	}

	/**
	 * 修饰词接口的ssist实现
	 * 
	 * @created 2016-7-19
	 * @author  huanglj
	 */
	private static class SsistModifier extends BaseBean implements Modifiers {
		private static final long serialVersionUID = 3264077332022551833L;

		private final int code;

		public SsistModifier(int code) {
			this.code = code;
		}

		@Override
		public int intValue() {
			return code;
		}

		@Override
		public boolean isPublic() {
			return Modifier.isPublic(code);
		}

		@Override
		public boolean isPrivate() {
			return Modifier.isPrivate(code);
		}

		@Override
		public boolean isProtected() {
			return Modifier.isProtected(code);
		}

		@Override
		public boolean isFriendly() {
			return (code & (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED)) == 0;
		}

		@Override
		public boolean isStatic() {
			return Modifier.isStatic(code);
		}

		@Override
		public boolean isFinal() {
			return Modifier.isFinal(code);
		}

		@Override
		public boolean isSynchronized() {
			return Modifier.isSynchronized(code);
		}

		@Override
		public boolean isVolatile() {
			return Modifier.isVolatile(code);
		}

		@Override
		public boolean isTransient() {
			return Modifier.isTransient(code);
		}

		@Override
		public boolean isNative() {
			return Modifier.isNative(code);
		}

		@Override
		public boolean isInterface() {
			return Modifier.isInterface(code);
		}

		@Override
		public boolean isAbstract() {
			return Modifier.isAbstract(code);
		}

		@Override
		public boolean isStrict() {
			return Modifier.isStrict(code);
		}

		@Override
		public boolean isAnnotation() {
			return Modifier.isAnnotation(code);
		}

		@Override
		public boolean isEnum() {
			return Modifier.isEnum(code);
		}
	}

}
