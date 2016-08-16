package ru.sbrf.efs.rmkmcib.bht;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sbt-manayev-iye on 04.08.2016.
 *
 */
public abstract class TestUtils {

    public static final String PutLegalClientManagerTaskRq = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
            "<PutLegalClientManagerTaskRq>" +
            "<RqUID>2093b67dcabc47dabec4b3118bc19d65</RqUID>" +
            "<RqTm>2016-08-08T11:10:57.000907+03:00</RqTm>" +
            "<SPName>urn:sbrfsystems:99-ufs</SPName>" +
            "<Method>IANUAERMCreateAction</Method>" +
            "<Message><Login>sbt-ivanov-ii</Login><ActionInfo/></Message>" +
            "</PutLegalClientManagerTaskRq>";

    public static Method getPrivateMethod(String name, Object instance) {
        Method method = ReflectionUtils.findMethod(instance.getClass(), name);
        if (method != null)
            ReflectionUtils.makeAccessible(method);
        return method;
    }

    public static void setPrivateField(String fieldName, Object instance, Object value) {
        Field ff = ReflectionUtils.findField(instance.getClass(), fieldName);
        ReflectionUtils.makeAccessible(ff);
        ReflectionUtils.setField(ff, instance, value);
    }

    public static Object getPrivateFieldValue(String fieldName, Object instance) {
        Field ff = ReflectionUtils.findField(instance.getClass(), fieldName);
        ReflectionUtils.makeAccessible(ff);
        return ReflectionUtils.getField(ff, instance);
    }

    public static Map<String,Object> getProps(String operationName, String serviceName){
        Map<String,Object> props = new HashMap<>();
        props.put("ServiceName",serviceName);
        props.put("RqTm","2016-08-02T15:51:48.738+04:00");
        props.put("ServiceNamespace","srv://sbg.sbr/ucnServiceVersion001");
        props.put("OperationName",operationName);
        props.put("SCName","urn:sbrfsystems:99-ufs");
        props.put("RqUID","de23596088184793b228469b70583fb3");
        return props;
    }

}
