/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.craftutils.recipes;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

/**
 *
 * @author Max
 */
public interface AdvancedRecipe extends Recipe{
    public boolean validate(ItemStack[] matrix);
    public boolean validateMaterix(ItemStack[] matrix);
    public ItemStack getResult(ItemStack[] matrix);
    public int[] getCostMatrix(ItemStack[] matrix);
}
