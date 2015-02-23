/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.craftutils;

import com.googlemail.mcdjuady.craftutils.recipes.AdvancedRecipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Max
 */
public class CustomRecipeManager {

    private final HashMap<ItemStack, List<AdvancedRecipe>> recipes;

    private static CustomRecipeManager instance;

    public static CustomRecipeManager getInstance() {
        if (instance == null) {
            instance = new CustomRecipeManager();
        }
        return instance;
    }
    
    protected static void onDisable() {
        instance = null;
    }
    
    private CustomRecipeManager() {
        recipes = new HashMap<>();
    }

    /**
     * Gets a list of Recipes which fit the current crafting matrix
     * This method ignores the Validators and just compares the shape
     * @param result The registered result of the desired recipe
     * @param matrix The current crafting matrix
     * @return
     */
    public List<AdvancedRecipe> getRecipes(ItemStack result, ItemStack[] matrix) {
        List<AdvancedRecipe> recipeList = recipes.get(result);
        List<AdvancedRecipe> retList = new ArrayList<>();
        for (AdvancedRecipe recipe : recipeList) {
            if (recipe.validateMaterix(matrix)) {
                retList.add(recipe);
            }
        }
        return retList;
    }
    
    /**
     * True if there is a recipe with the desired result
     * @param result The desired result
     * @return If there are recipes matching the result
     */
    public boolean hasRecipe(ItemStack result) {
        return recipes.containsKey(result);
    }

    /**
     * Adds an AdvancedRecipe to the manager. This recipe will be tracked and
     * the Validators will be applied accordingly
     * This Method also registers the recipe with Bukkit 
     * @param recipe The recipe to add
     * @return if the recipe was successfully added
     */
    public boolean addRecipe(AdvancedRecipe recipe) {
        if (!Bukkit.addRecipe(recipe)) {
            Bukkit.getLogger().warning("[CustomRecipes] Failed to add recipe!");
            return false;
        }
        List<AdvancedRecipe> list = recipes.get(recipe.getResult());
        if (list == null) {
            list = new LinkedList<>();
            recipes.put(recipe.getResult(), list);
        }
        Bukkit.getLogger().info("[CustomRecipes] Added custom recipe!");
        return list.add(recipe);
    }
}
