package com.bankslips.api.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.bankslips.api.entity.BankslipEntity;

public class PopulateDatabaseUtility {

	public static BankslipEntity newBankslip(String customer) {
		BankslipEntity bankslip = new BankslipEntity();
		Date date = new Date();
		bankslip.setCustomer(customer);
		bankslip.setDueDate(getDueDate(date));
		bankslip.setTotalInCents(new BigDecimal(Math.random() * 1000));
		
		return bankslip;
	}

	private static Date getDueDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 10);
		Date dueDate = c.getTime();

		return dueDate;
	}

}
