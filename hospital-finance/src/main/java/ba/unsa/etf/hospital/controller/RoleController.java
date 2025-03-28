package ba.unsa.etf.hospital.controller;


import ba.unsa.etf.hospital.model.Role;
import ba.unsa.etf.hospital.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/role")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @GetMapping
    public List<Role> getAllRoles() {return roleService.getAllRoles();}
    @PostMapping
    public Role createRole(@RequestBody Role role) {return roleService.saveRole(role);}
}
