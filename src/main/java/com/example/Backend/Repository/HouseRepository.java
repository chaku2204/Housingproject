package com.example.Backend.Repository;

import com.example.Backend.Model.Householder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<Householder,Long> {
    Optional<Householder> findByMobilno(String mobilno);
    Optional<Householder> findByEmail(String email);
    Page<Householder>  findAll(Specification spec, Pageable pageable);

}
