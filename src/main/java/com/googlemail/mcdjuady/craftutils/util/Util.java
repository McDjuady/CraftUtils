/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.craftutils.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Max
 */
public class Util {

    public static void decreaseItems(ItemStack[] matrix, int ammount) {
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] != null && matrix[i].getType() != Material.AIR) {
                int amnt = matrix[i].getAmount() - ammount;
                if (amnt <= 0) {
                    matrix[i] = new ItemStack(Material.AIR);
                } else {
                    matrix[i].setAmount(amnt);
                }
            }
        }
    }

    public static void decreaseItems(ItemStack[] matrix, int ammount, int[] costMatrix) {
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] == null || matrix[i].getType() == Material.AIR) {
                continue;
            }
            int amnt = matrix[i].getAmount() - costMatrix[i] * ammount;
            if (amnt <=0 ) {
                matrix[i] = new ItemStack(Material.AIR);
            } else {
                matrix[i].setAmount(amnt);
            }
        }
    }
}
