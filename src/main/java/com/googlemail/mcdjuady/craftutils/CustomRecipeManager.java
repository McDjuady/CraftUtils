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

    private HashMap<ItemStack, List<AdvancedRecipe>> recipes;

    private static CustomRecipeManager instance;

    public static CustomRecipeManager getInstance() {
        if (instance == null) {
            instance = new CustomRecipeManager();
        }
        return instance;
    }

    public CustomRecipeManager() {
        recipes = new HashMap<ItemStack, List<AdvancedRecipe>>();
    }

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

    public boolean hasRecipe(ItemStack result) {
        return recipes.containsKey(result);
    }

    public boolean addRecipe(AdvancedRecipe recipe) {
        if (!Bukkit.addRecipe(recipe)) {
            Bukkit.getLogger().warning("[CustomRecipes] Failed to add recipe!");
            return false;
        }
        List<AdvancedRecipe> list = recipes.get(recipe.getResult());
        if (list == null) {
            list = new LinkedList<AdvancedRecipe>();
            recipes.put(recipe.getResult(), list);
        }
        Bukkit.getLogger().info("[CustomRecipes] Added custom recipe!");
        return list.add(recipe);
    }
}
