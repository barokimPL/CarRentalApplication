package pl.sda.carrental.service;

import org.springframework.stereotype.Service;
import pl.sda.carrental.model.repository.userRepositories.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    
}
