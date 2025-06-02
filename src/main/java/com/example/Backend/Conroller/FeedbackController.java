package com.example.Backend.Conroller;

import com.example.Backend.Model.Feedback;
import com.example.Backend.Model.Householder;
import com.example.Backend.Repository.FeedbackRepository;
import com.example.Backend.Repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class FeedbackController {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @PostMapping("feedback/{id}")
    public ResponseEntity<?> submitFeedback(@PathVariable Long id,
                                            @RequestBody Feedback feedbackRequest) {
        Householder householder = houseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Feedback feedback = feedbackRequest;
        feedback.setHouseholder(householder);
        feedbackRepository.save(feedback);

        return ResponseEntity.ok("Feedback submitted successfully");
    }

    @GetMapping("feedback/{id}")
    public ResponseEntity<?> getfeedback(@PathVariable Long id){

          Householder householder = houseRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
          List<Feedback> feedbackList = householder.getFeedbackList();
          return ResponseEntity.ok(feedbackList);
    }
}
