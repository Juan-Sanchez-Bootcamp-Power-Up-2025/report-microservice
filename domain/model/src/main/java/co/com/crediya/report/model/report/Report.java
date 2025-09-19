package co.com.crediya.report.model.report;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Report {

    String metricKey;

    Long approvedLoansCount;

    BigDecimal approvedLoansAmount;

}
