package com.example.elektronski_karton_servis.Repository;

import com.example.elektronski_karton_servis.model.Terapija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface TerapijaRepository extends JpaRepository<Terapija, Integer> {


}





