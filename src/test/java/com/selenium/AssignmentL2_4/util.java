package com.selenium.AssignmentL2_4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.DataProvider;

public class util {
	private static File file;
	private static FileInputStream fis;
	private static XSSFWorkbook workbook;
	private static XSSFSheet sheet;
	private static Object object;

	// function to read data from excel
	public static Object[][] readFromExcel(String sheetName) {
		try {
			file = new File(System.getProperty("user.dir") + "\\src\\test\\java\\com\\selenium\\AssignmentL2_4\\data.xlsx");
			fis = new FileInputStream(file);

			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheet(sheetName);

			int rows = sheet.getLastRowNum();
			int cols = sheet.getRow(0).getLastCellNum();

			Object[][] excelData = new Object[rows][cols];

			for (int i = 0; i < rows; i++) {

				for (int j = 0; j < cols; j++) {
					XSSFCell cell = sheet.getRow(i + 1).getCell(j);

					CellType cellType = cell.getCellType();

					switch (cellType) {

					case STRING:
						excelData[i][j] = cell.getStringCellValue();
						System.out.println(cell.getStringCellValue());
						break;

					case NUMERIC:
						excelData[i][j] = Integer.toString((int) cell.getNumericCellValue());
						break;
					}
				}
			}

			return excelData;
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// Function to take screenshot
	public static void screenshot(WebDriver driver) {
		try {
			Date currentDate = new Date();
			String screenshotfilename = currentDate.toString().replace(" ", "-").replace(":", "-");
			int randomInt = (int)(Math.random() * 100); // 0 to 99
			File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshotFile, new File(".//screenshot/"+screenshotfilename+"-"+randomInt+".png"));
			
		} catch (WebDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
