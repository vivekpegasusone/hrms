package com.whizzy.hrms.core.domain.dto;

import java.util.HashSet;
import java.util.Set;

public class UserWithAuthorities {
    private String firstName;
    private String lastName;
    private String loginId;
    private String password;
    private Boolean active;
    private Set<String> authorities = new HashSet<>();

    public UserWithAuthorities() {
    }

    public UserWithAuthorities(String firstName, String lastName, String loginId, String password, Boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.loginId = loginId;
        this.password = password;
        this.active = active;
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

    public Boolean getActive() {
        return active;
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
                ", active=" + active +
                ", authorities=" + authorities +
                '}';
    }
}
