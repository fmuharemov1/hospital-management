package com.example.elektronski_karton_servis.Repository;

import com.example.elektronski_karton_servis.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {


}




