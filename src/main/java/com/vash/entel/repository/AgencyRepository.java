package com.vash.entel.repository;

import com.vash.entel.model.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Integer> {
    Optional<Agency> findBySeat(String seat);
    Optional<Agency> findBySeatAndCity(String seat, String city);
    Optional<Agency> findByCityAndSeat(String city, String seat);

}
