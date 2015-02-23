CraftUtils
==========

<What is CraftUtils?
--------------------

CraftUtils is a library to extend the functionality of custom recipes for Bukkit and Spigot.
It allows for custom recipe validation in Both the Shaped and Shapeless recipes.
This can be used to test for ItemMeta or other things and to change the result accordingly.

### Using CraftUtils

First you need to add the CraftUtils.jar to your Java build path. Then add CraftUtils as a 
dependecy (or softdependecy) to your plugin.yml

````yml
depend: [CraftUtils]
````

To create an AdvancedRecipe all you need to do is write the validators and register them
A simple ShapelessValidator could look something like this:

````java
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
        if (ingredients.size() != 2) {
            return false;
        }
        boolean valid = false;
        for (ItemStack item : ingredients) {
            valid = valid ^ item.hasItemMeta();
        }
        return valid;
    }

    @Override
    public Map<ItemStack, Integer> costMatrix(List<ItemStack> ingredients) {
        Map<ItemStack,Integer> costMap = new HashMap<>();
        for (ItemStack item : ingredients) {
            costMap.put(item, item.hasItemMeta() ? 0 : 1);
        }
        return costMap;
    }
    
}
````

This is a simple Validator used to clone the ItemMeta of one item onto another.
Now you need to create an AdvancedShapelessRecipe with this Validator.

````java
ShapelessAdvancedRecipe recipe = new ShapelessAdvancedRecipe(new ItemStack(Material.NAME_TAG,new CloneValidator()))
recipe.addIngredient(2,Material.NAME_TAG);
````

As you can see, adding and removing ingredients is exactly the same as with Bukkit recipes and can also be chained together.
Finally we need to add the recipe to the CustomRecipeManager

````java
CraftUtils.getRecipeManager().addRecipe(recipe);
````

This will also add the recipe to Bukkit's recipe list
With those simple steps you should now be able to clone Name Tags

### Example Projects

[EnderWarp](https://github.com/McDjuady/EnderWarp) - A Plugin using AdvancedShapelessRecipes to teleport to beacons
[EnderFly](https://github.com/McDjuady/EnderFly) - A Plugin using AdvancedShapedRecipes to allow flight in SMP
