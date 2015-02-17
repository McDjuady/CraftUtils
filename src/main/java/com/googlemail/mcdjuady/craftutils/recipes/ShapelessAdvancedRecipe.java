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

/**
 *
 * @author Max
 */
public class ShapelessAdvancedRecipe extends ShapelessRecipe implements AdvancedRecipe {

    private ShapelessValidator result;

    public ShapelessAdvancedRecipe(ItemStack result, ShapelessValidator validator) {
        super(result);
        this.result = validator;
    }

    public boolean validate(ItemStack[] matrix) {
        List<ItemStack> ingredients = generateIngredientList(matrix);
        return result.validate(ingredients);
    }

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
        List<ItemStack> ingredients = new LinkedList<ItemStack>();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getType() != Material.AIR) {
                ingredients.add(items[i]);
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
                    int ammount = ingredient.getAmount()-1;
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

}
