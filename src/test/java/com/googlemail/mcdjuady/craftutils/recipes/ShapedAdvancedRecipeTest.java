/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.craftutils.recipes;

import com.googlemail.mcdjuady.craftutils.validators.IngredientValidator;
import com.googlemail.mcdjuady.craftutils.util.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Max
 */
public class ShapedAdvancedRecipeTest {
    
    public ShapedAdvancedRecipeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws IllegalAccessException {
        Server mockServer = mock(Server.class);
        ItemFactory mockFactory = mock(ItemFactory.class);
        
        when(mockServer.getItemFactory()).thenReturn(mockFactory);
        when(mockFactory.equals(any(ItemMeta.class), any(ItemMeta.class))).thenReturn(true);
        when(mockFactory.equals(any(ItemMeta.class), isNull(ItemMeta.class))).thenReturn(false);
        when(mockFactory.equals(isNull(ItemMeta.class), isNull(ItemMeta.class))).thenReturn(true);
        
        FieldUtils.writeStaticField(Bukkit.class, "server", mockServer, true);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of validate method, of class ShapedAdvancedRecipe.
     */
    public class TestValidator implements IngredientValidator {

        @Override
        public boolean validate(ItemStack ingredient) {
            return ingredient.getDurability()==1;
        }
        
    }
    
    @Test
    public void testValidate() {
        System.out.println("validate");
        ItemStack[] matrix = new ItemStack[10];
        matrix[1] = new ItemStack(Material.IRON_INGOT);
        matrix[5] = new ItemStack(Material.IRON_INGOT);
        matrix[5].setDurability((short)1);
        ShapedAdvancedRecipe instance = new ShapedAdvancedRecipe(new ItemStack(Material.SHEARS),null);
        instance.shape( "x ",
                        " x");
        instance.setIngredient('x', Material.IRON_INGOT);
        boolean expResult = true;
        boolean result = instance.validate(matrix);
        assertEquals(expResult, result);
        matrix[4] = matrix[1];
        matrix[8] = matrix[5];
        matrix[1] = null;
        matrix[5] = null;
        instance.shape("x"," y");
        //instance.setIngredient('y', Material.IRON_INGOT);
        instance.setIngredient('y', Material.IRON_INGOT, new TestValidator());
        assertTrue(instance.validate(matrix));
        matrix[8] = null;
        assertFalse(instance.validate(matrix));
        matrix = new ItemStack[10];
        matrix[4] = new ItemStack(Material.IRON_CHESTPLATE);
        matrix[7] = new ItemStack(Material.ENDER_PEARL);
        ShapedAdvancedRecipe recipe = new ShapedAdvancedRecipe(new ItemStack(Material.IRON_CHESTPLATE), null);
        recipe.shape(" e ","fcf"," r ").setIngredient('e', Material.EYE_OF_ENDER).setIngredient('f', Material.FEATHER).setIngredient('c', Material.IRON_CHESTPLATE).setIngredient('r', Material.REDSTONE);
        assertFalse(recipe.validate(matrix));
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getResult method, of class ShapedAdvancedRecipe.
     *//*
    @Test
    public void testGetResult() {
        System.out.println("getResult");
        ItemStack[] matrix = null;
        ShapedAdvancedRecipe instance = null;
        ItemStack expResult = null;
        ItemStack result = instance.getResult(matrix);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of getCostMatrix method, of class ShapedAdvancedRecipe.
     *//*
    @Test
    public void testGetCostMatrix() {
        System.out.println("getCostMatrix");
        ItemStack[] matrix = null;
        ShapedAdvancedRecipe instance = null;
        int[] expResult = null;
        int[] result = instance.getCostMatrix(matrix);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of shape method, of class ShapedAdvancedRecipe.
     *//*
    @Test
    public void testShape() {
        System.out.println("shape");
        String[] shape = null;
        ShapedAdvancedRecipe instance = null;
        ShapedRecipe expResult = null;
        ShapedRecipe result = instance.shape(shape);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
    
}
