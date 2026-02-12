package org.resume.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

    /*
    stripe listen --forward-to localhost:8080/api/webhooks/stripe

    pm_card_visa
    pm_card_chargeDeclined

     */

}
