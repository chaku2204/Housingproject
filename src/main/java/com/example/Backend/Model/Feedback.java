package com.example.Backend.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private int rating; // optional
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "householder_id", nullable = false)
    @JsonIgnore
    private Householder householder;

    private LocalDateTime submittedAt = LocalDateTime.now();

}
