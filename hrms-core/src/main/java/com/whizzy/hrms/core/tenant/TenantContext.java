package com.whizzy.hrms.core.tenant;

/*
* This class acts as holder for the current tenant.
*/
public class TenantContext {
    private static final ThreadLocal<String> TENANT = new InheritableThreadLocal<>();

    public static String getTenantId() {
        return TENANT.get();
    }

    public static void setTenantId(String tenant) {
        TENANT.set(tenant);
    }

    public static void clear() {
        TENANT.remove();
    }
}
