package co.com.crediya.report.model.report.message;

import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
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
