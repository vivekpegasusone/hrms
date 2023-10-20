package com.whizzy.hrms.core.master.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;


import java.io.Serializable;
import java.util.Objects;

@Entity
@Table (name = "tenant_config")
public class Tenant implements Serializable {

    private static final long serialVersionUID = 3210275181684883952L;

    @Id
    private long id;

    @NotNull
    @Column(name = "schema_name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "domain", nullable = false)
    private String domain;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "driver_class", nullable = false)
    private String driverClass;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "maximumpoolsize", nullable = false)
    private Integer maxPoolSize;

    @NotNull
    @Column(name = "minimumidle", nullable = false)
    private Integer minIdle;

    @NotNull
    @Column(name = "maxlifetime", nullable = false)
    private Integer maxLifeTime;

    @NotNull
    @Column(name = "keepalivetime", nullable = false)
    private Integer keepAliveTime;

    @NotNull
    @Column(name = "connectiontimeout", nullable = false)
    private Integer connectionTimeout;


    public Tenant() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxLifeTime() {
        return maxLifeTime;
    }

    public void setMaxLifeTime(Integer maxLifeTime) {
        this.maxLifeTime = maxLifeTime;
    }

    public Integer getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Integer keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tenant tenant = (Tenant) o;
        return Objects.equals(name, tenant.name) && Objects.equals(url, tenant.url) && Objects.equals(username, tenant.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, domain, url);
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}
