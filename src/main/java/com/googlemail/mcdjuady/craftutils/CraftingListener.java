/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.craftutils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.googlemail.mcdjuady.craftutils.util.Util;
import com.googlemail.mcdjuady.craftutils.recipes.AdvancedRecipe;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Max
 */
public class CraftingListener implements Listener {

    @EventHandler
    public void onCraftingPrepare(PrepareItemCraftEvent e) {
        if (CraftUtils.getRecipeManager().hasRecipe(e.getRecipe().getResult())) {
            List<AdvancedRecipe> recipes = CraftUtils.getRecipeManager().getRecipes(e.getRecipe().getResult(), e.getInventory().getMatrix());
            if (recipes.isEmpty()) {
                return;
            }
            for (AdvancedRecipe recipe : recipes) {
                //Check if the recipe is valid (can't compare recipes)
                if (recipe.validate(e.getInventory().getMatrix())) {
                    //Set the result
                    e.getInventory().setResult(recipe.getResult(e.getInventory().getMatrix()));
                    return;
                }
            }
            //Clear the result if we don't find a valid recipe
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (CraftUtils.getRecipeManager().hasRecipe(e.getRecipe().getResult())) {
            List<AdvancedRecipe> recipes = CraftUtils.getRecipeManager().getRecipes(e.getRecipe().getResult(), e.getInventory().getMatrix());
            if (recipes.isEmpty()) {
                return;
            }
            AdvancedRecipe finalRecipe = null;
            //find our recipe (if we have one)
            ItemStack[] matrix = e.getInventory().getMatrix();
            for (AdvancedRecipe recipe : recipes) {
                if (recipe.validate(matrix)) {
                    finalRecipe = recipe;
                    break;
                }
            }

            if (finalRecipe == null) {
                return;
            }
            //cancel the event so we can do manual crafting            
            ItemStack result = finalRecipe.getResult(matrix);
            int maxStackSize = result.getMaxStackSize();
            int[] costMatrix = finalRecipe.getCostMatrix(matrix);
            if (costMatrix.length != matrix.length) {
                throw new IndexOutOfBoundsException("The costMatrix isn't the same size as the craftingMatrix!");
            }
            e.setCancelled(true);
            if (e.isShiftClick()) {
                //find the ammount we can craft
                int craftable = 64;
                for (int i = 0; i < matrix.length; i++) {
                    ItemStack item = matrix[i];
                    if (item == null || item.getType() == Material.AIR || costMatrix[i] == 0) {
                        continue;
                    }
                    craftable = Math.min(craftable, item.getAmount() / costMatrix[i]);
                }
                //find the free slots
                int freeSlots = 0;
                for (ItemStack item : e.getView().getPlayer().getInventory()) {
                    if (item == null || item.getType() == Material.AIR) {
                        freeSlots += maxStackSize;
                        continue;
                    }
                    if (item.isSimilar(result)) {
                        freeSlots += (maxStackSize - item.getAmount());
                    }
                }
                freeSlots /= result.getAmount(); //divde by the size of the result stack
                int ammountToCraft = Math.min(craftable, freeSlots);
                result.setAmount(result.getAmount() * ammountToCraft);
                e.getView().getPlayer().getInventory().addItem(result); //Add the items to the players inventory
                Util.decreaseItems(matrix, ammountToCraft, costMatrix);
            } else {
                if (e.getCursor() != null && e.getCursor().getType() != Material.AIR && !e.getCursor().isSimilar(result) && e.getCursor().getAmount() + result.getAmount() <= maxStackSize) {
                    return; //cursor not empty, can't leftclick
                }
                ItemStack mouse = e.getCursor();
                if (mouse == null || mouse.getType() == Material.AIR) {
                    mouse = result;
                } else {
                    mouse.setAmount(mouse.getAmount() + result.getAmount());
                }
                Util.decreaseItems(matrix, 1, costMatrix);
                e.getView().setCursor(mouse);
                //Result item dissapears
                //Send a packet to update this slot manually? 
            }
            e.getInventory().setMatrix(matrix);
            if (!finalRecipe.validate(matrix)) {
                e.getInventory().setResult(null);
            } else {
                /*
                 * Updating a CraftingInventory resets the result slot on the Client
                 * Since the server, for some reason, sends the matrix update the 
                 * tick after the event, we have to update the result 2 ticks after the event.
                 * This is the only way i have found to achive consistent results
                 */
                if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
                    new ProtocolLibDelayedResultUpdate(e.getInventory(), finalRecipe).runTaskLater(CraftUtils.getPlugin(CraftUtils.class), 2);
                } else {
                    new DelayedResultUpdate(e.getInventory(), finalRecipe).runTaskLater(CraftUtils.getPlugin(CraftUtils.class), 2);
                }
            }
        }
    }

    private class DelayedResultUpdate extends BukkitRunnable {

        private final CraftingInventory inv;
        private final AdvancedRecipe recipe;

        public DelayedResultUpdate(CraftingInventory inventory, AdvancedRecipe recipe) {
            inv = inventory;
            this.recipe = recipe;
        }

        @Override
        public void run() {
            List<HumanEntity> viewers = inv.getViewers();
            if (inv.getType() == InventoryType.CRAFTING) {
                viewers.add((HumanEntity) inv.getHolder());
            }
            if (viewers == null || viewers.isEmpty()) {
                return;
            }
            inv.setResult(recipe.getResult(inv.getMatrix()));
            for (HumanEntity e : inv.getViewers()) {
                if (e instanceof Player) {
                    ((Player) e).updateInventory();
                }
            }
        }

    }

    private class ProtocolLibDelayedResultUpdate extends BukkitRunnable {

        private final CraftingInventory inv;
        private final AdvancedRecipe recipe;

        public ProtocolLibDelayedResultUpdate(CraftingInventory inventory, AdvancedRecipe recipe) {
            inv = inventory;
            this.recipe = recipe;
        }

        @Override
        public void run() {
            List<HumanEntity> viewers = inv.getViewers();
            if (inv.getType() == InventoryType.CRAFTING) {
                viewers.add((HumanEntity) inv.getHolder());
            }
            if (viewers == null || viewers.isEmpty()) {
                return;
            }
            ItemStack result = recipe.getResult(inv.getMatrix());
            inv.setResult(result);
            for (HumanEntity e : inv.getViewers()) {
                if (e instanceof Player) {
                    try {
                        //Find the current window id
                        Method m = e.getClass().getMethod("getHandle");
                        Object playerEntity = m.invoke(e);
                        final Field containerField = playerEntity.getClass().getField("activeContainer");
                        Object activeContainer = containerField.get(playerEntity);
                        final Field windowIdField = activeContainer.getClass().getField("windowId");
                        int windowId = windowIdField.getInt(activeContainer);
                        int slot = 0;
                        //Build the packet
                        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SET_SLOT);
                        packet.getIntegers().write(0, windowId).write(1, slot);
                        packet.getItemModifier().write(0, result);
                        ProtocolLibrary.getProtocolManager().sendServerPacket((Player) e, packet);
                    } catch (InvocationTargetException ex) {
                        Bukkit.getLogger().info(ex.getMessage());
                    } catch (NoSuchMethodException ex) {
                        Bukkit.getLogger().info("An internal method is out of date. Please contact the developer");
                    } catch (SecurityException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException ex) {
                        Bukkit.getLogger().info("An internal Field Name has changed. Please contact the developer");
                    }
                }
            }
        }

    }

}
