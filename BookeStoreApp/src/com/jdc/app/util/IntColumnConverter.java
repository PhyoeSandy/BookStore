package com.jdc.app.util;

import java.text.DecimalFormat;
import java.text.ParseException;

import javafx.util.StringConverter;

public class IntColumnConverter extends StringConverter<Integer> {
	
	private static DecimalFormat DF = new DecimalFormat("#,##0");

	@Override
	public String toString(Integer data) {
		if(data != null) {
			return DF.format(data);
		}
		return null;
	}

	@Override
	public Integer fromString(String string) {
		if(!string.isEmpty())
			try {
				return DF.parse(string).intValue();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return null;
	}

}
