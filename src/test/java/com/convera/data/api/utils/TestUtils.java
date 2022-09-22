package com.convera.data.api.utils;

import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

@UtilityClass
public class TestUtils {

  public static String getContentFromJsonFile(String filename) throws FileNotFoundException {
    // get expected response payload from json file
    InputStream expectedResponseInputStream = TestUtils.class.getClassLoader().getResourceAsStream(filename);
    if (expectedResponseInputStream == null) {
      throw new FileNotFoundException(filename + " is not found. Please check /test/resources folder.");
    }
    return JsonParser.parseReader(new InputStreamReader(expectedResponseInputStream)).toString();
  }
}
