package com.magicbricks.test.utiltest;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

@ExtendWith(MockitoExtension.class)
public class BeanTest {

	@Test
	public void test1() {
		try {
			List<String> modellist = new LinkedList<String>() {
				private static final long serialVersionUID = 3689380290852753142L;
				{
					add("com/magicbricks/mbprod/dto");
					add("com/magicbricks/mbprod/model");
					add("com/magicbricks/pojo");
				}
			};
			modellist.stream().forEach(path -> {
				try {
					ClassPathResource res = new ClassPathResource(path);
					File file = res.getFile();
					String[] list = file.list((dir, name) -> name.endsWith(".class"));
					List<String> asList = Arrays.asList(list);
					asList.forEach(filename -> {
						try {
							Class<?> klass = Class.forName(path.replace("/", ".") + "." + filename.split("\\.")[0]);
							Constructor<?>[] cons = klass.getConstructors();
							for (Constructor<?> constructor : cons) {
								try {
									Class<?>[] parameterTypes = constructor.getParameterTypes();
									constructor.setAccessible(true);
									if (parameterTypes != null && parameterTypes.length > 0) {
										List<Object> paramlist = new LinkedList<>();
										for (Class<?> parameterType : parameterTypes) {
											paramlist.add(getInstance(parameterType));
										}
										constructor.newInstance(paramlist.toArray());
									} else {
										constructor.newInstance();
									}
								} catch (Exception e) {
									System.out.println(klass.getSimpleName() + " " + constructor.getName() + "    " + e.getMessage());
									System.out.println(StringUtils.join(constructor.getParameterTypes(), " , "));
								}
							}
							Object ins = klass.newInstance();
							Method[] methods = klass.getMethods();
							for (Method method : methods) {
								try {
									if ("wait".equalsIgnoreCase(method.getName()))
										return;
									if ("canEqual".equalsIgnoreCase(method.getName())) {
										method.invoke(ins, ins);
										return;
									}

									method.setAccessible(true);
									Class<?>[] parameterTypes = method.getParameterTypes();
									if (parameterTypes.length > 0) {
										List<Object> paramlist = new LinkedList<>();
										for (Class<?> parameterType : parameterTypes) {
											paramlist.add(getInstance(parameterType));
										}
										method.invoke(ins, paramlist.toArray());
									} else
										method.invoke(ins);
								} catch (Exception e) {
								}
							}
							ins.equals(ins);
							ins.toString();
							ins.hashCode();
						} catch (Exception e) {
						}
					});
				} catch (Exception e) {
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {

			new BeanTest().test1();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Object getInstance(Class<?> parameterType) {
		// TODO Auto-generated method stub
		String name = parameterType.getSimpleName();
		switch (name) {
		case "int":
			return new Integer(1);
		case "float":
			return new Float(1);
		case "double":
			return new Double("1");
		case "long":
			return new Long(1);
		case "char":
			return 'N';
		case "boolean":
			return Boolean.TRUE;
		case "Integer":
			return new Integer(1);
		case "Float":
			return new Float(1);
		case "Short":
			return new Short("1");
		case "Long":
			return new Long(1);
		case "Double":
			return new Double("1.0");
		case "String":
			return new String("");
		case "Boolean":
			return Boolean.TRUE;
		case "List":
			return new LinkedList<>();
		case "Collection":
			return new LinkedList<>();
		case "Map":
			return new HashMap<>(1);
		case "Set":
			return new HashSet<>(1);
		case "BigDecimal":
			return new BigDecimal(1);
		case "BigInteger":
			return new BigInteger("1");
		case "String[]":
			return new String[] { "1" };
		case "Date":
			String name2 = parameterType.getName();
			if (name2.contains("sql"))
				return new java.sql.Date(new Date().getTime());
			else
				return new Date();
		case "Timestamp":
			return new Timestamp(new Date().getTime());
		case "Calendar":
			return Calendar.getInstance();
		default:
			try {
				String classnme = parameterType.getName();
				return Class.forName(classnme).newInstance();
			} catch (Exception e) {
			}
			return new Object();
		}
	}
}
