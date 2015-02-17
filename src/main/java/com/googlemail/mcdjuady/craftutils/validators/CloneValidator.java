/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.craftutils.validators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Max
 */
public class CloneValidator implements ShapelessValidator{

    @Override
    public ItemStack getResult(List<ItemStack> ingredients) {
        for (ItemStack item : ingredients) {
            if (item.hasItemMeta()) {
                ItemStack result = item.clone();
                result.setAmount(1);
                return result;
            }
        }
        return null;
    }

    @Override
    public boolean validate(List<ItemStack> ingredients) {
        boolean valid = false;
        for (ItemStack item : ingredients) {
            valid = valid ^ item.hasItemMeta();
        }
        return valid;
    }

    @Override
    public Map<ItemStack, Integer> costMatrix(List<ItemStack> ingredients) {
        Map<ItemStack,Integer> costMap = new HashMap<ItemStack,Integer>();
        for (ItemStack item : ingredients) {
            costMap.put(item, item.hasItemMeta() ? 0 : 1);
        }
        return costMap;
    }
    
}
