package com.cy.pj.common.aspect;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.util.IPUtils;
import com.cy.pj.sys.dao.SysLogDao;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.entity.SysUser;
/**
 * @Aspect 注解修饰的类通常认为一个切面对象类型
 * 切面对象是对扩展业务的封装,它通常会在内部声明
 * 如下几个部分.
 * 1)实现扩展业务的方法(一般会称为通知-advice)
 * 2)切入扩展业务的点(一般会称为切入点-PointCut)
 */
//@Order(1)
@Aspect
@Service
public class SysLogAspect {//日志切面
	  /**
	   * @Around注解修饰方法为一个环绕通知,其目的
	   * 是在目标业务方法执行之前和之后都可以进行
	   * 扩展业务的处理
	   * 其中:
	   * 1)bean(sysUserServiceImpl) 为切入点表达式,
	   * 表示sysUserServiceImpl对象中所有业务方法执行
	   * 时都会执行@Around注解修饰的方法
	   * @param jp 连接点(封装了要执行的目标方法信息)
	   * @return
	   * @throws Throwable
	   */
	  //@Around("bean(sysUserServiceImpl)")
	  //@Around("bean(*ServiceImpl)")
	  //@annotation()为细粒度的切入点表达式定义方式
	  @Around("@annotation(com.cy.pj.common.annotation.RequiredLog)")
	  public Object aroundMethod(ProceedingJoinPoint jp) 
	      throws Throwable{
		  System.out.println("LogAspect:开始记录日志");
		  //1.目标业务执行之前的记录
		  long t1=System.currentTimeMillis();
		  //2.执行目标业务(底层通过反射执行目标方法)
		  Object result=jp.proceed();
		  //3.目标业务执行之后的记录
		  long t2=System.currentTimeMillis();
		  System.out.println("目标业务执行时长:"+(t2-t1));
		  saveObject(jp,(t2-t1));
		  //4.返回目标业务的执行结果
		  return result;
	  }
	  @Autowired
	  private SysLogDao sysLogDao;
	  private void saveObject(ProceedingJoinPoint jp,long time)throws Exception {
		//1.获取要保存的日志信息
		//1.1获取登陆用户(没问题)
		SysUser user=(SysUser)SecurityUtils.getSubject().getPrincipal();
		//1.2获取方法签名(此对象封装了我们要执行的目标方法信息)
		Signature s=jp.getSignature();
		System.out.println(s.getClass().getName());//MethodSignature
		MethodSignature ms=(MethodSignature)s;
		//1.2.1获取目标对象(要执行的业务层对象)
		Class<?> targetClass=jp.getTarget().getClass();
		//1.2.2基于目标业务对象获取要执行的目标方法
		//?思考(为什么要获取此方法呢)
		Method targetMethod=targetClass.getDeclaredMethod(
				    ms.getName(),
				    ms.getParameterTypes());
		//1.2.3获取方法上定义的注解内容(定义的操作名)
		RequiredLog requiredLog=
		targetMethod.getDeclaredAnnotation(RequiredLog.class);
		String operation=requiredLog.value();
		//1.2.4获取目标对象方法的全称(类全名+方法名)
		String targetClassName=targetClass.getName();
		String targetMethodName=targetClassName+"."+targetMethod.getName();
		//1.3获取方法执行时的实际参数
		String params=Arrays.toString(jp.getArgs());
		//2.封装日志信息
		SysLog log=new SysLog();
		log.setUsername(user.getUsername());
		log.setIp(IPUtils.getIpAddr());
		log.setOperation(operation);
		log.setMethod(targetMethodName);
		log.setParams(params);
		log.setTime(time);
		log.setCreatedTime(new Date());
		//3.将日志信息写入到数据库
		sysLogDao.insertObject(log);
	  }
	
}







