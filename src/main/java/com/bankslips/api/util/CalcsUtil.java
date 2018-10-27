package com.bankslips.api.util;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

public class CalcsUtil {

	public static Optional<BigDecimal> calcFine(Date dueDate, BigDecimal totalInCents) {

		BigDecimal fine = null;
		Date today = new Date();
		long days = ChronoUnit.DAYS.between(dueDate.toInstant(), today.toInstant());

		if (days > 10) {
			fine = (totalInCents.multiply(BigDecimal.valueOf(0.01))).multiply(BigDecimal.valueOf(days));
		}

		if (days > 0 && days <= 10) {
			fine = (totalInCents.multiply(BigDecimal.valueOf(0.005))).multiply(BigDecimal.valueOf(days));
		}

		return Optional.ofNullable(fine);
	}
}