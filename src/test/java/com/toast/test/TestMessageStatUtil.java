package com.toast.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;

import org.json.JSONArray;
import org.json.JSONException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;


import com.toast.utils.MessageStatUtil;

public class TestMessageStatUtil {

	@ParameterizedTest
	@MethodSource("crunchNumArgs")
	public void testCrunchNumbers(JSONArray array, JSONArray expected) {
		MessageStatUtil tester = new MessageStatUtil();
		JSONArray actual = tester.crunchNumbers(array);
		System.out.println(actual);
		System.out.println(expected);
		JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
		System.out.println("Testing");
	}

	private static Stream<Arguments> crunchNumArgs() throws JSONException, FileNotFoundException, IOException {

		File inputFile1 = new File("src/test/resources/crunchNumArgs/input/test1.json");
		File expectedFile1 = new File("src/test/resources/crunchNumArgs/expectedValues/test1.json");
		JSONArray input1 = new JSONArray(IOUtils.toString(new FileInputStream(inputFile1)));
		JSONArray expected1 = new JSONArray(IOUtils.toString(new FileInputStream(expectedFile1)));

		return Stream.of(Arguments.of(input1, expected1));
	}
}
