package com.example.Backend.Conroller;

import com.example.Backend.Exception.ResourceNotFoundException;
import com.example.Backend.Model.Householder;
import com.example.Backend.Services.Houseservice;
//import com.example.Backend.Services.houseservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

//class InvoiceSpecification {
//    public static Specification<Invoice> hasCustomerId(Long customerId) {
//        return (root, query, cb) -> cb.equal(root.get("customerId"), customerId);
//    }
//
//    public static Specification<Invoice> hasAmountBetween(BigDecimal min, BigDecimal max) {
//        return (root, query, cb) -> cb.between(root.get("amount"), min, max);
//    }
//
//    public static Specification<Invoice> hasDateBetween(LocalDate from, LocalDate to) {
//        return (root, query, cb) -> cb.between(root.get("invoiceDate"), from, to);
//    }
//
//    public static Specification<Invoice> hasDescriptionLike(String keyword) {
//        return (root, query, cb) -> cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
//    }
//}
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class HouseController {
    private Houseservice houseservice;

    @Autowired
    HouseController(Houseservice houseservice){
        this.houseservice = houseservice;
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> addHouseholder(@RequestBody Householder householder) {

        Householder saved = houseservice.saveHouseholder(householder);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteHouseholder(@PathVariable Long id) {
        houseservice.deleteHouseholder(id);
        return ResponseEntity.ok("Householder deleted successfully");
    }

    @PatchMapping("/admin/update/{id}")
    public ResponseEntity<Householder> patchHouseholder(@PathVariable Long id, @RequestBody Householder dto) {
        Householder updated = houseservice.patchHouseholder(id, dto);
        return ResponseEntity.ok(updated);
    }


    @GetMapping("/user/getlist")
    public ResponseEntity<Page<Householder>> getAllHouseholders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page index must be 0 or greater and size must be greater than 0");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Householder> result = houseservice.getHouseholders(pageable);
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>("Bad request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }




}
