package com.whizzy.hrms.core.util;

import java.util.Objects;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.AT_THE_RATE;

public class HrmsCoreUtil {

    public static String findDomainNameFromEmail(String emailId) {
        String domainName = null;
        if(Objects.nonNull(emailId)) {
            String arrEmail[] = emailId.split(AT_THE_RATE);
            if (arrEmail.length == 2) {
                domainName = arrEmail[1];
            }
        }
        return domainName;
    }
}
