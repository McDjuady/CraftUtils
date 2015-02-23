/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.craftutils.recipes;

import com.googlemail.mcdjuady.craftutils.validators.ShapelessValidator;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;

/**
 * This class is used for AdvancedShapelessRecipes
 * It provides enhanced functionality to Bukkit's ShapelessRecipes
 * @see ShapelessRecipe
 * @see ShapelessValidator
 * @author Max
 */
public class ShapelessAdvancedRecipe extends ShapelessRecipe implements AdvancedRecipe {

    private final ShapelessValidator result;

    public ShapelessAdvancedRecipe(ItemStack result, ShapelessValidator validator) {
        super(result);
        this.result = validator;
    }

    @Override
    public boolean validate(ItemStack[] matrix) {
        List<ItemStack> ingredients = generateIngredientList(matrix);
        return result.validate(ingredients);
    }

    @Override
    public ItemStack getResult(ItemStack[] matrix) {
        List<ItemStack> ingredients = generateIngredientList(matrix);
        ItemStack resultItem = result.getResult(ingredients);
        return (resultItem == null || resultItem.getType() == Material.AIR) ? new ItemStack(Material.AIR) : resultItem.clone();
    }

    @Override
    public int[] getCostMatrix(ItemStack[] matrix) {
        List<ItemStack> ingredients = generateIngredientList(matrix);
        Map<ItemStack, Integer> costMap = result.costMatrix(ingredients);
        int[] costs = new int[matrix.length];
        if (costMap == null) {
            Arrays.fill(costs, 0);
            return costs;
        }
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] == null || matrix[i].getType() == Material.AIR) {
                costs[i] = 0;
                continue;
            }
            Integer cost = costMap.get(matrix[i]);
            costs[i] = cost == null ? 0 : cost; //default cost is 0 
        }
        return costs;
    }

    private List<ItemStack> generateIngredientList(ItemStack[] items) {
        List<ItemStack> ingredients = new LinkedList<>();
        for (ItemStack item : items) {
            if (item != null && item.getType() != Material.AIR) {
                ingredients.add(item);
            }
        }
        return ingredients;
    }

    @Override
    public boolean validateMaterix(ItemStack[] matrix) {
        List<ItemStack> matrixList = generateIngredientList(matrix);
        List<ItemStack> ingredients = getIngredientList();
        for (ItemStack item : matrixList) {
            for (ItemStack ingredient : ingredients) {
                if (ingredient.getType() == item.getType()) {
                    int ammount = ingredient.getAmount() - 1;
                    if (ammount <= 0) {
                        ingredients.remove(ingredient);
                    } else {
                        ingredient.setAmount(ammount);
                    }
                    break;
                }
            }
        }
        return ingredients.isEmpty();
    }

//<editor-fold defaultstate="collapsed" desc="Chaining Methods">
    @Override
    public ShapelessAdvancedRecipe addIngredient(Material ingredient) {
        return (ShapelessAdvancedRecipe) super.addIngredient(ingredient); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ShapelessAdvancedRecipe addIngredient(MaterialData ingredient) {
        return (ShapelessAdvancedRecipe) super.addIngredient(ingredient); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ShapelessAdvancedRecipe addIngredient(Material ingredient, int rawdata) {
        return (ShapelessAdvancedRecipe) super.addIngredient(ingredient, rawdata); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ShapelessAdvancedRecipe addIngredient(int count, Material ingredient) {
        return (ShapelessAdvancedRecipe) super.addIngredient(count, ingredient); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ShapelessAdvancedRecipe addIngredient(int count, MaterialData ingredient) {
        return (ShapelessAdvancedRecipe) super.addIngredient(count, ingredient); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ShapelessAdvancedRecipe addIngredient(int count, Material ingredient, int rawdata) {
        return (ShapelessAdvancedRecipe) super.addIngredient(count, ingredient, rawdata); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ShapelessAdvancedRecipe removeIngredient(Material ingredient) {
        return (ShapelessAdvancedRecipe) super.removeIngredient(ingredient); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ShapelessAdvancedRecipe removeIngredient(MaterialData ingredient) {
        return (ShapelessAdvancedRecipe) super.removeIngredient(ingredient); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ShapelessAdvancedRecipe removeIngredient(Material ingredient, int rawdata) {
        return (ShapelessAdvancedRecipe) super.removeIngredient(ingredient, rawdata); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ShapelessAdvancedRecipe removeIngredient(int count, Material ingredient) {
        return (ShapelessAdvancedRecipe) super.removeIngredient(count, ingredient); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ShapelessAdvancedRecipe removeIngredient(int count, MaterialData ingredient) {
        return (ShapelessAdvancedRecipe) super.removeIngredient(count, ingredient); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ShapelessAdvancedRecipe removeIngredient(int count, Material ingredient, int rawdata) {
        return (ShapelessAdvancedRecipe) super.removeIngredient(count, ingredient, rawdata); //To change body of generated methods, choose Tools | Templates.
    }
//</editor-fold>
}
