package com.example.elektronski_karton_servis.Repository;

import com.example.elektronski_karton_servis.model.Dijagnoza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface DijagnozaRepository extends JpaRepository<Dijagnoza, Integer> {


}



