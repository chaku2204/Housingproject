package com.example.Backend.Conroller;
import com.example.Backend.Model.Invoice;
import com.example.Backend.Services.Invoiceservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

class InvoiceSpecification {
    public static Specification<Invoice> hasCustomerId(Long customerId) {
        return (root, query, cb) -> cb.equal(root.get("customerId"), customerId);
    }

    public static Specification<Invoice> hasAmountBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> cb.between(root.get("amount"), min, max);
    }

    public static Specification<Invoice> hasDateBetween(LocalDate from, LocalDate to) {
        return (root, query, cb) -> cb.between(root.get("invoiceDate"), from, to);
    }

    public static Specification<Invoice> hasDescriptionLike(String keyword) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
    }
}
@RestController
@RequestMapping("/api/invoice")
@CrossOrigin(origins = "http://localhost:4200")
public class InvoiceController {
    private Invoiceservice invoiceservice;

    @Autowired
    InvoiceController(Invoiceservice invoiceservice){
        this.invoiceservice = invoiceservice;
    }

    @GetMapping("/invoicelist")
    public ResponseEntity<?> getInvoices(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "invoiceDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try{
            Specification<Invoice> spec = Specification.where(null);

            Page<Invoice> data =  this.invoiceservice.getInvoicesPaginated(spec,page, size);
            return ResponseEntity.ok(data);
        }
        catch (Exception e){
            Map<String,Object> map = new HashMap<>();
            map.put("message","Internal Server error");
            return ResponseEntity.status(400).body(map);
        }


    }
    @PostMapping("/upload")
    public ResponseEntity<?> uploadcsvFile(@RequestParam("file") MultipartFile file){
        Map<String,Object> map = new HashMap<>();

        try {
            System.out.println(file);
            this.invoiceservice.saveInvoicesFromCsv(file);
            map.put("message", "Sucessfull Saved");
            return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(map);
        } catch (Exception e) {
            map.put("message", "File Not saved");
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body("File Not saved");
        }







    }

}
