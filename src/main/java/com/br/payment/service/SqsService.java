package com.br.payment.service;

import com.br.payment.dto.PaymentDTO;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SqsService {
    private final SqsClient sqsClient;

    public SqsService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void sendPayment(PaymentDTO payment, String queueUrl) {
        sendToQueue(queueUrl, payment);
    }

    private void sendToQueue(String queueUrl, PaymentDTO payment) {
        SendMessageRequest send_msg_request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(payment.toString())
                .build();

        sqsClient.sendMessage(send_msg_request);
    }
}
