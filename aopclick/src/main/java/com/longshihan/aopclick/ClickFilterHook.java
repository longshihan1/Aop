package com.longshihan.aopclick;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by LONGHE001.
 *
 * @time 2018/12/29 0029
 * @des
 * @function
 */
@Aspect
public class ClickFilterHook {
    private static Object currentObject = null;
    //进行类似于正则表达式的匹配，被匹配到的方法都会被截获
    ////截获任何包中以类名以Activity、Layout结尾，并且该目标类和当前类是一个Object的对象的所有方法
    private static final String POINTCUT_METHOD =
            "(execution(* *..Activity+.*(..)) ||execution(* *..Layout+.*(..))) && target(Object) && this(Object)";
    //精确截获MyFrameLayou的onMeasure方法
    private static final String POINTCUT_CALL = "call(* org.android10.viewgroupperformance.component.MyFrameLayout.onMeasure(..))";

    private static final String POINTCUT_METHOD_MAINACTIVITY = "execution(* *..MainActivity+.onCreate(..))";

    //切点，ajc会将切点对应的Advise编织入目标程序当中
    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotated() {
    }

    @Pointcut(POINTCUT_METHOD_MAINACTIVITY)
    public void methodAnootatedWith() {
    }

    @Before("methodAnootatedWith()")
    public void onCreateBefore(JoinPoint joinPoint) throws Throwable{
        Log.e("onCreateAfter:", "onCreate is start .");
    }

    /**
     * 在截获的目标方法调用返回之后（无论正常还是异常）执行该Advise
     *
     * @param joinPoint
     * @throws Throwable
     */
    @After("methodAnootatedWith()")
    public void onCreateAfter(JoinPoint joinPoint) throws Throwable {
        Log.e("onCreateAfter:", "onCreate is end .");

    }

    /**
     * 在截获的目标方法体开始执行时（刚进入该方法实体时）调用
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("methodAnnotated()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.e("onCreateAfter:", "weaveJoinPoint is end .");
        if (currentObject == null) {
            currentObject = joinPoint.getTarget();
        }
        //调用原方法的执行。
        Object result = joinPoint.proceed();

        return result;
    }
}
