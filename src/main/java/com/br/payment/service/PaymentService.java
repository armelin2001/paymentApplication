package com.br.payment.service;

import com.br.payment.dto.PaymentDTO;
import com.br.payment.entity.Charge;
import com.br.payment.entity.Saler;
import com.br.payment.repository.ChargeRepository;
import com.br.payment.repository.SalerRepository;
import com.br.payment.utils.enums.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private SalerRepository salerRepository;

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private SqsService sqsService;

    public List<PaymentDTO> processPayment(String salerCode, List<PaymentDTO> payments) {
        Saler saler = salerRepository.findByCode(salerCode)
                .orElseThrow(() -> new RuntimeException("Saler not found"));

        for (PaymentDTO payment : payments) {
            Charge charge = chargeRepository.findByCode(payment.getCodeCharge())
                    .orElseThrow(() -> new RuntimeException("Charge not found"));

            BigDecimal valorOriginal = charge.getOriginalValue();
            BigDecimal valorPago = payment.getPaymentValue();

            PaymentStatus status;

            // need to change string values to url sqs values
            if (valorPago.compareTo(valorOriginal) < 0) {
                status = PaymentStatus.PARTIAL;
                sqsService.sendPayment(payment, "PARTIAL URL");
            } else if (valorPago.compareTo(valorOriginal) == 0) {
                status = PaymentStatus.TOTAL;
                sqsService.sendPayment(payment, "TOTAL URL");
            } else {
                status = PaymentStatus.EXCEEDS;
                sqsService.sendPayment(payment, "EXCEEDS URL");
            }

            payment.setStatus(status);
        }

        return payments;
    }
}
