package com.ewyboy.engineersworkshop;

import com.ewyboy.bibliotheca.common.loaders.ContentLoader;
import com.ewyboy.engineersworkshop.common.register.Register;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import static com.ewyboy.engineersworkshop.EngineersWorkshop.MOD_ID;

@Mod(MOD_ID)
public class EngineersWorkshop {

    public static final String MOD_ID = "engineersworkshop";

    public EngineersWorkshop() {
        ContentLoader.init(MOD_ID, contentGroup, Register.BLOCKS.class, Register.ITEMS.class, Register.TILES.class);
    }

    public static final ItemGroup contentGroup = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Register.BLOCKS.WORKSHOP);
        }
    };

}
