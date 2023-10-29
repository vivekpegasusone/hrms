package com.whizzy.hrms.core.tenant.domain.mapper;

import com.whizzy.hrms.core.tenant.domain.dto.AuthDetail;
import com.whizzy.hrms.core.tenant.domain.entity.UserAuthorities;

public class UserAuthoritiesToAuthDetail {
    public static AuthDetail mapToAuthDetail(UserAuthorities userAuth) {
        return new AuthDetail(userAuth.getFirstName(), userAuth.getLastName(),
                userAuth.getLoginId(), userAuth.getAuthorities());
    }
}
