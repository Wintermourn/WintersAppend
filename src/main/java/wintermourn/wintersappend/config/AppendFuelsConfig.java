package wintermourn.wintersappend.config;

import com.google.gson.*;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import wintermourn.wintersappend.WintersAppend;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AppendFuelsConfig {
    public static final Path CONFIG_PATH = Paths.get("config/wintermourn/"+ WintersAppend.MOD_ID, "fuels.json");
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    /** An automatically updated list of valid fuels for the Tonic Stand. Left is heat fuel, right is purity.
     * <br>Use RegisteredFuels if you're planning on adding your own items through code. */
    public static Map<Identifier, Pair<Integer, Integer>> Fuels = new HashMap<>();
    /** A manual list of fuel types that are applied to missing entries in the user's config.<br>
     * Left in the pair is heat fuel, right is purity. */
    public static final Map<Identifier, Pair<Integer, Integer>> RegisteredFuels = new HashMap<>();

    public static void LoadConfig()
    {
        try {
            if (Files.exists(CONFIG_PATH))
            {
                LoadValues();
            } else {
                Files.createDirectories(CONFIG_PATH.getParent());
                JsonObject defaultJsonObject = createDefaultConfig();
                saveConfig(defaultJsonObject);
                LoadValues();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static JsonObject GetConfig()
    {
        try {
            if (!Files.exists(CONFIG_PATH))
            {
                Files.createDirectories(CONFIG_PATH.getParent());
                JsonObject defaultJsonObject = createDefaultConfig();
                saveConfig(defaultJsonObject);
            }
            FileReader fileReader = new FileReader(CONFIG_PATH.toFile());
            return JsonParser.parseReader(fileReader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static void LoadValues()
    {
        try (FileReader fileReader = new FileReader(CONFIG_PATH.toFile()))
        {
            JsonObject jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();
            LoadValues(jsonObject);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected static void LoadValues(JsonObject jsonObject)
    {
        Fuels = new HashMap<>();

        try {
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet())
            {
                if (!entry.getValue().isJsonObject()) continue;
                JsonObject entryObject = entry.getValue().getAsJsonObject();

                int fuel = entryObject.has("fuel") ? entryObject.get("fuel").getAsInt() : -1;
                int purity = entryObject.has("purity") ? entryObject.get("purity").getAsInt() : -1;
                Fuels.put(Identifier.tryParse(entry.getKey()), new Pair<>(fuel, purity));
            }
            for (Map.Entry<Identifier, Pair<Integer, Integer>> entry : RegisteredFuels.entrySet())
            {
                if (Fuels.containsKey(entry.getKey())) continue;
                Fuels.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception ignored) {}
    }

    private static JsonObject createDefaultConfig() {
        JsonObject defaultJsonObject = new JsonObject();
        JsonObject blazeDefault = new JsonObject();
        blazeDefault.addProperty("type", "heat");
        blazeDefault.addProperty("value", 100);
        JsonObject gypsoDefault = new JsonObject();
        gypsoDefault.addProperty("type", "purity");
        gypsoDefault.addProperty("value", 60);
        // Add default weights for known items
        defaultJsonObject.add("minecraft:blaze_powder", blazeDefault);
        defaultJsonObject.add( WintersAppend.MOD_ID+":gypsophila", gypsoDefault);
        return defaultJsonObject;
    }

    private static void saveConfig(JsonObject jsonObject) throws IOException {
        try (FileWriter fileWriter = new FileWriter(CONFIG_PATH.toFile())) {
            GSON.toJson(jsonObject, fileWriter);
        }
    }

    public static int GetHeatFuel(ItemStack stack)
    {
        Identifier id = Registries.ITEM.getId(stack.getItem());
        if (Fuels.containsKey(id))
        {
            return Fuels.get(id).getLeft();
        }
        return -1;
    }

    public static int GetPurity(ItemStack stack)
    {
        Identifier id = Registries.ITEM.getId(stack.getItem());
        if (Fuels.containsKey(id))
        {
            return Fuels.get(id).getRight();
        }
        return -1;
    }

    // I don't like that I'm just sending the config file as JSON instead of sending a list of ID (string), type (byte), and value (int),
    // but I don't know how to check if it's done scanning the data, so...
    // If anyone's reading this that knows how to do this (or might know someone who does), please let me (Wintermourn) know.
    public static void TransferFuelsFromServer(PacketByteBuf buf)
    {
        JsonObject config = JsonParser.parseString(buf.readString()).getAsJsonObject();
        LoadValues(config);
    }

    public static PacketByteBuf CreateFuelsPacket()
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(Objects.requireNonNull(GetConfig()).toString());
        return buf;
    }

}
