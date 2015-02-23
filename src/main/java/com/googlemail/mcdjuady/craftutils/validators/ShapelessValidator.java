/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.craftutils.validators;

import java.util.List;
import java.util.Map;
import org.bukkit.inventory.ItemStack;

/**
 * This class is used to Validate and build the result for ShapelessAdvancedRecipes
 * @see ShapelessAdvancedRecipe
 * @author Max
 */
public interface ShapelessValidator {    

    /**
     * Returns the result of the recipe given the current ingredients
     * At this point the recipe has been validated and the ingredients can be
     * assumed to be correct
     * @param ingredients The current list of ingredients in the craftingMatrix
     * @return The desired result
     */
    public ItemStack getResult(List<ItemStack> ingredients);

    /**
     * Validates the current list of ingredients.
     * See CloneValidator for an example
     * @see CloneValidator
     * @param ingredients The current list of ingredients in the craftingMatrix
     * @return The desired result
     */
    public boolean validate(List<ItemStack> ingredients);
    /**
     * Generates a map with the cost of each item in the ingredients list.
     * At this point the recipe has been validated and the ingredients can be
     * assumed to be correct.
     * This can be used to have items which cost 2, but also items which cost 0.
     * If an item is not included in the map the cost will be 0
     * @param ingredients The current list of ingredients in the craftingMatrix
     * @return A map containing the costs of each item
     */
    public Map<ItemStack,Integer> costMatrix(List<ItemStack> ingredients);
}
