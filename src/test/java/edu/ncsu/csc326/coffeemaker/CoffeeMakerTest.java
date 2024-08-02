/*
 * Copyright (c) 2009,  Sarah Heckman, Laurie Williams, Dright Ho
 * All Rights Reserved.
 * 
 * Permission has been explicitly granted to the University of Minnesota 
 * Software Engineering Center to use and distribute this source for 
 * educational purposes, including delivering online education through
 * Coursera or other entities.  
 * 
 * No warranty is given regarding this software, including warranties as
 * to the correctness or completeness of this software, including 
 * fitness for purpose.
 * 
 * 
 * Modifications 
 * 20171114 - Ian De Silva - Updated to comply with JUnit 4 and to adhere to 
 * 							 coding standards.  Added test documentation.
 */
package edu.ncsu.csc326.coffeemaker;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Unit tests for CoffeeMaker class.
 * 
 * @author Sarah Heckman
 */
public class CoffeeMakerTest {
	
	/**
	 * The object under test.
	 */
	private CoffeeMaker coffeeMaker;
	private RecipeBook recipeBook;


	// Sample recipes to use in testing.
	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;
	private Recipe recipe4;

	private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;

	/**
	 * Initializes some recipes to test with and the {@link CoffeeMaker} 
	 * object we wish to test.
	 * 
	 * @throws RecipeException  if there was an error parsing the ingredient 
	 * 		amount when setting up the recipe.
	 */
	@Before
	public void setUp() throws RecipeException {
		System.setOut(new PrintStream(outputStream));
		coffeeMaker = new CoffeeMaker();
		recipeBook = new RecipeBook();

		
		//Set up for r1
		recipe1 = new Recipe();
		recipe1.setName("Coffee");
		recipe1.setAmtChocolate("0");
		recipe1.setAmtCoffee("3");
		recipe1.setAmtMilk("1");
		recipe1.setAmtSugar("1");
		recipe1.setPrice("50");

		
		//Set up for r2
		recipe2 = new Recipe();
		recipe2.setName("Mocha");
		recipe2.setAmtChocolate("20");
		recipe2.setAmtCoffee("3");
		recipe2.setAmtMilk("1");
		recipe2.setAmtSugar("1");
		recipe2.setPrice("75");
		
		//Set up for r3
		recipe3 = new Recipe();
		recipe3.setName("Latte");
		recipe3.setAmtChocolate("0");
		recipe3.setAmtCoffee("3");
		recipe3.setAmtMilk("3");
		recipe3.setAmtSugar("1");
		recipe3.setPrice("100");
		
		//Set up for r4
		recipe4 = new Recipe();
		recipe4.setName("Hot Chocolate");
		recipe4.setAmtChocolate("4");
		recipe4.setAmtCoffee("0");
		recipe4.setAmtMilk("1");
		recipe4.setAmtSugar("1");
		recipe4.setPrice("65");
	}

	@After
	public void tearDown() {
		System.setOut(originalOut);
		System.setIn(System.in);
	}

	/**
	 * Given 4 recipes it's gonna try to put it in the recipe book
	 */
	@Test
	public void addFourRecipes() {
        assertTrue(coffeeMaker.addRecipe(recipe1));
		assertTrue(coffeeMaker.addRecipe(recipe2));
		assertTrue(coffeeMaker.addRecipe(recipe3));
		assertFalse(coffeeMaker.addRecipe(recipe4));
	}

	/**
	 * Add two recipes with the same name
	 */
	@Test
	public void twoRecipesWithSameName() {
		assertTrue(coffeeMaker.addRecipe(recipe1));
		assertFalse(coffeeMaker.addRecipe(recipe1));
	}

	/**
	 * Add recipe with the empty name
	 */
	@Test(expected = RecipeException.class)
	public void recipeWithEmptyName() throws RecipeException {
		recipe1.setName("");
		coffeeMaker.addRecipe(recipe1);
	}

	/**
	 * Delete a recipe
	 */
	@Test
	public void deleteRecipe() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(coffeeMaker.getRecipes()[0],recipe1);
		coffeeMaker.deleteRecipe(0);
		Recipe recipe = coffeeMaker.getRecipes()[0];
		assertEquals("", recipe.getName());
		assertEquals(0, recipe.getAmtCoffee());
		assertEquals(0, recipe.getAmtMilk());
		assertEquals(0, recipe.getPrice());
		assertEquals(0, recipe.getAmtSugar());
	}

	/**
	 * Edit correctly a recipe
	 */
	@Test
	public void editRecipe() throws RecipeException {
		coffeeMaker.addRecipe(recipe1);

		//Set up for r4
		Recipe recipeModified = new Recipe();
		recipeModified.setAmtChocolate("4");
		recipeModified.setAmtCoffee("0");
		recipeModified.setAmtMilk("1");
		recipeModified.setAmtSugar("1");
		recipeModified.setPrice("65");

		Recipe recipeToCompare = new Recipe();
		recipeToCompare.setName(recipe1.getName());
		recipeToCompare.setAmtChocolate("4");
		recipeToCompare.setAmtCoffee("0");
		recipeToCompare.setAmtMilk("1");
		recipeToCompare.setAmtSugar("1");
		recipeToCompare.setPrice("65");

		coffeeMaker.editRecipe(0,recipeModified);
		assertEquals(coffeeMaker.getRecipes()[0], recipeToCompare);
	}

	/**
	 * Edit a recipe with negative ingredient amount
	 */
	@Test(expected = RecipeException.class)
	public void editRecipeWithNegativeValues() throws RecipeException {
		coffeeMaker.addRecipe(recipe1);

		//Set up for r4
		Recipe recipeModified = new Recipe();
		recipeModified.setAmtChocolate("-4");
		recipeModified.setAmtCoffee("0");
		recipeModified.setAmtMilk("1");
		recipeModified.setAmtSugar("1");
		recipeModified.setPrice("65");

		coffeeMaker.editRecipe(0,recipeModified);
	}

	/**
	 * Edit a recipe incorrectly  with a decimal price
	 */
	@Test(expected = RecipeException.class)
	public void editRecipeWithDecimalPrice() throws RecipeException {
		coffeeMaker.addRecipe(recipe1);

		//Set up for r4
		Recipe recipeModified = new Recipe();
		recipeModified.setAmtChocolate("4");
		recipeModified.setAmtCoffee("0");
		recipeModified.setAmtMilk("1");
		recipeModified.setAmtSugar("1");
		recipeModified.setPrice("65.25");

		coffeeMaker.editRecipe(0,recipeModified);
	}

	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with well-formed quantities
	 * Then we do not get an exception trying to read the inventory quantities.
	 * 
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test
	public void testAddInventory() throws InventoryException {
		coffeeMaker.checkInventory();
		coffeeMaker.addInventory("4","7","0","9");
	}
	
	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with malformed quantities (i.e., a negative 
	 * quantity and a non-numeric string)
	 * Then we get an inventory exception
	 * 
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddInventoryException() throws InventoryException {
		coffeeMaker.addInventory("4", "-1", "asdf", "3");
	}
	
	/**
	 * Given a coffee maker with one valid recipe
	 * When we make coffee, selecting the valid recipe and paying more than 
	 * 		the coffee costs
	 * Then we get the correct change back.
	 */
	@Test
	public void testMakeCoffee() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(25, coffeeMaker.makeCoffee(0, 75));
	}

	@Test
	public void testMakeCoffeePriceException() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(25, coffeeMaker.makeCoffee(0, 75));
	}


	@Test
	public void testMakeCoffeeWithInvalidRecipe() {
		assertEquals(100, coffeeMaker.makeCoffee(1, 100));
	}

	@Test
	public void testMakeCoffeeInsufficientIngredients() {
		coffeeMaker.addRecipe(recipe2);
		assertEquals(75, coffeeMaker.makeCoffee(0, 75));
	}

	@Test
	public void testCheckInventory() {
		assertEquals("Coffee: 15\nMilk: 15\nSugar: 15\nChocolate: 15\n", coffeeMaker.checkInventory());
	}


	// Duplicate recipe
	@Test
	public void testAddRecipe() {
		assertTrue(recipeBook.addRecipe(recipe1));
		assertTrue(recipeBook.addRecipe(recipe2));
		assertFalse(recipeBook.addRecipe(recipe1));
	}

	@Test
	public void testEditRecipe() throws RecipeException {
		recipeBook.addRecipe(recipe1);
		Recipe newRecipe = new Recipe();
		newRecipe.setAmtChocolate("1");
		newRecipe.setAmtCoffee("2");
		newRecipe.setAmtMilk("3");
		newRecipe.setAmtSugar("1");
		newRecipe.setPrice("70");

		assertEquals("Coffee", recipeBook.editRecipe(0, newRecipe));
		assertEquals(newRecipe.getAmtCoffee(), recipeBook.getRecipes()[0].getAmtCoffee());
	}


	@Test
	public void testSetName() throws RecipeException {
		recipe3.setName("Latte");
		assertEquals("Latte", recipe3.getName());
	}

	@Test(expected = RecipeException.class)
	public void testSetNameNull() throws RecipeException {
		recipe2.setName(null);
	}

	@Test
	public void testSetPrice() throws RecipeException {
		recipe2.setPrice("100");
		assertEquals(100, recipe2.getPrice());
	}

	@Test(expected = RecipeException.class)
	public void testSetPriceNegative() throws RecipeException {
		recipe2.setPrice("-100");
	}

	@Test
	public void testSetAmtCoffee() throws RecipeException {
		recipe1.setAmtCoffee("3");
		assertEquals(3, recipe1.getAmtCoffee());
	}

	@Test(expected = RecipeException.class)
	public void testSetAmtCoffeeNegative() throws RecipeException {
		recipe3.setAmtCoffee("-3");
	}

	@Test
	public void testSetAmtMilk() throws RecipeException {
		recipe1.setAmtMilk("2");
		assertEquals(2, recipe1.getAmtMilk());
	}

	@Test(expected = RecipeException.class)
	public void testSetAmtMilkNegative() throws RecipeException {
		recipe3.setAmtMilk("-2");
	}

	@Test
	public void testSetAmtSugar() throws RecipeException {
		recipe2.setAmtSugar("1");
		assertEquals(1, recipe2.getAmtSugar());
	}

	@Test(expected = RecipeException.class)
	public void testSetAmtSugarNegative() throws RecipeException {
		recipe1.setAmtSugar("-1");
	}

	@Test
	public void testSetAmtChocolate() throws RecipeException {
		recipe3.setAmtChocolate("4");
		assertEquals(4, recipe3.getAmtChocolate());
	}

	@Test(expected = RecipeException.class)
	public void testSetAmtChocolateNegative() throws RecipeException {
		recipe1.setAmtChocolate("-4");
	}

	@Test
	public void testToString() throws RecipeException {
		recipe2.setName("Espresso");
		assertEquals("Espresso", recipe2.toString());
	}

	@Test
	public void testHashCode() throws RecipeException {
		recipe2.setName("Mocha");
		assertNotNull(recipe2.hashCode());
	}

	@Test
	public void testEquals() throws RecipeException {
		Recipe other = new Recipe();
		other.setName("Mocha");

		recipe2.setName("Mocha");
		assertTrue(recipe2.equals(other));
	}

	@Test
	public void testNotEquals() throws RecipeException {
		Recipe other = new Recipe();
		other.setName("Latte");

		recipe1.setName("Mocha");
		assertFalse(recipe1.equals(other));
	}

	// Tests for Main class functionalities
	@Test
	public void testMainMenuExit() {
		String input = "0\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		Main.main(new String[]{});
		String output = outputStream.toString();

		// Verifica que se haya mostrado el mensaje de bienvenida y el menú
		assertTrue(output.contains("Welcome to the CoffeeMaker!\n"));
		assertTrue(output.contains("1. Add a recipe"));
		assertTrue(output.contains("0. Exit\n"));
	}

	@Test
	public void testAddRecipeMain() {
		// Simular la entrada del usuario para agregar una receta
		String input = "1\nEspresso\n150\n3\n0\n0\n0\n0\n";
		System.setIn(new ByteArrayInputStream(input.getBytes()));

		Main.main(new String[]{});
		String output = outputStream.toString();

		// Verificar que la receta se haya añadido exitosamente
		assertTrue(output.contains("Espresso successfully added.\n"));
	}

}
