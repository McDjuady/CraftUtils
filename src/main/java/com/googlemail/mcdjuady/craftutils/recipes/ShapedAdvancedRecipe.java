/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.craftutils.recipes;

import com.googlemail.mcdjuady.craftutils.validators.IngredientValidator;
import com.googlemail.mcdjuady.craftutils.validators.ShapedResultBuilder;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;

/**
 * This class is used for AdvancedShapedRecipes
 * It provides enhanced functionality to Bukkit's ShapedRecipes
 * @see ShapedRecipe
 * @see IngredientValidator
 * @see ShapedResultBuilder
 * @author Max
 */
public class ShapedAdvancedRecipe extends ShapedRecipe implements AdvancedRecipe {

    private String rows[];
    private Map<Character, IngredientValidator> validators;
    private final ShapedResultBuilder resultBuilder;

    public ShapedAdvancedRecipe(ItemStack result, ShapedResultBuilder builder) {
        super(result);
        validators = new HashMap<>();
        resultBuilder = builder;
    }

    @Override
    public boolean validate(ItemStack[] matrix) {
        return _validate(matrix, true);
    }

    private boolean _validate(ItemStack[] matrix, boolean useValidators) {
        Map<Character, ItemStack> ingredients = getIngredientMap();
        int craftingSize = matrix.length == 10 ? 3 : 2;
        int numRows = rows.length;
        int rowWidth = 0;
        for (String row : rows) {
            rowWidth = Math.max(rowWidth, row.length());
        }
        //System.out.println("ml "+matrix.length+" cS "+craftingSize+" nr "+numRows+" rw "+rowWidth);
        if (craftingSize < numRows || craftingSize < rowWidth) {

            return false;
        }

        int xOffset = 0;
        int yOffset = 0;

        if (numRows != craftingSize || rowWidth != craftingSize) {
            char[] row = rows[0].toCharArray();
            int y = 0;
            int x = 0;
            //try to match the first row
            for (int i = 0; i < row.length; i++) {
                ItemStack currentItem = matrix[y * craftingSize + x];
                ItemStack rowItem = ingredients.get(row[i]);
                if (rowItem != currentItem && ((currentItem == null || rowItem == null) || rowItem.getType() != currentItem.getType())) {
                    xOffset++;
                    xOffset %= craftingSize;
                    if (xOffset == 0) {
                        yOffset++;
                    }
                    if (yOffset == craftingSize) {
                        return false;
                    }
                    i = -1; //reset the row index to the first char in the row
                }
                x++;
                x %= craftingSize;
                if (x == 0 && i != row.length - 1) {
                    y++;
                    i = -1; //reset the row index to the first char in the row
                }
                if (y == craftingSize) {
                    return false;
                }
            }
        }
        //match the whole shape
        for (int y = yOffset; y < craftingSize; y++) {
            if (y - yOffset >= rows.length) {
                break;
            }
            String row = rows[y - yOffset];
            for (int x = xOffset; x < craftingSize; x++) {
                char c;
                if (x - xOffset >= row.length()) {
                    c = ' ';
                } else {
                    c = row.charAt(x - xOffset);
                }
                ItemStack currentItem = matrix[y * craftingSize + x];
                if ((c == ' ' && (currentItem == null || currentItem.getType() == Material.AIR)) || (currentItem != null && currentItem.getType() == ingredients.get(c).getType())) {
                    IngredientValidator validator = validators.get(c);
                    if (!useValidators || validator == null || validator.validate(currentItem)) {
                        continue;
                    } else {
                        return false;
                    }
                }
                return false; //doesn't match
            }
        }
        return true;
    }

    @Override
    public ItemStack getResult(ItemStack[] matrix) {
        ItemStack item = resultBuilder.getResult(matrix);
        return (item == null || item.getType() == Material.AIR) ? new ItemStack(Material.AIR) : item;
    }

    @Override
    public int[] getCostMatrix(ItemStack[] matrix) {
        return resultBuilder.generateCostMatrix(matrix);
    }

    @Override
    public ShapedAdvancedRecipe shape(final String... shape) {
        super.shape(shape);
        Validate.notNull(shape, "Must provide a shape");
        Validate.isTrue(shape.length > 0 && shape.length < 4, "Crafting recipes should be 1, 2, 3 rows, not ", shape.length);

        for (String row : shape) {
            Validate.notNull(row, "Shape cannot have null rows");
            Validate.isTrue(row.length() > 0 && row.length() < 4, "Crafting rows should be 1, 2, or 3 characters, not ", row.length());
        }
        this.rows = new String[shape.length];
        System.arraycopy(shape, 0, this.rows, 0, shape.length);
        HashMap<Character, IngredientValidator> newValidators = new HashMap<>();
        for (String row : shape) {
            for (Character c : row.toCharArray()) {
                newValidators.put(c, validators.get(c));
            }
        }
        this.validators = newValidators;
        return this;
    }

    @Override
    public boolean validateMaterix(ItemStack[] matrix) {
        return _validate(matrix, false);
    }  
    
    //<editor-fold defaultstate="collapsed" desc="Chaining Methods">
    @Override
    public ShapedAdvancedRecipe setIngredient(char key, Material ingredient) {
        return (ShapedAdvancedRecipe) super.setIngredient(key, ingredient); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ShapedAdvancedRecipe setIngredient(char character, Material ingredient, IngredientValidator validator) {
        setIngredient(character, ingredient);
        validators.put(character, validator);
        return this;
    }
    
    @Override
    public ShapedAdvancedRecipe setIngredient(char key, Material ingredient, int raw) {
        return (ShapedAdvancedRecipe) super.setIngredient(key, ingredient, raw); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ShapedAdvancedRecipe setIngredient(char key, Material ingredient, int raw, IngredientValidator validator) {
        setIngredient(key, ingredient, raw);
        validators.put(key, validator);
        return this;
    }
    
    @Override
    public ShapedAdvancedRecipe setIngredient(char key, MaterialData ingredient) {
        return (ShapedAdvancedRecipe) super.setIngredient(key, ingredient); //To change body of generated methods, choose Tools | Templates.
    }
    
    public ShapedAdvancedRecipe setIngredient(char key, MaterialData ingredient, IngredientValidator validator) {
        setIngredient(key, ingredient);
        validators.put(key, validator);
        return this;
    }
//</editor-fold>
    
}
