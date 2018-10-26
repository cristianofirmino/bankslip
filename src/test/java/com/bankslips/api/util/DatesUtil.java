package com.bankslips.api.util;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;

public class DatesUtil {

	public static Date plusDays(Date date, int days) {
		return Date.from((date.toInstant().atOffset(ZoneOffset.UTC).plusDays(days)).toInstant());
	}

	public static String simpleFormaDate(Date date) {
		SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");
		return pattern.format(date);
	}
}
