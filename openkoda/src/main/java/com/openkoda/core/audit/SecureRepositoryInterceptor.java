/*
MIT License

Copyright (c) 2016-2024, Openkoda CDX Sp. z o.o. Sp. K. <openkoda.com>

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice
shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.openkoda.core.audit;

import com.openkoda.controller.ComponentProvider;
import com.openkoda.core.service.event.EntityApplicationEvent;
import com.openkoda.repository.SecureRepositoryWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * An interceptor used when performing persistance operation that uses (primarly Dynamic Entities) .SecureRepositoryWrapper
 * 
 */
@Aspect
@Component
public class SecureRepositoryInterceptor extends ComponentProvider {

    @Pointcut("execution(public * com.openkoda.repository.SecureRepositoryWrapper+.*(..)) && @annotation(com.openkoda.core.audit.AuditableEntityOperation)")
    public void pointcut() {
        
    }
    
    /**
     * Annotated method will be measured for it's cpu performance
     */
    @Around("pointcut()")
    public Object getterBefore(ProceedingJoinPoint joinPoint) throws Throwable {       
        trace("[getterBefore] Executing Before method [{}]", joinPoint.getSignature());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SecureRepositoryWrapper<?> secureRepositoryWrapper = (SecureRepositoryWrapper<?>)joinPoint.getTarget();
        AuditableEntityOperation annotation = (AuditableEntityOperation)signature.getMethod().getAnnotationsByType(AuditableEntityOperation.class)[0];
        String category = annotation.category();
        Object arg = joinPoint.getArgs()[0];
        
        List<Object> objects = new ArrayList<>();
        // 1. determine if single 
        boolean isList = false;
        boolean returnsObject = false;
        if(arg instanceof List) {
            isList = true;
        }
        
        if(!signature.getReturnType().isPrimitive()) {
            returnsObject = true;
        }
        
        // 2. determining if entity or id
        if(EntityApplicationEvent.DELETED_CATEGORY.equals(category)) {
            if(!isList) {
                objects.add(secureRepositoryWrapper.findOne(joinPoint.getArgs()[0]));
            } else {
                objects.addAll(secureRepositoryWrapper.findAllById(joinPoint.getArgs()[0]));
            }
        } else {
           Long id = ((com.openkoda.model.common.OpenkodaEntity)joinPoint.getArgs()[0]).getId();
           if(id == null || id == 0) {
               category = EntityApplicationEvent.CREATED_CATEGORY;
           } else {
               category = EntityApplicationEvent.MODIFIED_CATEGORY;
           }
        }

        Object result = joinPoint.proceed();
        
        if(!EntityApplicationEvent.DELETED_CATEGORY.equals(category) && isList) {
            objects.addAll((List)result);
        } else if(!EntityApplicationEvent.DELETED_CATEGORY.equals(category)){
            objects.add(result);
        }
        
        trace("[getterBefore] Executing After method [{}]", joinPoint.getSignature());
        if(EntityApplicationEvent.DELETED_CATEGORY.equals(category)) {
            objects.forEach( o -> services.customEventService.onDelete(o));
        } else if (EntityApplicationEvent.CREATED_CATEGORY.equals(category)){
            objects.forEach( o -> services.customEventService.onSave(o));
        } else if (EntityApplicationEvent.MODIFIED_CATEGORY.equals(category)) {
            objects.forEach( o -> services.customEventService.onUpdate(o));
        }
        
        debug("[getterBefore] {}", "");
        return result;
    }
    
}
