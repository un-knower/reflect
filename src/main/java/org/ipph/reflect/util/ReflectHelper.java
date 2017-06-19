package org.ipph.reflect.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
/**
 * 反射工具类
 */
public class ReflectHelper {
	/**
	 * 根据属性名称获取对象的Field
	 * @param obj
	 * @param fieldName
	 * @return
	 */
    public static Field getFieldByFieldName(Object obj, String fieldName) {  
    	//利用反射遍历对象的父类，直到找到该obj的父对象为Object时为止
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {  
            try {  
                return superClass.getDeclaredField(fieldName);//领用反射获取对象的Field对象
            } catch (NoSuchFieldException e) {  
            }  
        }  
        return null;  
    }
    /**
     * 通过对象的属性获取对象的Method
     * @param obj
     * @param fieldName
     * @return
     */
    public static Method getMethodByFieldName(Object obj, String fieldName) {  
    	return getMethodByFieldName(obj.getClass(), fieldName);
    }
    /**
     * 通过Class对象获取其属性对应的Method
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Method getMethodByFieldName(Class<?> clazz, String fieldName) {  
    	//利用反射遍历对象的父类，直到找到该obj的父对象为Object时为止
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {  
            try {  
                Field field=superClass.getDeclaredField(fieldName);//领用反射获取对象的Field对象
                
                try {
					return superClass.getDeclaredMethod("set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1), field.getType());
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
            } catch (NoSuchFieldException e) {  
            }  
        }  
        return null;  
    }
  
    
    /** 
     * 通过属性名获取对象的属性值  
     * @param obj 
     * @param fieldName 
     * @return 
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */  
    public static Object getValueByFieldName(Object obj, String fieldName)throws SecurityException, NoSuchFieldException,IllegalArgumentException, IllegalAccessException {  
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;  
        if(field!=null){
            if (field.isAccessible()) {
                value = field.get(obj);  
            } else {
                field.setAccessible(true);  
                value = field.get(obj);
                field.setAccessible(false);
            }  
        }  
        return value;
    }  
  
    /** 
     * @param obj 
     * @param fieldName 
     * @param value 
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */  
    public static void setValueByFieldName(Object obj, String fieldName,Object value) throws SecurityException, NoSuchFieldException,IllegalArgumentException, IllegalAccessException {  
    	Field field=getFieldByFieldName(obj, fieldName);
        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }
    
    public static void setValueByFieldMethod(Object obj, String fieldName,Object value){
    	Field field=getFieldByFieldName(obj, fieldName);
    	Method method=getMethodByFieldName(obj, fieldName);
    	if(null!=method){
    		try {
    			//构造value值
    			Class<?> typeClass=field.getType();
    			Object val=null;
    			if("Long".equals(typeClass.getSimpleName())){
    				val=Long.parseLong(value+"");
    			}else if("Date".equals(typeClass.getSimpleName())){
    				//val=DateFormatUtils.parse(value+"","yyyy-MM-dd");
    			}else if("Integer".equals(typeClass.getSimpleName())){
    				val=Integer.parseInt(value+"");
    			}else{
    				val=value;
    			}
    			method.invoke(obj, val);
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }

}
