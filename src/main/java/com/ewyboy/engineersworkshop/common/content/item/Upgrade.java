package com.ewyboy.engineersworkshop.common.content.item;

import com.ewyboy.engineersworkshop.EngineersWorkshop;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.EnumSet;
import java.util.List;

public enum Upgrade {

    BLANK(new MaxCount(0), ParentType.NULL),
    AUTO_CRAFTER(new MaxCount(1), ParentType.CRAFTING),
    STORAGE(new MaxCount(1), ParentType.CRAFTING),
    CHARGED(new ConfigurableMax(8), ParentType.MachineSet),
    SPEED(new ConfigurableMax(8), ParentType.MachineSet),
    QUEUE(new MaxCount(3), EnumSet.of(ParentType.SMELTING)),
    AUTO_TRANSFER(new MaxCount(1), ParentType.GLOBAL),
    FILTER(new MaxCount(1), ParentType.GLOBAL),
    TRANSFER(new ConfigurableMax(6, 20), ParentType.GLOBAL);

    private String name;
    private String description;
    private MaxCount maxCount;
    private EnumSet<ParentType> validParents;
    private Upgrade dep;
    private UpgradeItem item;

    Upgrade(MaxCount maxCount, EnumSet<ParentType> validParents, Upgrade dep) {
        this.validParents = validParents;
        this.name = toString().toLowerCase();
        this.description = String.format(EngineersWorkshop.MOD_ID + ":" + "item" + "." + "%s" + "." + "description", name);
        this.maxCount = maxCount;
        maxCount.init(this);
        this.dep = dep;
    }

    Upgrade(MaxCount maxCount, EnumSet<ParentType> validParents) {
        this(maxCount, validParents, null);
    }

    Upgrade(MaxCount maxCount, ParentType type) {
        this(maxCount, type == null ? EnumSet.of(ParentType.NULL) : EnumSet.of((type)), null);
    }

    Upgrade(MaxCount maxCount, ParentType type, Upgrade dep) {
        this(maxCount, type == null ? EnumSet.of(ParentType.NULL) : EnumSet.of((type)), dep);
    }


    public Upgrade getDependency() {
        return dep;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return maxCount.getConfigurableMax() == 0 || maxCount.getMax() > 0;
    }

    public ItemStack getItemStack() {
        return new ItemStack(item, 1);
    }

    public Item.Properties createSettings(){
        return new Item.Properties().group(EngineersWorkshop.contentGroup);
    }

    public void addInfo(List<ITextComponent> toolTips) {
        toolTips.add(new StringTextComponent(description));

        toolTips.add(new StringTextComponent("<hold shift for stack info>"));
        toolTips.add(new StringTextComponent("<hold control for parent info>"));


        /*if (Gui.isShiftPressed()) {
            if (getMaxCount() == 1)
                info.add(new TranslatableTextComponent("engineersworkshop:item.unstackable"));
            else if (getMaxCount() > 1)
                info.add(new TranslatableTextComponent("engineersworkshop:item.stackable", getMaxCount()));
            info.addAll(validParents.stream().map(validParent -> new TranslatableTextComponent(validParent.description)).collect(Collectors.toList()));
        }*/

        /*if (Gui.isControlPressed()) {
            if (validParents.size() > 0) {
                info.add(new StringTextComponent("Parents: "));
                info.addAll(validParents.stream().map(ParentType::name).map((Function<String, TextComponent>) s -> new TranslatableTextComponent(s)).collect(Collectors.toList()));
            }
        }*/
    }

    public boolean isValid(
            ItemStack parent) {
        for (ParentType validParent : validParents) {
            if (validParent.isValidParent(parent)) {
                return true;
            }
        }
        return false;
    }

    public int getMaxCount() {
        return maxCount.getMax();
    }

    public MaxCount getMaxCountObject() {
        return maxCount;
    }

    public enum ParentType {
        CRAFTING("Works with Crafting Tables") {
            @Override
            public boolean isValidParent(
                    ItemStack item) {
                if (!item.isEmpty()) {
                    return item.getItem() == Items.CRAFTING_TABLE;
                }
                return false;
            }
        },
        SMELTING("Works with Furnaces") {
            @Override
            public boolean isValidParent(
                    ItemStack item) {
                if (!item.isEmpty()) {
                    if (!item.isEmpty()) {
                        return item.getItem() == Item.getItemFromBlock(Blocks.FURNACE);
                    }
                }

                return false;
            }
        },
        STORAGE("Works with Chests") {
            @Override
            public boolean isValidParent(
                    ItemStack item) {
                if (!item.isEmpty()) {
                    return item.getItem() == Item.getItemFromBlock(Blocks.CHEST);
                }
                return false;
            }

        },

        GLOBAL("Upgrades the entire Table") {
            @Override
            public boolean isValidParent(
                    ItemStack item) {
                return !item.isEmpty();
            }
        },
        NULL("you shouldn't be seeing this.") {
            @Override
            public boolean isValidParent(
                    ItemStack item) {
                return false;
            }
        };

        private static final EnumSet<ParentType> MachineSet = EnumSet.of(ParentType.CRAFTING, ParentType.SMELTING);
        private String description;

        ParentType(String description) {
            this.description = description;
        }

        public abstract boolean isValidParent(
                ItemStack item);
    }

    public static class MaxCount {
        private int max;
        private int defaultMax;

        public MaxCount(int max) {
            this.max = max;
            this.defaultMax = max;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int value) {
            this.max = value;
        }

        public int getConfigurableMax() {
            return defaultMax;
        }

        public void init(Upgrade upgrade) {}
    }

    private static class ConfigurableMax extends MaxCount {
        private static final int GLOBAL_MAX_COUNT = 8 * 64;
        private static final int MAX_COUNT = 7 * 64;
        private boolean isGlobal;
        private int configurableMax;

        private ConfigurableMax(int max, int configurableMax) {
            super(max);
            this.configurableMax = configurableMax;
        }

        private ConfigurableMax(int max) {
            this(max, -1);
        }

        @Override
        public int getConfigurableMax() {
            return configurableMax != -1 ? configurableMax : isGlobal ? GLOBAL_MAX_COUNT : MAX_COUNT;
        }

        @Override
        public void init(Upgrade upgrade) {
            isGlobal = upgrade.validParents.contains(ParentType.GLOBAL);
        }
    }

}
