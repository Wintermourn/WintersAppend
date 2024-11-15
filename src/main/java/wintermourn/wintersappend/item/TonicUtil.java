package wintermourn.wintersappend.item;

import com.demonwav.mcdev.annotations.Translatable;
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
    public static final String TONIC_EFFECTS_KEY = "effects";
    private static final int DEFAULT_COLOR = 16253176;
    private static final Map<StatusEffect, Integer> TONIC_COLORS = new HashMap<>();
    private static final Map<StatusEffect, IntFunction<Text>> EFFECT_TEXT = new HashMap<>();
    private static final Map<StatusEffect, String> TONIC_NAMES = new HashMap<>();
    private static final Text NONE_TEXT;
    private static final ItemStack TONIC_REP = new ItemStack(AppendItems.TONIC);


    public static List<ItemStack> registeredItems = new ArrayList<>();

//    static Identifier EMPTY = new Identifier(WintersAppend.MOD_ID, "empty");
//    public static final SimpleDefaultedRegistry<Object> TONICS = FabricRegistryBuilder.createDefaulted(
//            RegistryKey.ofRegistry(new Identifier(WintersAppend.MOD_ID, "tonic")), EMPTY).buildAndRegister();

    public static void registerColor(StatusEffect effect, int color)
    {
        TONIC_COLORS.put(effect, color);
    }
    public static void registerName(StatusEffect effect, @Translatable String translationKey)
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
    public static ItemStack getStack(List<StatusEffect> effects, int span)
    {
        ItemStack stack = getStack(effects.toArray(new StatusEffect[0]));
        TonicUtil.setEffectSpan(stack, span);
        return stack;
    }
    public static ItemStack getStack(TonicItem item, List<StatusEffect> effects)
    {
        return getStack(item, effects.toArray(new StatusEffect[0]));
    }
    public static ItemStack getStack(TonicItem item, List<StatusEffect> effects, int span)
    {
        ItemStack stack = getStack(item, effects.toArray(new StatusEffect[0]));
        TonicUtil.setEffectSpan(stack, span);
        return stack;
    }
    public static ItemStack getStack(StatusEffect... effects)
    {
        return getStack((TonicItem) AppendItems.TONIC, effects);
    }
    public static ItemStack getStack(TonicItem item, StatusEffect... effects)
    {
        ItemStack tonic = new ItemStack(item);
        NbtCompound nbt = tonic.getOrCreateNbt();
        NbtList effectNbt = new NbtList();

        Map<StatusEffect, Integer> effectData = new HashMap<>();
        for (StatusEffect effect : effects)
        {
            if (effectData.containsKey(effect))
            {
                effectData.put(effect, effectData.get(effect) + 1);
            } else
            {
                effectData.put(effect, 0);
            }
        }
        for (Map.Entry<StatusEffect, Integer> entry : effectData.entrySet().stream().sorted().toList())
        {
            Identifier id = Registries.STATUS_EFFECT.getId(entry.getKey());
            assert id != null;
            NbtList mini = new NbtList();
            mini.add(NbtString.of(id.toString()));
            mini.add(NbtString.of(entry.getValue().toString()));
            effectNbt.add(mini);
        }
        nbt.put(TONIC_EFFECTS_KEY, effectNbt);

        return tonic;
    }
    public static ItemStack getRepresentative()
    {
        return TONIC_REP;
    }


    public static int getEffectSpan(ItemStack tonic)
    {
        NbtCompound itemNbt = tonic.getOrCreateNbt();
        if (!(tonic.getItem() instanceof TonicItem)) return 0;
        if (!itemNbt.contains(TONIC_EFFECTS_KEY) || !itemNbt.contains("spanOffset"))
            return ((TonicItem) tonic.getItem()).getEffectLifetime();

        return ((TonicItem) tonic.getItem()).getEffectLifetime() + itemNbt.getInt("spanOffset");
    }

    public static void setEffectSpan(ItemStack tonic, int time)
    {
        NbtCompound itemNbt = tonic.getOrCreateNbt();
        itemNbt.putInt("spanOffset", time);
    }

    public static int getColor(ItemStack tonic)
    {
        NbtCompound itemNbt = tonic.getOrCreateNbt();
        if (!(tonic.getItem() instanceof TonicItem)) return 0x385DC6;
        if (!itemNbt.contains(TONIC_EFFECTS_KEY) || itemNbt.contains("representative")) return DEFAULT_COLOR;

        Color finalColor = new Color(0x385DC6);

        NbtList effects = itemNbt.getList(TONIC_EFFECTS_KEY, NbtElement.LIST_TYPE);

        for (NbtElement effect : effects) {
            Identifier effectId = Identifier.tryParse(((NbtList) effect).getString(0));
            if (effectId == null) continue;

            StatusEffect status = Registries.STATUS_EFFECT.get(effectId);
            if (status == null) continue;

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

        return finalColor.getRGB();
    }
    public static int[] getColorRGB(ItemStack tonic)
    {
        NbtCompound itemNbt = tonic.getOrCreateNbt();
        if (!(tonic.getItem() instanceof TonicItem)) return new int[]{0x38, 0x5D, 0xC6};
        if (!itemNbt.contains(TONIC_EFFECTS_KEY)) return new int[]{(DEFAULT_COLOR >> 16) & 255,(DEFAULT_COLOR >> 8) & 255, DEFAULT_COLOR & 255};

        Color finalColor = new Color(0x385DC6);

        NbtList effects = itemNbt.getList(TONIC_EFFECTS_KEY, NbtElement.LIST_TYPE);

        for (NbtElement effect : effects) {
            Identifier effectId = Identifier.tryParse(((NbtList) effect).getString(0));
            if (effectId == null) continue;

            StatusEffect status = Registries.STATUS_EFFECT.get(effectId);
            if (status == null) continue;

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

        return new int[]{finalColor.getRed(),finalColor.getGreen(),finalColor.getBlue()};
    }

    public static List<StatusEffectInstance> getTonicEffectInstances(ItemStack tonic)
    {
        if (!(tonic.getItem() instanceof TonicItem)) return null;

        NbtCompound nbt = tonic.getOrCreateNbt();
        List<StatusEffectInstance> instances = new ArrayList<>();

        NbtList effects = nbt.getList(TONIC_EFFECTS_KEY, NbtElement.LIST_TYPE);

        for (NbtElement effect : effects) {
            Identifier effectId = Identifier.tryParse(((NbtList) effect).getString(0));
            if (effectId == null) continue;
            int amplifier;
            try { amplifier = Integer.parseInt(((NbtList) effect).getString(1)); } catch (NumberFormatException ignored) {
                amplifier = 0;
            }

            StatusEffect status = Registries.STATUS_EFFECT.get(effectId);
            if (status == null) continue;

            instances.add(new StatusEffectInstance(status, TonicUtil.getEffectSpan(tonic), amplifier));
        }

        return instances;
    }

    public static Map<StatusEffect, Integer> getTonicEffects(ItemStack tonic)
    {
        return getTonicEffects(tonic.getOrCreateNbt());
    }

    public static Map<StatusEffect, Integer> getTonicEffects(NbtCompound nbt)
    {
        Map<StatusEffect, Integer> effects = new HashMap<>();

        NbtList effectsNbt = nbt.getList(TONIC_EFFECTS_KEY, NbtElement.LIST_TYPE);

        for (NbtElement effect : effectsNbt) {
            Identifier effectId = Identifier.tryParse(((NbtList) effect).getString(0));
            if (effectId == null) continue;
            int amplifier;
            try { amplifier = Integer.parseInt(((NbtList) effect).getString(1)); } catch (NumberFormatException e) {
                amplifier = 0;
            }

            StatusEffect status = Registries.STATUS_EFFECT.get(effectId);
            if (status == null) continue;

            effects.put(status, amplifier);
        }

        return effects;
    }

    /**
     * Creates a list of Status Effects, with one instance per effect.<br>
     * For example, Speed II and Haste IV would be <code>[speed, haste]</code>.
     */
    public static List<StatusEffect> getTonicEffectsList(ItemStack tonic)
    {
        return getTonicEffectsList(tonic.getOrCreateNbt());
    }

    /**
     * Creates a list of Status Effects, with one instance per effect.<br>
     * For example, Speed II and Haste IV would be <code>[speed, haste]</code>.
     */
    public static List<StatusEffect> getTonicEffectsList(NbtCompound nbt)
    {
        List<StatusEffect> effects = new ArrayList<>();

        NbtList effectsNbt = nbt.getList(TONIC_EFFECTS_KEY, NbtElement.LIST_TYPE);

        for (NbtElement effect : effectsNbt) {
            Identifier effectId = Identifier.tryParse(((NbtList) effect).getString(0));
            if (effectId == null) continue;

            StatusEffect status = Registries.STATUS_EFFECT.get(effectId);
            if (status == null) continue;

            effects.add(status);
        }

        return effects;
    }

    /**
     * Creates a list of Status Effects, with equal duplicates to amplifiers.<br>
     * For example, Speed II would be <code>[speed, speed]</code>.
     */
    public static List<StatusEffect> getTonicEffectsListFlat(ItemStack tonic)
    {
        return getTonicEffectsListFlat(tonic.getOrCreateNbt());
    }

    /**
     * Creates a list of Status Effects, with equal duplicates to amplifiers.<br>
     * For example, Speed II would be <code>[speed, speed]</code>.
     */
    public static List<StatusEffect> getTonicEffectsListFlat(NbtCompound nbt)
    {
        List<StatusEffect> effects = new ArrayList<>();

        NbtList effectsNbt = nbt.getList(TONIC_EFFECTS_KEY, NbtElement.LIST_TYPE);

        for (NbtElement effect : effectsNbt) {
            Identifier effectId = Identifier.tryParse(((NbtList) effect).getString(0));
            if (effectId == null) continue;
            int amplifier;
            try { amplifier = Integer.parseInt(((NbtList) effect).getString(1)); } catch (NumberFormatException ignored) {
                amplifier = 0;
            }
            StatusEffect status = Registries.STATUS_EFFECT.get(effectId);
            if (status == null) continue;

            for (int i = 0; i <= amplifier; i++) {effects.add(status);}
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
        int count = 0;
        NbtList list = nbt.getList(TONIC_EFFECTS_KEY, NbtElement.LIST_TYPE);
        for (NbtElement element : list)
        {
            String amplifier = ((NbtList) element).getString(1);
            try {
                count += Integer.parseInt(amplifier) + 1;
            } catch (NumberFormatException ignored) {
                count += 1;
            }
        }

        return count;
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
        NbtCompound compound = new NbtCompound();
        compound.putBoolean("representative", true);
        TONIC_REP.setNbt(compound);

        registerText(StatusEffects.ABSORPTION, i -> Text.translatable("tonic.effect.absorption", (i+1) * 2));
    }
}
