package co.com.crediya.report.sqs.listener.message;

import java.math.BigDecimal;

public record LoanMessage(

        String clientName,

        String email,

        String documentId,

        BigDecimal baseSalary,

        String status,

        String type,

        BigDecimal amount,

        BigDecimal term,

        BigDecimal monthlyDebt

) {}
