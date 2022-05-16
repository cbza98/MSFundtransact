package com.bankntt.MSFundtransact.application.helpers;

import java.util.Random;

public class AccountGeneratorValues {
	public static String NumberGenerate(String _accountType) {
		String start = _accountType;
		Random value = new Random();

		// Generar dos valores en base a los tipos de cuenta
		int v1 = value.nextInt(10);
		int v2 = value.nextInt(10);
		start += Integer.toString(v1) + Integer.toString(v2) + " ";

		int count = 0;
		int n = 0;
		for (int i = 0; i < 12; i++) {
			if (count == 4) {
				start += " ";
				count = 0;
			} else
				n = value.nextInt(10);
			start += Integer.toString(n);
			count++;

		}
		return start.replaceAll(" ", "");
	}

	public static String IdentityGenerate(String BP, String Account) {
		return (new StringBuilder()).append(BP).append(Account).toString();

	}

}
