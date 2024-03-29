package com.ebdesk.report.slide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CSVUtils {

	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DEFAULT_QUOTE = '"';

	public static void main(String[] args) throws Exception {

		String csvFile = "C:\\Users\\eBdesk\\Desktop\\JAVA\\sna-report\\output\\pk\\kontra_top10tweet.csv";
		Scanner scanner = new Scanner(new File(csvFile));
		String[][] data= new String[6][7];
		
		int index = 0;
		String headers[] = new String[7];
		int k = 0;
		String tweet = null;
		Map<String, String> map = new HashMap<String, String>();
		while (scanner.hasNext()) {
			List<String> line = parseLine(scanner.nextLine());
			if (index == 0) {
				for (int i = 0; i < 7; i++) {
					headers[i] = line.get(i);
					System.out.println(line.get(i));
				}
			} else {

				int ko = 0;
				k += 1;
				for (int c = 0; c < 7; c++) {
					try {
						data[k - 1][ko] = line.get(ko);
						System.out.println(line.get(ko));
						ko += 1;
					} catch (Exception e) {
					}
				}
			}
			index += 1;
		}
		scanner.close();

	}

	public static List<String> parseLine(String cvsLine) {
		return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
	}

	public static List<String> parseLine(String cvsLine, char separators) {
		return parseLine(cvsLine, separators, DEFAULT_QUOTE);
	}

	public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

		List<String> result = new ArrayList<>();

		// if empty, return!
		if (cvsLine == null && cvsLine.isEmpty()) {
			return result;
		}

		if (customQuote == ' ') {
			customQuote = DEFAULT_QUOTE;
		}

		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuffer curVal = new StringBuffer();
		boolean inQuotes = false;
		boolean startCollectChar = false;
		boolean doubleQuotesInColumn = false;

		char[] chars = cvsLine.toCharArray();

		for (char ch : chars) {

			if (inQuotes) {
				startCollectChar = true;
				if (ch == customQuote) {
					inQuotes = false;
					doubleQuotesInColumn = false;
				} else {

					// Fixed : allow "" in custom quote enclosed
					if (ch == '\"') {
						if (!doubleQuotesInColumn) {
							curVal.append(ch);
							doubleQuotesInColumn = true;
						}
					} else {
						curVal.append(ch);
					}

				}
			} else {
				if (ch == customQuote) {

					inQuotes = true;

					// Fixed : allow "" in empty quote enclosed
					if (chars[0] != '"' && customQuote == '\"') {
						curVal.append('"');
					}

					// double quotes in column will hit this!
					if (startCollectChar) {
						curVal.append('"');
					}

				} else if (ch == separators) {

					result.add(curVal.toString());

					curVal = new StringBuffer();
					startCollectChar = false;

				} else if (ch == '\r') {
					// ignore LF characters
					continue;
				} else if (ch == '\n') {
					// the end, break!
					break;
				} else {
					curVal.append(ch);
				}
			}

		}

		result.add(curVal.toString());

		return result;
	}

}