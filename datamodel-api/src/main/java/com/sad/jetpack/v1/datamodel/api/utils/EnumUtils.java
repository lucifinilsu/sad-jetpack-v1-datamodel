package com.sad.jetpack.v1.datamodel.api.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EnumUtils {

    /**
     * 获取枚举类中变量的列表
     *
     * @param enumClass  枚举类的class
     * @param paramClass 参数类型的class
     * @param <E,        T>
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <E extends Enum<E>, T> List<T> getValues(Class<E> enumClass, Class<T> paramClass)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        E[] values = values(enumClass);

        Field field = getField(enumClass, paramClass);
        field.setAccessible(true);
        List<T> resultList = new ArrayList<>();
        for (E e : values) {
            T t = (T) field.get(e);
            resultList.add(t);
        }
        return resultList;
    }

    /**
     * 通过反射调用枚举类的values获取所有枚举类
     *
     * @param enumClass
     * @param <E>
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static <E extends Enum<E>> E[] values(Class<E> enumClass)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method valuesMethod = enumClass.getMethod("values");
        Object valuesObj = valuesMethod.invoke(enumClass);
        E[] values = (E[]) valuesObj;
        return values;
    }

    /**
     * 通过field的类型获取field
     * 说明：
     * 因为枚举类中只有Enum类型的Field,所有可以通过指定Field的类型来获取Field
     *
     * @param enumClass
     * @param paramClass
     * @param <E>
     * @param <T>
     * @return
     */
    private static <E, T> Field getField(Class<E> enumClass, Class<T> paramClass) throws IllegalAccessException {
        //如果是包装类获取包装类的基本类型
        Class basicClass = getBasicClass(paramClass);
        //获取类型相同的Field(类型相同或与其基本类型相同)
        List<Field> fieldList = Arrays.stream(enumClass.getDeclaredFields())
                .filter(f -> f.getType() == paramClass || f.getType() == basicClass).collect(Collectors.toList());
        if (fieldList.size() != 1) {
            //抛出异常，只支持一个属性
            throw new IllegalArgumentException(paramClass + "类型属性数量异常。");
        }
        return fieldList.get(0);
    }

    /**
     * 获取class的基本类型的class
     * 说明：
     * 存在基本类型将返回其基本类型，否则返回null
     * 如传入Integer.class将返回int.class,传入String.class返回null
     *
     * @param paramClass
     * @return
     * @throws IllegalAccessException
     */
    private static Class getBasicClass(Class paramClass) throws IllegalAccessException {
        Field typeField = null;
        try {
            //尝试获取包装类的TYPE
            typeField = paramClass.getField("TYPE");
        } catch (NoSuchFieldException e) {
            return null;
        }
        //获取包装类TYPE成功，获取TYPE属性值（因为类型为static，所以传入null）
        return (Class) typeField.get(null);
    }

    public static <T, E extends Enum<E>> E valueOf(Class<E> eClass, Object... parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class[] paramClassArr = new Class[parameterTypes.length + 2];
        String name = (String) eClass.getMethod("name").invoke(null);
        int ordinal = (int) eClass.getMethod("ordinal").invoke(null);
        paramClassArr[0] = name.getClass();
        paramClassArr[1] = int.class;
        for (int i = 2; i < parameterTypes.length; i++) {
            paramClassArr[i] = parameterTypes[i].getClass();
        }
        Constructor constructor = eClass.getDeclaredConstructor(paramClassArr);
        constructor.setAccessible(true);
        Object o = constructor.newInstance(name, ordinal, parameterTypes);
        return (E) o;
    }

    /**
     * 通过指定参数值获取枚举类型
     *
     * @param enumClass
     * @param value
     * @return
     */
    public static <E extends Enum<E>> E valueOf(Class<E> enumClass, Object value) {
        E[] values = null;
        Field field = null;
        try {
            field = getField(enumClass, value.getClass());
            field.setAccessible(true);
            values = values(enumClass);
            for (E e : values) {
                Object t = field.get(e);
                if (Objects.equals(t, value)) {
                    return e;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
