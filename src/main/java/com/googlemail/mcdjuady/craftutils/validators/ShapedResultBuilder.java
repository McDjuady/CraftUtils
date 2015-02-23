/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.craftutils.validators;

import org.bukkit.inventory.ItemStack;

/**
 * This class is used to create the Result for an ShapedAdvancedRecipe
 * @see ShapedAdvancedRecipe
 * @author Max
 */
public interface ShapedResultBuilder {

    /**
     * Generates a matrix with the cost of each item in the crafting matrix.
     * At this point the matrix has been checked by all Validators and can be 
     * assumed to include all desired ingredients.
     * This can be used to have items which cost 2, but also items which cost 0.
     * The default amount is always 0
     * The returned matrix must always have the same length as the craftingMatrix
     * or else an exception will be thrown.
     * @param matrix The current craftingMatrix
     * @return A matrix containing the costs of each item
     */
    public int[] generateCostMatrix(ItemStack[] matrix);

    /**
     * Returns the result of the recipe given the current craftingMatrix
     * At this point the matrix has been checked by all Validators and can be 
     * assumed to include all desired ingredients
     * @param matrix The current craftingMatrix
     * @return The desired result
     */
    public ItemStack getResult(ItemStack[] matrix);
}
