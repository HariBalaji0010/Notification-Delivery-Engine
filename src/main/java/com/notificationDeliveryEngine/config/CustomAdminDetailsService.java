package com.notificationDeliveryEngine.config;


import com.notificationDeliveryEngine.entity.Admin;
import com.notificationDeliveryEngine.repository.AdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomAdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public CustomAdminDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByAdminUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(admin.getAdminUserName())
                .password(admin.getAdminPassword())
                .authorities("ADMIN")
                .build();
    }
}