package utilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.DataProvider;

import commons.GlobalConstants;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProviderFactory {

	protected final Log logback;
	
	protected DataProviderFactory() {
		logback = LogFactory.getLog(getClass());
	}

	   @DataProvider(name = "excelData")
	   public Object[][] dataProviderMethod(Method method) {
	      Object[][] excelData = null;
	      String testCaseId = null;
	      try {
	         logback.info("Parsing the test jsonData...");
	         String testCaseName = method.getName();
	         testCaseId = testCaseName;
	         JSONObject jsonData = (JSONObject) getTestData().get(testCaseName);
	         if (jsonData.isEmpty()) {
	            throw new IllegalArgumentException("The test jsonData of the testcase Id '" + testCaseId + "' is empty");
	         }
	         
	         String xlFilePath = jsonData.get("xlFilePath").toString();
	         String sheetName = jsonData.get("sheetName").toString();
	         String specificRows = jsonData.get("specificRows").toString();
	         if (specificRows.isEmpty()) {
	            excelData = getTestData(xlFilePath, sheetName);
	         } else {
	            if (specificRows.contains("-")) {
	               excelData = getTestDataBySpecificRange(xlFilePath, sheetName, specificRows);
	            } else {
	               excelData = getTestDataBySpecificRows(xlFilePath, sheetName, specificRows);
	            }
	         }
	      } catch (NullPointerException e) {
	         logback.error("Not found the testcase Id '{}' in the testdata");
	      } catch (ArrayIndexOutOfBoundsException e) {
	         logback.error("The format of the testcase is not correct. Please help to check.");
	      } catch (Exception e) {
	      }
	      return excelData;
	   }


	   private Object[][] getTestData(String xlFilePath, String sheetName) throws IOException {
	      ExcelHelper excelHelper = new ExcelHelper(xlFilePath);

	      int rows = excelHelper.getRowCount(sheetName);

	      int columns = excelHelper.getColumnCount(sheetName);

	      Object[][] excelData = new Object[rows - 1][1];

	      List<String> columnNames = excelHelper.getColumnNames(sheetName, columns);

	      for (int i = 1; i < rows; i++) {
	         Map<String, String> rowDataMap = new HashMap<>();
	         for (String columnName : columnNames) {
	            String cellData = excelHelper.getCellData(sheetName, columnName, i);
	            rowDataMap.put(columnName, cellData);
	         }
	         excelData[i - 1][0] = rowDataMap;
	      }

	      return excelData;
	   }

	   private Object[][] getTestDataBySpecificRows(String xlFilePath, String sheetName, String specificRows) throws IOException {

	      Object[][] excelData = this.getTestData(xlFilePath, sheetName);

	      List<String> listSpecificRows = Arrays.asList(specificRows.split("\\s*,\\s*"));

	      Object[][] newExcelData = new Object[listSpecificRows.size()][1];

	      for (int i = 0; i < listSpecificRows.size(); i++) {
	         int specificRow = Integer.valueOf(listSpecificRows.get(i)) - 1;
	         newExcelData[i][0] = excelData[specificRow][0];
	      }
	      return newExcelData;
	   }

	   private Object[][] getTestDataBySpecificRange(String xlFilePath, String sheetName, String range) throws IOException {

	      Object[][] excelData = this.getTestData(xlFilePath, sheetName);

	      List<String> listRange = Arrays.asList(range.split("\\s*-\\s*"));

	      int fromRow = Integer.parseInt(listRange.get(0));
	      int toRow = Integer.parseInt(listRange.get(1));


	      Object[][] newExcelData = new Object[toRow - fromRow + 1][1];

	      for (int i = fromRow; i <= toRow; i++) {
	         newExcelData[i - fromRow][0] = excelData[i - 1][0];
	      }
	      logback.info(String.format("Test case is run from jsonData row %s to row %s", fromRow, toRow));
	      return newExcelData;
	   }

	   protected JSONObject getTestData() throws IOException, ParseException {
	      Reader reader = new FileReader(GlobalConstants.getGlobalConstants().getDATA_JSON_PATH());
	      JSONParser parser = new JSONParser();
	      JSONObject result = (JSONObject) parser.parse(reader);
	      if (result == null) {
	         logback.error("testData is null!!!");
	      }
	      return result;
	   }
}
