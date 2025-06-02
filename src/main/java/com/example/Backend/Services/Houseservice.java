package com.example.Backend.Services;

import com.example.Backend.Exception.DuplicateFieldException;
import com.example.Backend.Exception.ResourceNotFoundException;
import com.example.Backend.Model.Householder;
import com.example.Backend.Repository.HouseRepository;

import com.example.Backend.utiliti.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class Houseservice {

    private HouseRepository houseRepository;

    private MailService mailService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    Houseservice(HouseRepository houseRepository, MailService mailService,PasswordEncoder passwordEncoder){
        this.houseRepository = houseRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    public Householder saveHouseholder(Householder householder) {
        if (houseRepository.findByMobilno(householder.getMobilno()).isPresent()) {
            throw new DuplicateFieldException("Mobile number already exists");
        }

        if (houseRepository.findByEmail(householder.getEmail()).isPresent()) {
            throw new DuplicateFieldException("Email already exists");
        }

        String rawPassword = PasswordGenerator.generate();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        householder.setPassword(encodedPassword);
        System.out.println(householder);


        String fullName = householder.getFirst_name() + " " + householder.getLast_name();
        Thread mailservice = new Thread(()->{ mailService.sendPasswordEmail(householder.getEmail(), fullName, rawPassword);});
        mailservice.start();
//        mailService.sendPasswordEmail(householder.getEmail(), fullName, rawPassword);


        return houseRepository.save(householder);
    }

    public Page<Householder> getHouseholders(Pageable pageable) {
        return houseRepository.findAll(pageable);
    }
    public void deleteHouseholder(Long id) {
        Householder existing = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Householder not found with ID: " + id));
        houseRepository.delete(existing);
    }

    public Householder patchHouseholder(Long id, Householder dto) {
        Householder householder = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Householder not found with id: " + id));

        if (dto.getHouseNo() != null) householder.setHouseNo(dto.getHouseNo());
        if (dto.getFirst_name() != null) householder.setFirst_name(dto.getFirst_name());
        if (dto.getLast_name() != null) householder.setLast_name(dto.getLast_name());
        if (dto.getProfasion() != null) householder.setProfasion(dto.getProfasion());

        if (dto.getMobilno() != null) {
            houseRepository.findByMobilno(dto.getMobilno()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new DuplicateFieldException("Mobile number already exists");
                }
            });
            householder.setMobilno(dto.getMobilno());
        }

        if (dto.getEmail() != null) {
            houseRepository.findByEmail(dto.getEmail()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new DuplicateFieldException("Email already exists");
                }
            });
            householder.setEmail(dto.getEmail());
        }

        return houseRepository.save(householder);
    }



}
