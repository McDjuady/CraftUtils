/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlemail.mcdjuady.craftutils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Max
 */
public class CraftUtils extends JavaPlugin {

    public static CustomRecipeManager getRecipeManager() {
        return CustomRecipeManager.getInstance();
    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new CraftingListener(), this);
    }
    
}
