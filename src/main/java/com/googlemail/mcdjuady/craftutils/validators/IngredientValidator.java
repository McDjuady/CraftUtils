/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.craftutils.validators;

import com.googlemail.mcdjuady.craftutils.recipes.ShapedAdvancedRecipe;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Max
 */
public interface IngredientValidator {

    /**
     * Used to validate an ingredient in the ShapedAdvancedRecipe
     * For example you could check if an item has Lore or Enchantments
     * @see ShapedAdvancedRecipe
     * @param ingredient The ingredient to Test
     * @return True if the ingredient is valid, else false 
     */
    public boolean validate(ItemStack ingredient);
}
