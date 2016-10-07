package freedom.common.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import freedom.common.kit.StrKit;
import freedom.common.validate.Length;
import freedom.common.validate.Mobile;
import freedom.common.validate.NotBlank;
import freedom.common.validate.NumberRange;
import freedom.common.validate.ParamVerify;
import freedom.common.validate.ParameterException;

@Component
@Aspect
public class TimeAspect {

	public TimeAspect(){
		System.out.println("init costTime");
	}
	
	@Pointcut("execution (* com.iquizoo.server.web.service..*Impl.*(..))")
	public void costTime(){}
	
	@Around("costTime()")
	public Object round(JoinPoint joinPoint)throws Throwable
	{
		ProceedingJoinPoint processdingPoint = (ProceedingJoinPoint) joinPoint;
		Object target = processdingPoint.getTarget();
		Object self   = processdingPoint.getThis();
		String kind = processdingPoint.getKind();
		Signature signature =  processdingPoint.getSignature();
		String name = signature.getName();
		Object[] args = processdingPoint.getArgs();
		for (Object object : args) 
		{
			Class<?> paramType = object.getClass();
			ParamVerify verify = paramType.getAnnotation(ParamVerify.class);
			if(null != verify)
			{
				verify(object,paramType);
			}
		}
		StaticPart staticPart = processdingPoint.getStaticPart();
		long start = System.currentTimeMillis();
		Object value = processdingPoint.proceed();
		long end = System.currentTimeMillis();
		System.out.println(target.getClass().getName() +"." + name +" : " + (end - start));
		return value;
	}
	
	private void verify(Object obj,Class<?> clazz)throws Exception
	{
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) 
		{
			field.setAccessible(true);
			String fieldName = field.getName();
			Class<?> fieldType = field.getType();
			Annotation[] annotations = field.getAnnotations();
			Object entity = field.get(obj);
			if(annotations.length > 0)
			{
				for (Annotation verifyAnnotation : annotations) 
				{
					Class<?> annotationType = verifyAnnotation.annotationType();
					if(annotationType == NotBlank.class)
					{
						verifyNotBlank(entity, field,fieldName, fieldType);
					}
					else if(annotationType == NumberRange.class)
					{
						verifyNumberRange(entity, field,fieldName, fieldType,verifyAnnotation);
					}
					else if(annotationType == Length.class)
					{
						verifyStringLength(entity, field,fieldName, fieldType,verifyAnnotation);
					}
					else if(annotationType == Mobile.class)
					{
						verifyMobile(entity, field, fieldName,fieldType);
					}
				}
			}
		}
	}
	
	private void verifyMobile(Object entity, Field field,
			String fieldName, Class<?> fieldType)
			throws IllegalAccessException, ParameterException {
		if(fieldType == String.class)
		{
			String value = (String)entity;
			if(StrKit.isBlank(value))
				throw new ParameterException(fieldName + " must not null");
			if(!StrKit.isMobile(value))
				throw new ParameterException(fieldName + " must be Mobile phone format");
		}
	}

	private void verifyNotBlank(Object entity, Field field,
			String fieldName, Class<?> fieldType)
			throws IllegalAccessException, ParameterException {
		if(fieldType == String.class)
		{
			if(StrKit.isBlank((String)entity))
				throw new ParameterException(fieldName + " must not null");
		}
	}

	private void verifyNumberRange(Object entity, Field field,
			String fieldName, Class<?> fieldType,
			Annotation verifyAnnotation) throws IllegalAccessException,
			ParameterException {
		if(
		   fieldType == int.class   || fieldType == Integer.class || 
		   fieldType == long.class  || fieldType == Long.class || 
		   fieldType == float.class || fieldType == Float.class ||
		   fieldType == double.class|| fieldType == Double.class || 
		   fieldType == short.class || fieldType == Short.class || 
		   fieldType == byte.class  || fieldType == Byte.class)
		{
			if(null == entity)
				throw new ParameterException(fieldName + " must not null");
			else
			{
				long value = Long.parseLong(String.valueOf(entity));
				NumberRange numberRange = (NumberRange) verifyAnnotation;
				if(value < numberRange.min() || value > numberRange.max())
				{
					throw new ParameterException(fieldName + " must " + numberRange.min() + " <= ? <= " + numberRange.max());
				}
			}
		}
	}
	
	private void verifyStringLength(Object entity, Field field,
			String fieldName, Class<?> fieldType,
			Annotation verifyAnnotation) throws IllegalAccessException,
			ParameterException {
		if(fieldType == String.class)
		{
			String value = (String)entity;
			if(StrKit.isBlank(value))
				throw new ParameterException(fieldName + " must not null");
			else
			{
				int valueLength = value.length();
				Length length = (Length) verifyAnnotation;
				if(valueLength < length.min() || valueLength > length.max())
				{
					throw new ParameterException(fieldName + " length must >= " + length.min() + " and <= " + length.max());
				}
			}
		}
	}
}
