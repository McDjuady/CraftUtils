/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.craftutils.validators;

import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Max
 */
public interface ShapedResultBuilder {
    public int[] generateCostMatrix(ItemStack[] matrix);
    public ItemStack getResult(ItemStack[] matrix);
}
