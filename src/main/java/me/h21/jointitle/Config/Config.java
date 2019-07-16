package me.h21.jointitle.Config;

import me.h21.jointitle.JTMain;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;

public class Config {

    private static CommentedConfigurationNode config;

    public static void buildConfig(ConfigurationLoader<CommentedConfigurationNode> loader, File ConfigFile){
        if (!ConfigFile.exists()){
            try{
                ConfigFile.createNewFile();
                config = loader.load();
                setupConfig();
                loader.save(config);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            try{
                config = loader.load();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void setupConfig(){

        //default config

        config.getNode("Default").getNode("Text").setValue("#title:title#subtitle:subtitle#actionbar:actionbar#").setComment("Your text need to be between :and#. For ex title:Welcome to my server#");

        config.getNode("Default").getNode("Staytime").setValue(202).setComment("Sets the duration in ticks how long the title should stay on. Default 202ticks = 10sec.");
        config.getNode("Default").getNode("FadeIn").setValue(101).setComment("Sets the duration in ticks of the fade in effect of the title. Default 101ticks = 5sec.");
        config.getNode("Default").getNode("FadeOut").setValue(101).setComment("Sets the duration in ticks of the fade out effect of the title. Default 101ticks = 5sec.");

        //vip config

        config.getNode("Vip").getNode("Text").setValue("#title:title#subtitle:subtitle#actionbar:actionbar#").setComment("Your text need to be between :and#. For ex title:Welcome to my server#");

        config.getNode("Vip").getNode("Staytime").setValue(202).setComment("Sets the duration in ticks how long the title should stay on. Default 202ticks = 10sec.");
        config.getNode("Vip").getNode("FadeIn").setValue(101).setComment("Sets the duration in ticks of the fade in effect of the title. Default 101ticks = 5sec.");
        config.getNode("Vip").getNode("FadeOut").setValue(101).setComment("Sets the duration in ticks of the fade out effect of the title. Default 101ticks = 5sec.");

        //admin config

        config.getNode("Admin").getNode("Text").setValue("#title:title#subtitle:subtitle#actionbar:actionbar#").setComment("Your text need to be between :and#. For ex title:Welcome to my server#");

        config.getNode("Admin").getNode("Staytime").setValue(202).setComment("Sets the duration in ticks how long the title should stay on. Default 202ticks = 10sec.");
        config.getNode("Admin").getNode("FadeIn").setValue(101).setComment("Sets the duration in ticks of the fade in effect of the title. Default 101ticks = 5sec.");
        config.getNode("Admin").getNode("FadeOut").setValue(101).setComment("Sets the duration in ticks of the fade out effect of the title. Default 101ticks = 5sec.");

    }

    /**String getters**/

    public static String getDefaultSampel(){

        return config.getNode("Default").getNode("Text").getString();
    }

    public static String getVipSampel(){

        return config.getNode("Vip").getNode("Text").getString();
    }

    public static String getAdminSampel(){

        return config.getNode("Admin").getNode("Text").getString();
    }

    /**Config utils**/

    public static CommentedConfigurationNode getConfig(){

        return config;
    }

    public static void reloadConfig() {

        try {
            config = JTMain.getInstance().getLoader().load();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
