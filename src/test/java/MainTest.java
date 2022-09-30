package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import main.java.Main;
import main.java.Vehicle;

public class MainTest {
	ArrayList<Vehicle> db;
	HashMap<String, Integer> totalMap;

	@Before
	public void startUp() throws Exception {
		this.db = Main.generateDatabase(
				"/Users/jacobpyke/Documents/University Projects/Further Programming/s3755145/Fleet.csv");
		this.totalMap = Main.calculateTotals(this.db.get(0),
				Period.between(LocalDate.parse("2022-09-23"), LocalDate.parse("2022-09-30")));
	}

	@Test
	public void testDatabaseNotNull() throws Exception {
		assertNotNull(this.db);
	}

	@Test
	public void testDatabaseLength() throws Exception {
		assertEquals(9, this.db.size());
	}

	@Test(expected = FileNotFoundException.class)
	public void testGenerateDatabase() throws Exception {
		Main.generateDatabase(
				"thisPathIsn'tReal");

	}

	@Test
	public void testParseDiscount() throws Exception {
		Integer discount = Main.parseDiscount("10");
		Integer result = 10;
		assertEquals(result, discount);
	}

	@Test(expected = NumberFormatException.class)
	public void testDiscountParseError() throws Exception {
		Main.parseDiscount("ThisValueIsInvalid");
	}

	@Test
	public void testTotalsWereCalculated() throws Exception {
		assertNotNull(this.totalMap);
	}

	@Test
	public void testTotalRentalCost() throws Exception {
		Integer result = 350;
		assertEquals(result, this.totalMap.get("TotalRentalCost"));
	}

	@Test
	public void testRentWithDiscount() throws Exception {
		Integer result = 340;
		assertEquals(result, this.totalMap.get("RentWithDiscount"));

	}

	@Test
	public void testTotalInsuranceCost() throws Exception {
		Integer result = 105;
		assertEquals(result, this.totalMap.get("TotalInsuranceCost"));
	}

	@Test
	public void testTotal() throws Exception {
		Integer result = 455;
		assertEquals(result, this.totalMap.get("Total"));
	}
}