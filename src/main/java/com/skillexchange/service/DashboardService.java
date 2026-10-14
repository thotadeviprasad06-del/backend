package com.skillexchange.service;

import com.skillexchange.dto.AdminDashboardDto;
import com.skillexchange.dto.UserDashboardDto;

/**
 * Service interface for aggregate user and admin dashboard metrics.
 */
public interface DashboardService {

    UserDashboardDto getUserDashboard(String email);

    AdminDashboardDto getAdminDashboard();
}
