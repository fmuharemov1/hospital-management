package ba.unsa.etf.hospital.controller;


import ba.unsa.etf.hospital.exception.RoleNotFoundException;
import ba.unsa.etf.hospital.model.Role;
import ba.unsa.etf.hospital.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);}
    @PostMapping
    public Role createRole(@RequestBody Role role) {return roleService.saveRole(role);}

    @GetMapping("/{id}")
    public Role one(@PathVariable Long id){
        return roleService.findById(id)
                .orElseThrow(()->new RoleNotFoundException(id));
    }

    @PutMapping("/{id}")
    public Role replaceRole(@RequestBody Role newRole, @PathVariable Long id){
        return roleService.findById(id)
                .map(role -> {
                    role.setTipKorisnika(newRole.getTipKorisnika());
                    role.setSmjena(newRole.getSmjena());
                    role.setId(newRole.getId());
                    role.setOdjeljenje(newRole.getOdjeljenje());
                    return roleService.saveRole(role);
                })
                .orElseGet(() -> {
                    return roleService.saveRole(newRole);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id){
        roleService.deleteById(id);
    }
}
