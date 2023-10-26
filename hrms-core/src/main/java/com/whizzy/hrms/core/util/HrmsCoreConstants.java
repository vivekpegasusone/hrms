package com.whizzy.hrms.core.util;

import java.util.List;

public interface HrmsCoreConstants {

    String ID = "id";
    String EMPTY = "";
    String COMMA = ",";
    String COLLON = ":";
    String NONE = "none";
    String AT_THE_RATE = "@";
    String ACTIVE = "Active";
    String MASTER = "master";
    String UNKNOWN = "UnKnown";
    String LOGIN_ID = "loginId";
    String TENANT_ID = "tenantId";
    String IN_ACTIVE = "In-Active";
    String DEFAULT_TENANT = "drishti";
    String AUTHORITIES = "authorities";
    String AUTHORIZATION = "Authorization";
    String POOL_NAME_SUFFIX = "_DataSource";
    String LOGGED_IN_USER_CACHE = "LoggedInUserCache";
    String MASTER_PERSISTENT_UNIT = "master-persistence-unit";
    String TENANT_PERSISTENT_UNIT = "tenant-persistence-unit";

    List<String> AUTH_URL = List.of("/api/authentication");
}
