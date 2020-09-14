package com.jay.blog.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jay.blog.cache.RedisCacheRemove;
import com.jay.blog.cache.RedisCache;
import com.jay.blog.cache.RedisHandler;
import com.jay.blog.utils.StringUtils;
import io.netty.util.internal.StringUtil;
import org.apache.catalina.LifecycleState;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Aspect
@Component
public class RedisAspect {
    Logger logger = LoggerFactory.getLogger(RedisAspect.class);

    @Autowired
    private RedisHandler redisHandler;

    @Pointcut("@annotation(com.jay.blog.cache.RedisCache)")
    void saveCache(){}

    @Pointcut("@annotation(com.jay.blog.cache.RedisCacheRemove) ")
    void removeCache(){}

    @Around(value = "saveCache()")
    public Object saveCache(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RedisCache annotation = method.getAnnotation(RedisCache.class);
        Object[] args = proceedingJoinPoint.getArgs();
        // 解析 SPEL 获取 key
        String key = parseKey(method, args, annotation.key(), annotation.cacheNames());
        // 获取缓存
        String cache = redisHandler.getCache(key);
        Object result = null;
        if (cache == null){
            logger.info("====== Redis 中不存在该缓存，从数据库中查找 ======");
            result = proceedingJoinPoint.proceed();
            if (result != null){
                long expire = annotation.expire();
                if (expire == -1){  // 不指定过期时间
                    redisHandler.saveCache(key, result);
                }else {     // 指定过期时间
                    redisHandler.saveCache(key, result, annotation.expire(), annotation.unit());
                }
            }
        }else{
            // 缓存中存在该数据
            result = deSerialize(method,cache);
        }

        return result;
    }

    @After(value = "removeCache()")
    public void remove(JoinPoint joinPoint){
        logger.info("======拦截 removeCache方法:{}.{} ======" ,
                 joinPoint.getSignature().getName());
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RedisCacheRemove annotation = method.getAnnotation(RedisCacheRemove.class);
        String key = parseKey(method, joinPoint.getArgs(), annotation.key(),annotation.cacheName());
        redisHandler.removeCache(key);
    }

    private String parseKey(Method method, Object[] args, String keySEL, String cacheName){
        // TODO
        if (StringUtils.checkIsEmpty(keySEL)){
            return cacheName + "::" + "-1";
        }
        logger.info("keySEL : " + keySEL);
        // 创建解析器
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression(keySEL);
        EvaluationContext context = new StandardEvaluationContext();
        // 添加参数
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        for (int i = 0; i < args.length; i ++){
            context.setVariable(parameterNames[i],args[i]);
            System.out.println("parameterName : " + i + " = " + parameterNames[i]+ ",value=" + args[i]);
        }
        // 返回解析的内容
        return cacheName+"::" + expression.getValue(context).toString();
    }


    private Object deSerialize(Method m, String cache) throws JsonProcessingException {
        Class returnTypeClass = m.getReturnType();
        Object result = null;
        // 方法返回值是 List<具体类> 的情况， 获取 List 中元素的具体类型
        // returnType 获取到的就是 List<具体的类型>
        Type returnType = m.getGenericReturnType();
        // 解决嵌套泛型反序列化
        if (returnType instanceof ParameterizedType){
            ParameterizedType type = (ParameterizedType)returnType;
            //System.out.println("type" + type);
            Type[] acturalTypesArgs = type.getActualTypeArguments();
            for ( Type acturalTypesArg : acturalTypesArgs){
                Class classArg = (Class) acturalTypesArg;
                //logger.info("===== 获取到泛型:{} ======", classArg.getName());
                result = JSON.parseArray(cache, classArg);
            }
        }else{
            result = JSON.parseObject(cache, returnTypeClass);
        }
        return result;
    }

    // TODO 一整页缓存
    private Object pageDeserialize(String cache){
        return null;
    }

}
