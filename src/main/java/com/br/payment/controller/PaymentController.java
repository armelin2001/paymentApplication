package com.br.payment.controller;

import com.br.payment.dto.PaymentDTO;
import com.br.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<List<PaymentDTO>> processPayment(
            @RequestParam String salerCode,
            @RequestBody List<PaymentDTO> payments
    ) {
        try{
            List<PaymentDTO> result = paymentService.processPayment(salerCode, payments);
            return ResponseEntity.ok(result);
        }catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Collections.emptyList());
        }
    }
}
