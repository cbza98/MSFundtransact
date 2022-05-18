package com.bankntt.MSFundtransact.application.helpers;

import java.util.function.BiFunction;

public class Holdergeneratorvalue {

	public static BiFunction<String, String, String> Identity = (s, i) -> {
		return (new StringBuilder()).append(s).append(i).toString();
	};

}
