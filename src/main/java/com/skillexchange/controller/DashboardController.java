package com.skillexchange.controller;

import com.skillexchange.dto.AdminDashboardDto;
import com.skillexchange.dto.UserDashboardDto;
import com.skillexchange.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for user and administrator dashboards.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/user")
    public ResponseEntity<UserDashboardDto> getUserDashboard(Authentication authentication) {
        UserDashboardDto dto = dashboardService.getUserDashboard(authentication.getName());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<AdminDashboardDto> getAdminDashboard() {
        AdminDashboardDto dto = dashboardService.getAdminDashboard();
        return ResponseEntity.ok(dto);
    }
}
