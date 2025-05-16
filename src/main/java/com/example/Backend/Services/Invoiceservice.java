package com.example.Backend.Services;

import com.example.Backend.Model.Invoice;
import com.example.Backend.Repository.InvoiceRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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
public class Invoiceservice {

    private InvoiceRepository invoiceRepository;

    @Autowired
    Invoiceservice(InvoiceRepository invoiceRepository){
        this.invoiceRepository = invoiceRepository;
    }

    public Page<Invoice> getInvoicesPaginated(Specification<Invoice> spec, int page, int size) {


        Pageable pageable = PageRequest.of(page, size);
        return invoiceRepository.findAll(spec,pageable);
    }

    public void saveInvoicesFromCsv(MultipartFile file) throws Exception {
        try  {
            CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));

            String[] header = reader.readNext();
            if (header == null) throw new RuntimeException("Empty CSV file");
            System.out.println(header);

            Map<String, Integer> columnIndex = new HashMap<>();
            for (int i = 0; i < header.length; i++) {
                String normalized = header[i].trim().toLowerCase();
                columnIndex.put(normalized, i);
            }
            System.out.println(columnIndex);


            String[] requiredHeaders = {"customer id", "invoice number", "invoice date", "amount", "description"};
            for (String required : requiredHeaders) {
                if (!columnIndex.containsKey(required)) {
                    throw new RuntimeException("Missing required header: " + required);
                }
            }
            System.out.println(columnIndex);
            String[] row;
            List<Invoice> invoiceList = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            while ((row = reader.readNext()) != null) {
                Long customerId = Long.parseLong(row[columnIndex.get("customer id")].trim());
                Long invoiceNumber = Long.parseLong(row[columnIndex.get("invoice number")].trim());

                Invoice invoice = invoiceRepository
                        .findByCustomerIdAndInvoiceNumber(customerId, invoiceNumber)
                        .orElse(new Invoice());
                System.out.println(row[0]+ " "+ row[1]+" "+row[2]+" " +row[3]+" "+row[4]);
                invoice.setCustomerId(Long.parseLong(row[columnIndex.get("customer id")].trim()));
                invoice.setInvoiceNumber(Long.parseLong(row[columnIndex.get("invoice number")].trim()));
                invoice.setInvoiceDate(LocalDate.parse(row[columnIndex.get("invoice date")].trim(), formatter));
                invoice.setAmount(new BigDecimal(row[columnIndex.get("amount")].trim()));
                invoice.setDescription(String.valueOf(row[columnIndex.get("description")]).trim());
                invoiceList.add(invoice);
                this.invoiceRepository.save(invoice);

            }
//            this.invoiceRepository.saveAll(invoiceList);


        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
