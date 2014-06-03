package com.mut8ed.battlemap.server.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mut8ed.battlemap.server.util.annotation.SkipPrinting;



public class ToStringGenerator {

	public static void main(String[] args){
		List<List<String>> a = new ArrayList<List<String>>();
			List<String> b = new ArrayList<String>();
			b.add(new String("a"));
			b.add(new String("b"));
			b.add(new String("c"));
			a.add(b);
			a.add(b);
			a.add(b);
		System.out.println(ToStringGenerator.genToString(a));
	}

	@SkipPrinting
	private static int tabLvl = -1;
	@SkipPrinting private static List<Object> seen = new ArrayList<Object>();

	private static String genTabs(){
		String ret = "";
		for (int i = 0;i<tabLvl;i++)ret+="\t";
		return ret;
	}

	@SuppressWarnings("rawtypes")
	public static String genToString(Object obj) {
		if (seen.contains(obj) && seen.get(seen.indexOf(obj))==obj){
			return null;
		}

		StringBuilder ret = new StringBuilder();
		try {
			tabLvl++;
			if (obj==null)return genTabs()+"null";
			
			String classType = obj.getClass().toString();
			if (!classType.equals("class java.lang.String")){
				seen.add(obj);
			}
			if (obj.getClass().toString().matches("class java.util.*List")){
				List list = (List)obj;

				ret.append(genTabs()+obj.getClass().toString()+" [\n");
				for (Object i : list){
					if (seen.contains(i) && seen.get(seen.indexOf(i))==i){
						continue;
					}

					ret.append(genToString(i)+genTabs()+",\n");
				}
				ret.setLength(ret.length()-2);
				ret.append("\n"+genTabs()+"]\n");


				return ret.toString();
			} else if ((classType.matches("^class java.*") && !classType.contains("Object")) || classType.matches(".*(Date|Calendar).*")){
				ret.append(genTabs()+obj+"\n");
				return ret.toString();
			}
			Map<String, Object> map = readAllFields(obj);
			for (String key : map.keySet()){
				if (key.contains("$"))continue;
				Object value = map.get(key);

				if (value==null){
					ret.append(genTabs()+key+" = "+value+"\n");
					continue;
				}
				classType = value.getClass().toString();
				if (!classType.matches("^class java.*") && !classType.matches(".*(Date|Calendar).*")){
					ret.append(genTabs()+key+" = "+classType+"[\n"+genToString(value)+"]\n");
					
				} else {
					ret.append(genTabs()+key+" = "+value+"\n");
				}
			}
		} finally {
			tabLvl--;
			if (tabLvl==-1){
				seen = new ArrayList<Object>();
			}
		}
		return ret.toString();
	}

	private static Map<String,Object> readAllFields(Object obj) {
		Map<String,Object> ret = new LinkedHashMap<String, Object>();
		ArrayList<Field> fields = new ArrayList<Field>();
		//		for (Field field : obj.getClass().getFields()){
		//		fields.add(field);
		//		//getDeclaredFields();
		//		}
		for (Field field : obj.getClass().getDeclaredFields()){
			if (field.getAnnotation(SkipPrinting.class)!=null){
				continue;
			}
			fields.add(field);
		}
		for (Field field : fields) {

			try {
				field.setAccessible(true);
				String name = field.getName();
				String str = field.getName();
				String a = str.substring(0,1);
				String b = str.substring(1);
				str = a.toUpperCase()+b;
				try {
					Method method = obj.getClass().getDeclaredMethod("get"+str);
					String value = (String)method.invoke(obj);
					ret.put(name, value);
				} catch (Exception e){
					Object got = field.get(obj); 
					ret.put(name, got);
				}
			} catch (IllegalAccessException iae){
				//do nothing.  we'll always get this for private fields
			}
		}
		return ret;
	}

}


