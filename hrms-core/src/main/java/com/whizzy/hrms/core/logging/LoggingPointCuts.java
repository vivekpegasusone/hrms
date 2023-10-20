package com.whizzy.hrms.core.logging;

import org.aspectj.lang.annotation.Pointcut;

public class LoggingPointCuts {

    @Pointcut("@annotation(com.whizzy.hrms.core.logging.Loggable)")
    public void pointcutBasedLoggableAnnotation() {
    }

    @Pointcut("within(com.whizzy.hrms.core.filter..*)")
    public void nonLoggablePackage() {}

    @Pointcut("within(com.whizzy.hrms..*) && !nonLoggablePackage())")
    public void pointcutBasedOnPackage() {}

    @Pointcut("""            
            @within(org.springframework.web.bind.annotation.RestController)
            || @within(org.springframework.stereotype.Service)
            || @within(org.springframework.stereotype.Repository)
            || @within(org.springframework.stereotype.Component)
    """)
    public void pointcutBasedOnAnnotation() {}

    //Pointcut based on packages
    @Pointcut("pointcutBasedOnAnnotation() && !nonLoggablePackage()")
    public void pointcutOnAnnotationAndNonLoggable() {}
}
