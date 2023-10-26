package com.whizzy.hrms.core.util;

import com.whizzy.hrms.core.filter.JwtValidatorFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtil {
    private static final Logger LOG = LoggerFactory.getLogger(JwtValidatorFilter.class);

    public static void logStackTrace(Exception exception) {
        StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));
        LOG.error("StackTrace : {}", writer);
    }
}
