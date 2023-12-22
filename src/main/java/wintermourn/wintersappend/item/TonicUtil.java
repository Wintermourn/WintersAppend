package wintermourn.wintersappend.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.IntFunction;

public class TonicUtil {
    public static final String TONIC_EFFECTS_KEY = "TonicEffects";
    public static final String TONIC_EFFECTS_ID_KEY = "EffectIds";
    public static final String TONIC_KEY = "Tonic";
    private static final int DEFAULT_COLOR = 16253176;
    private static final Map<StatusEffect, Integer> TONIC_COLORS = new HashMap<>();
    private static final Map<StatusEffect, IntFunction<Text>> EFFECT_TEXT = new HashMap<>();
    private static final Map<StatusEffect, String> TONIC_NAMES = new HashMap<>();
    private static final Text NONE_TEXT;

    public static List<ItemStack> registeredItems = new ArrayList<>();

//    static Identifier EMPTY = new Identifier(WintersAppend.MOD_ID, "empty");
//    public static final SimpleDefaultedRegistry<Object> TONICS = FabricRegistryBuilder.createDefaulted(
//            RegistryKey.ofRegistry(new Identifier(WintersAppend.MOD_ID, "tonic")), EMPTY).buildAndRegister();

    public static void registerColor(StatusEffect effect, int color)
    {
        TONIC_COLORS.put(effect, color);
    }
    public static void registerName(StatusEffect effect, String translationKey)
    {
        TONIC_NAMES.put(effect, translationKey);
    }
    public static void registerText(StatusEffect effect, IntFunction<Text> text)
    {
        EFFECT_TEXT.put(effect, text);
    }

    public static ItemStack getStack(List<StatusEffect> effects)
    {
        return getStack(effects.toArray(new StatusEffect[0]));
    }
    public static ItemStack getStack(StatusEffect... effects)
    {
        return getStack((TonicItem) AppendItems.TONIC, effects);
    }
    public static ItemStack getStack(TonicItem item, StatusEffect... effects)
    {
        ItemStack tonic = new ItemStack(item);
        NbtCompound nbt = tonic.getOrCreateNbt();
        NbtList effectNames = new NbtList();
        NbtList effectIds = new NbtList();

        List<String> effectNamesForSort = new ArrayList<>();
        for (StatusEffect effect : effects) {
            Identifier id = Registries.STATUS_EFFECT.getId(effect);
            if (id == null) continue;

            String textId = id.toString();
            effectNamesForSort.add(textId);
        }
        Collections.sort(effectNamesForSort);

        int currentId = -1;
        for (String s : effectNamesForSort) {
            NbtString ns = NbtString.of(s);
            if (!effectNames.contains(ns))
            {
                currentId++;
                effectNames.add(ns);
            }
            effectIds.add(NbtInt.of(currentId));
        }
        nbt.put(TONIC_EFFECTS_KEY, effectNames);
        nbt.put(TONIC_EFFECTS_ID_KEY, effectIds);

        return tonic;
    }


//    public static ItemStack setTonic(ItemStack stack, Tonic tonic)
//    {
//        Identifier id = TONICS.getId(tonic);
//
//        if (id == EMPTY)
//        {
//            stack.removeSubNbt(TONIC_EFFECTS_KEY);
//            stack.removeSubNbt(TONIC_EFFECTS_ID_KEY);
//        } else
//        {
//            ImmutableList<StatusEffect> statuses = tonic.getEffects();
//            NbtList names = new NbtList();
//            NbtList ids = new NbtList();
//
//            ge
//            stack.getOrCreateNbt().put(TONIC_EFFECTS_KEY, new NbtList());
//        }
//    }


    public static int getColor(ItemStack tonic)
    {
        NbtCompound itemNbt = tonic.getOrCreateNbt();
        if (!(tonic.getItem() instanceof TonicItem)) return 0x385DC6;
        if (!itemNbt.contains(TONIC_EFFECTS_KEY)) return DEFAULT_COLOR;

        Color finalColor = new Color(0x385DC6);

        NbtList list = itemNbt.getList(TONIC_EFFECTS_KEY, NbtElement.STRING_TYPE);
        NbtList ids = itemNbt.getList(TONIC_EFFECTS_ID_KEY, NbtElement.INT_TYPE);

        for (int i = 0; i < ids.size(); i++) {
            int effectId = ids.getInt(i);

            String effect = list.getString(effectId);
            Identifier id = new Identifier(effect);
            if (Registries.STATUS_EFFECT.containsId(id))
            {
                StatusEffect status = Registries.STATUS_EFFECT.get(id);
                assert status != null;

                Color effectColor;

                if (TONIC_COLORS.containsKey(status))
                    effectColor = new Color(TONIC_COLORS.get(status));
                else
                    effectColor = new Color(status.getColor());
                finalColor = new Color(
                        (finalColor.getRed() + effectColor.getRed()) / 2,
                        (finalColor.getGreen() + effectColor.getGreen()) / 2,
                        (finalColor.getBlue() + effectColor.getBlue()) / 2
                );
            }
        }

        return finalColor.getRGB();
    }
    public static int[] getColorRGB(ItemStack tonic)
    {
        NbtCompound itemNbt = tonic.getOrCreateNbt();
        if (!(tonic.getItem() instanceof TonicItem)) return new int[]{0x38, 0x5D, 0xC6};
        if (!itemNbt.contains(TONIC_EFFECTS_KEY)) return new int[]{(DEFAULT_COLOR >> 16) & 255,(DEFAULT_COLOR >> 8) & 255, DEFAULT_COLOR & 255};

        Color finalColor = new Color(0x385DC6);

        NbtList list = itemNbt.getList(TONIC_EFFECTS_KEY, NbtElement.STRING_TYPE);
        NbtList ids = itemNbt.getList(TONIC_EFFECTS_ID_KEY, NbtElement.INT_TYPE);

        for (int i = 0; i < ids.size(); i++) {
            int effectId = ids.getInt(i);

            String effect = list.getString(effectId);
            Identifier id = new Identifier(effect);
            if (Registries.STATUS_EFFECT.containsId(id))
            {
                StatusEffect status = Registries.STATUS_EFFECT.get(id);
                assert status != null;

                Color effectColor;

                if (TONIC_COLORS.containsKey(status))
                    effectColor = new Color(TONIC_COLORS.get(status));
                else
                    effectColor = new Color(status.getColor());
                finalColor = new Color(
                        (finalColor.getRed() + effectColor.getRed()) / 2,
                        (finalColor.getGreen() + effectColor.getGreen()) / 2,
                        (finalColor.getBlue() + effectColor.getBlue()) / 2
                );
            }
        }

        return new int[]{finalColor.getRed(),finalColor.getGreen(),finalColor.getBlue()};
    }

    public static List<StatusEffectInstance> getTonicEffectInstances(ItemStack tonic)
    {
        return getTonicEffectInstances(tonic.getOrCreateNbt());
    }

    public static List<StatusEffectInstance> getTonicEffectInstances(NbtCompound nbt)
    {
        Map<StatusEffect, Integer> instances = new HashMap<>();

        NbtList list = nbt.getList(TONIC_EFFECTS_KEY, NbtElement.STRING_TYPE);
        NbtList ids = nbt.getList(TONIC_EFFECTS_ID_KEY, NbtElement.INT_TYPE);
//        Map<Integer, Integer> usedEffects = new HashMap<>();

        for (int i = 0; i < ids.size(); i++) {
            int effectId = ids.getInt(i);

            String effect = list.getString(effectId);
            //WintersAppend.LOGGER.info(effect);

            Identifier id = new Identifier(effect);
            if (Registries.STATUS_EFFECT.containsId(id))
            {
                StatusEffect status = Registries.STATUS_EFFECT.get(id);
                assert status != null;

                if (instances.containsKey(status))
                {
                    instances.put(status, instances.get(status) + 1);
                } else
                    instances.put(status, 0);

//                if (usedEffects.containsKey(effectId))
//                    usedEffects.put(effectId, usedEffects.get(effectId)+1);
//                else
//                    usedEffects.put(effectId, 1);
            }
        }

//        List<String> newEffectsList = new ArrayList<>(usedEffects.size());
//        List<Integer> newIdsList = new ArrayList<>(usedEffects.size());
//        NbtList effectNbt = new NbtList();
//
//        usedEffects.forEach((id, a) -> {
//            for (int i = 0; i < a; i++) {
//                newEffectsList.add(list.getString(id));
//            }
//        });
//
//        Collections.sort(newEffectsList);
//
//        for (String s : newEffectsList) {
//            effectNbt.add(NbtString.of(s));
//        }
//
//        if (effectNbt != list)
//        {
//            NbtList effectIdNbt = new NbtList();
//            usedEffects.forEach((id, a) -> {
//                newIdsList.add(newEffectsList.indexOf(list.getString(id)));
//            });
//            Collections.sort(newIdsList);
//
//            for (Integer i : newIdsList) {
//                effectIdNbt.add(NbtInt.of(i));
//            }
//
//            nbt.put(TONIC_EFFECTS_KEY, effectNbt);
//            nbt.put(TONIC_EFFECTS_ID_KEY, effectIdNbt);
//        }

        List<StatusEffectInstance> finalEffects = new ArrayList<>();

        for (Map.Entry<StatusEffect, Integer> entry : instances.entrySet()) {
            finalEffects.add(new StatusEffectInstance(entry.getKey(), 24000, entry.getValue()));
        }

        return finalEffects;
    }

    public static Map<StatusEffect, Integer> getTonicEffects(ItemStack tonic)
    {
        return getTonicEffects(tonic.getOrCreateNbt());
    }

    public static Map<StatusEffect, Integer> getTonicEffects(NbtCompound nbt)
    {
        Map<StatusEffect, Integer> effects = new HashMap<>();

        NbtList list = nbt.getList(TONIC_EFFECTS_KEY, NbtElement.STRING_TYPE);
        NbtList ids = nbt.getList(TONIC_EFFECTS_ID_KEY, NbtElement.INT_TYPE);

        for (int i = 0; i < ids.size(); i++) {
            int effectId = ids.getInt(i);

            String effect = list.getString(effectId);
            //WintersAppend.LOGGER.info(effect);

            Identifier id = new Identifier(effect);
            if (Registries.STATUS_EFFECT.containsId(id))
            {
                StatusEffect status = Registries.STATUS_EFFECT.get(id);
                assert status != null;

                if (effects.containsKey(status))
                {
                    effects.put(status, effects.get(status) + 1);
                } else
                    effects.put(status, 0);
            }
        }

        return effects;
    }

    public static List<StatusEffect> getTonicEffectsList(ItemStack tonic)
    {
        return getTonicEffectsList(tonic.getOrCreateNbt());
    }

    public static List<StatusEffect> getTonicEffectsList(NbtCompound nbt)
    {
        List<StatusEffect> effects = new ArrayList<>();

        NbtList list = nbt.getList(TONIC_EFFECTS_KEY, NbtElement.STRING_TYPE);
        NbtList ids = nbt.getList(TONIC_EFFECTS_ID_KEY, NbtElement.INT_TYPE);

        for (int i = 0; i < ids.size(); i++) {
            int effectId = ids.getInt(i);

            String effect = list.getString(effectId);
            //WintersAppend.LOGGER.info(effect);

            Identifier id = new Identifier(effect);
            if (Registries.STATUS_EFFECT.containsId(id))
            {
                StatusEffect status = Registries.STATUS_EFFECT.get(id);
                assert status != null;

                effects.add(status);
            }
        }

        return effects;
    }

    @Nullable
    public static Pair<StatusEffect, Boolean> getPrimaryEffect(ItemStack tonic)
    {
        return getPrimaryEffect(tonic.getOrCreateNbt());
    }

    @Nullable
    public static Pair<StatusEffect, Boolean> getPrimaryEffect(NbtCompound nbt)
    {
        Map<StatusEffect, Integer> effects = getTonicEffects(nbt);
        Map.Entry<StatusEffect, Integer> highestEntry = null;
        boolean multiple = false;

        for (Map.Entry<StatusEffect, Integer> entry : effects.entrySet()) {
            if (highestEntry == null || highestEntry.getValue() < entry.getValue())
            {
                highestEntry = entry;
                multiple = false;
            } else
                multiple = true;
        }

        return highestEntry != null ? new Pair<>(highestEntry.getKey(), multiple) : new Pair<>(null, false);
    }

    public static Text getName(ItemStack tonic)
    {
        Pair<StatusEffect, Boolean> primaryEffect = getPrimaryEffect(tonic);

        if (primaryEffect == null || primaryEffect.getFirst() == null) return Text.translatable("tonic.name.none");
        if (primaryEffect.getSecond()) return Text.translatable("tonic.mixture");
        if (TONIC_NAMES.containsKey(primaryEffect.getFirst())) return Text.translatable(TONIC_NAMES.get(primaryEffect.getFirst()));
        return Text.translatable("tonic.name.generic", primaryEffect.getFirst().getName());
    }

    public static int getEffectsCount(ItemStack tonic)
    {
        return getEffectsCount(tonic.getOrCreateNbt());
    }

    public static int getEffectsCount(NbtCompound nbt)
    {
        NbtList list = nbt.getList(TONIC_EFFECTS_ID_KEY, NbtElement.INT_TYPE);

        return list.size();
    }

    public static void buildTooltip(ItemStack stack, List<Text> tooltip, double durationMultiplier)
    {

        if (stack.getItem() instanceof TonicItem tonic)
        {
            int effectCount = getEffectsCount(stack);
            int maxEffects = tonic.getMaxEffects();
            buildTooltip(getTonicEffects(stack), tooltip, durationMultiplier, maxEffects - effectCount);

//            tooltip.add(ScreenTexts.SPACE);
//            if (effectCount < maxEffects)
//                tooltip.add(Text.translatable("tonic.fillLevel", TonicUtil.getEffectsCount(stack), maxEffects).formatted(Formatting.DARK_GRAY));
//            else
//                tooltip.add(Text.translatable("tonic.full").formatted(Formatting.DARK_GRAY));
        } else
        {
            buildTooltip(getTonicEffects(stack), tooltip, durationMultiplier, 0);
        }
    }

    public static void buildTooltip(Map<StatusEffect, Integer> effects, List<Text> tooltip, double durationMultiplier, int additionalSlots)
    {

        Iterator<Map.Entry<StatusEffect, Integer>> effectIterator;
        Iterator<Pair<EntityAttribute, EntityAttributeModifier>> attributeIterator;

        List<Pair<EntityAttribute, EntityAttributeModifier>> attributeModifiers = new ArrayList<>();
        StatusEffect effect;
        MutableText text;
        if (effects.isEmpty())
            if (additionalSlots > 0)
                for (int i = 0; i < additionalSlots; i++)
                {
                    tooltip.add(Text.translatable("tonic.none").formatted(Formatting.DARK_GRAY));
                }
            else
                tooltip.add(NONE_TEXT);
        else
        {
            for (effectIterator = effects.entrySet().iterator(); effectIterator.hasNext();)
            {
                Map.Entry<StatusEffect, Integer> effectInstance = effectIterator.next();

                text = Text.translatable(effectInstance.getKey().getTranslationKey());
                effect = effectInstance.getKey();
                Map<EntityAttribute, EntityAttributeModifier> attributes = effect.getAttributeModifiers();

                if (!attributes.isEmpty()) {

                    for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : attributes.entrySet()) {
                        EntityAttributeModifier modifier = entry.getValue();
                        EntityAttributeModifier copiedModifier = new EntityAttributeModifier(modifier.getName(), effect.adjustModifierAmount(effectInstance.getValue(), modifier), modifier.getOperation());
                        attributeModifiers.add(new Pair<>(entry.getKey(), copiedModifier));
                    }
                }

                if (effectInstance.getValue() > 0) {
                    text = Text.translatable("tonic.withAmplifier", text, Text.translatable("potion.potency." + effectInstance.getValue()));
                }

                tooltip.add(Text.translatable("tonic.effect_entry", text.formatted(effect.getCategory().getFormatting())));

                if (EFFECT_TEXT.containsKey(effect))
                {
                    tooltip.add(((MutableText)EFFECT_TEXT.get(effect).apply(effectInstance.getValue())).formatted(Formatting.GRAY));
                }
            }

            for (int i = 0; i < additionalSlots; i++)
            {
                tooltip.add(Text.translatable("tonic.none").formatted(Formatting.DARK_GRAY));
            }
        }

        if (!attributeModifiers.isEmpty())
        {
            tooltip.add(ScreenTexts.SPACE);
            tooltip.add(Text.translatable("potion.whenDrank").formatted(Formatting.GOLD));

            attributeIterator = attributeModifiers.iterator();

            while (attributeIterator.hasNext())
            {
                Pair<EntityAttribute, EntityAttributeModifier> attribute = attributeIterator.next();
                EntityAttributeModifier modifier = attribute.getSecond();

                double value = modifier.getValue();

                if (modifier.getOperation() != EntityAttributeModifier.Operation.ADDITION)
                    value *= 100;

                if (value > 0.0) {
                    tooltip.add(Text.translatable("attribute.modifier.plus." + modifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(value), Text.translatable((attribute.getFirst()).getTranslationKey())).formatted(Formatting.BLUE));
                } else if (value < 0.0) {
                    value *= -1.0;
                    tooltip.add(Text.translatable("attribute.modifier.take." + modifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(value), Text.translatable((attribute.getFirst()).getTranslationKey())).formatted(Formatting.RED));
                }
            }
        }
    }

    public static void registerCreativeItems(ItemGroup.Entries entries, StatusEffect effect)
    {
        registerCreativeItems(entries, effect, 3);
    }
    public static void registerCreativeItems(ItemGroup.Entries entries, StatusEffect effect, int maxLevel)
    {
        List<StatusEffect> currentDepth = new ArrayList<>();
        for (int i = 0; i < maxLevel; i++) {
            currentDepth.add(effect);

            ItemStack thisStack = getStack(currentDepth);
            entries.add(thisStack);
            registeredItems.add(thisStack);
        }
    }

    static
    {
        NONE_TEXT = Text.translatable("tonic.none").formatted(Formatting.GRAY);

        registerText(StatusEffects.ABSORPTION, i -> Text.translatable("tonic.effect.absorption", (i+1) * 2));
    }
}
