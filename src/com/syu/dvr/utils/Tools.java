package com.syu.dvr.utils;

import java.math.BigInteger;

public class Tools {
	private static final String FILE_SIZE_B = "B";

	private static final String FILE_SIZE_KB = "KB";

	private static final String FILE_SIZE_MB = "MB";

	private static final String FILE_SIZE_GB = "GB";

	private static final String FILE_SIZE_TB = "TB";

	private static final String FILE_SIZE_NA = "N/A";

	
	public static String formatSize(BigInteger size) {


		if (size.compareTo(BigInteger.valueOf(1024)) == -1) {

			return (size.toString() + FILE_SIZE_B);

		} else if (size.compareTo(BigInteger.valueOf(1024 * 1024)) == -1) {

			return (size.divide(BigInteger.valueOf(1024)).toString() + FILE_SIZE_KB);

		} else if (size.compareTo(BigInteger.valueOf(1024 * 1024 * 1024)) == -1) {

			return (size.divide(BigInteger.valueOf(1024 * 1024)).toString() + FILE_SIZE_MB);

		} else if (size.compareTo(BigInteger
				.valueOf(1024 * 1024 * 1024 * 1024L)) == -1) {

			return (size.divide(BigInteger.valueOf(1024 * 1024 * 1024))
					.toString() + FILE_SIZE_GB);

		} else if (size.compareTo(BigInteger
				.valueOf(1024 * 1024 * 1024 * 1024L).multiply(

				BigInteger.valueOf(1024))) == -1) {

			return (size.divide(BigInteger.valueOf(1024 * 1024 * 1024 * 1024L))
					.toString() + FILE_SIZE_TB);

		}

		return FILE_SIZE_NA;

	}
}
