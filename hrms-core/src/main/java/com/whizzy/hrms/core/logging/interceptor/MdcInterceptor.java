package com.whizzy.hrms.core.logging.interceptor;

import com.whizzy.hrms.core.tenant.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;


import java.util.Objects;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.*;
import static com.whizzy.hrms.core.util.SecurityUtil.getCurrentUserLoginId;

@Component
public class MdcInterceptor implements WebRequestInterceptor {

    @Override
    public void preHandle(WebRequest request) {
        if(Objects.nonNull(TenantContext.getTenantId())) {
            MDC.put(TENANT_ID, TENANT_ID + COLLON + TenantContext.getTenantId());
        }

        if(Objects.nonNull(getCurrentUserLoginId())) {
            MDC.put(LOGIN_ID, LOGIN_ID + COLLON + getCurrentUserLoginId());
        }
    }

    @Override
    public void postHandle(@NonNull WebRequest request, ModelMap model) {
        MDC.remove(TENANT_ID);
        MDC.remove(LOGIN_ID);
    }

    @Override
    public void afterCompletion(@NonNull WebRequest request, Exception ex) {
        // NOOP
    }
}
