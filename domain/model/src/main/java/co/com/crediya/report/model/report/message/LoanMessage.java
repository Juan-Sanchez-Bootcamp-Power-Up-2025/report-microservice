package co.com.crediya.report.model.report.message;

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
