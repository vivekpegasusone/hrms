package com.whizzy.hrms.core.tenant.domain;

import java.util.HashSet;
import java.util.Set;

import static com.whizzy.hrms.core.util.HrmsCoreConstants.ACTIVE;
import static com.whizzy.hrms.core.util.HrmsCoreConstants.IN_ACTIVE;

public class UserWithAuthorities {
    private final String firstName;
    private final String lastName;
    private final String loginId;
    private final String password;
    private final String status;
    private final Set<String> authorities = new HashSet<>();

    public UserWithAuthorities(String firstName, String lastName, String loginId, String password, Boolean status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.loginId = loginId;
        this.password = password;
        this.status = status == true ? ACTIVE : IN_ACTIVE;
    }

    public boolean addAuthority(String authority) {
        return authorities.add(authority);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "UserWithAuthorities{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", loginId='" + loginId + '\'' +
                ", status=" + status +
                ", authorities=" + authorities +
                '}';
    }
}
