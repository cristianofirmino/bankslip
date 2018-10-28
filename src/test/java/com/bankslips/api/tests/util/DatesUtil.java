package com.bankslips.api.tests.util;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Utily Test Class for operations with Date 
 * @author Cristiano Firmino
 *
 */
public final class DatesUtil {

	public static Date plusDays(Date date, int days) {
		return Date.from((date.toInstant().atOffset(ZoneOffset.UTC).plusDays(days)).toInstant());
	}

	public static String simpleDate(Date date) {
		SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");
		return pattern.format(date);
	}
}
