package com.whizzy.hrms.api.domain.auth;

import java.util.Objects;
import java.util.Set;

public class AuthDetail {
    private String securityToken;
    private String firstName;
    private String lastName;
    private String loginId;
    private Set<String> authorities;

    public AuthDetail() {
    }

    public AuthDetail(String securityToken, String firstName, String lastName, String loginId, Set<String> authorities) {
        this.securityToken = securityToken;
        this.firstName = firstName;
        this.lastName = lastName;
        this.loginId = loginId;
        this.authorities = authorities;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthDetail that = (AuthDetail) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(loginId, that.loginId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, loginId);
    }

    @Override
    public String toString() {
        return "AuthDetail{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", loginId='" + loginId + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}