package com.br.payment;

import com.br.payment.dto.PaymentDTO;
import com.br.payment.entity.Charge;
import com.br.payment.entity.Saler;
import com.br.payment.repository.ChargeRepository;
import com.br.payment.repository.SalerRepository;
import com.br.payment.service.PaymentService;
import com.br.payment.service.SqsService;
import com.br.payment.utils.enums.PaymentStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class PaymentApplicationTests {
	@Autowired
	private PaymentService paymentService;

	@MockBean
	private SalerRepository salerRepository;

	@MockBean
	private ChargeRepository chargeRepository;

	@MockBean
	private SqsService sqsService;

	@Test
	public void testProcessPartialPayment() {
		Saler saler = new Saler();
		saler.setCode("V123");
		Mockito.when(salerRepository.findByCode("V123")).thenReturn(Optional.of(saler));

		Charge charge = new Charge();
		charge.setCode("C123");
		charge.setOriginalValue(BigDecimal.valueOf(100));
		Mockito.when(chargeRepository.findByCode("C123")).thenReturn(Optional.of(charge));

		PaymentDTO payment = new PaymentDTO();
		payment.setCodeCharge("C123");
		payment.setPaymentValue(BigDecimal.valueOf(50));

		List<PaymentDTO> payments = Collections.singletonList(payment);
		List<PaymentDTO> result = paymentService.processPayment("V123", payments);

		Assertions.assertEquals(PaymentStatus.PARTIAL, result.getFirst().getStatus());

		Mockito.verify(sqsService).sendPayment(payment, "PARTIAL URL");
	}

	@Test
	public void testProcessTotalPayment() {
		Saler saler = new Saler();
		saler.setCode("V123");
		Mockito.when(salerRepository.findByCode("V123")).thenReturn(Optional.of(saler));

		Charge charge = new Charge();
		charge.setCode("C123");
		charge.setOriginalValue(BigDecimal.valueOf(100));
		Mockito.when(chargeRepository.findByCode("C123")).thenReturn(Optional.of(charge));

		PaymentDTO payment = new PaymentDTO();
		payment.setCodeCharge("C123");
		payment.setPaymentValue(BigDecimal.valueOf(100));

		List<PaymentDTO> payments = Collections.singletonList(payment);
		List<PaymentDTO> result = paymentService.processPayment("V123", payments);

		Assertions.assertEquals(PaymentStatus.TOTAL, result.getFirst().getStatus());

		Mockito.verify(sqsService).sendPayment(payment, "TOTAL URL");
	}

	@Test
	public void testProcessExceedsPayment() {
		Saler saler = new Saler();
		saler.setCode("V123");
		Mockito.when(salerRepository.findByCode("V123")).thenReturn(Optional.of(saler));

		Charge charge = new Charge();
		charge.setCode("C123");
		charge.setOriginalValue(BigDecimal.valueOf(100));
		Mockito.when(chargeRepository.findByCode("C123")).thenReturn(Optional.of(charge));

		PaymentDTO payment = new PaymentDTO();
		payment.setCodeCharge("C123");
		payment.setPaymentValue(BigDecimal.valueOf(150));

		List<PaymentDTO> payments = Collections.singletonList(payment);
		List<PaymentDTO> result = paymentService.processPayment("V123", payments);

		Assertions.assertEquals(PaymentStatus.EXCEEDS, result.getFirst().getStatus());

		Mockito.verify(sqsService).sendPayment(payment, "EXCEEDS URL");
	}
}
