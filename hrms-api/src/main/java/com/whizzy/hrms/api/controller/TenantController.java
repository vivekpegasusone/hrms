package com.whizzy.hrms.api.controller;

import com.whizzy.hrms.core.master.domain.Tenant;
import com.whizzy.hrms.core.master.service.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping (produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class TenantController {
    private static final Logger LOG = LoggerFactory.getLogger(TenantController.class);

    @Autowired
    private TenantService tenantService;

    @GetMapping(value = "/api/tenants")
    public List<Tenant> getTenants() {
        return tenantService.findAll();
    }
}
