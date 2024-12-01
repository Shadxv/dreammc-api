package pl.dreammc.dreammcapi.paper.database;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bukkit.inventory.ItemStack;
import pl.dreammc.dreammcapi.api.database.MongoService;

import java.util.HashMap;
import java.util.Map;

public class InventoryContentCodec implements Codec<InventoryContentContainer> {

    @Override
    public InventoryContentContainer decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Map<Integer, ItemStack> map = new HashMap<>();

        Codec<ItemStack> itemStackCodec = MongoService.getCodec(ItemStack.class);

        bsonReader.readStartDocument();

        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String key = bsonReader.readName();
            Integer intKey = Integer.parseInt(key);

            ItemStack itemStack = itemStackCodec.decode(bsonReader, decoderContext);

            map.put(intKey, itemStack);
        }

        bsonReader.readEndDocument();

        return new InventoryContentContainer(map);
    }

    @Override
    public void encode(BsonWriter bsonWriter, InventoryContentContainer inventoryContentContainer, EncoderContext encoderContext) {
        Codec<ItemStack> itemStackCodec = MongoService.getCodec(ItemStack.class);

        bsonWriter.writeStartDocument();

        for (Map.Entry<Integer, ItemStack> entry : inventoryContentContainer.getContent().entrySet()) {
            bsonWriter.writeName(entry.getKey().toString());
            itemStackCodec.encode(bsonWriter, entry.getValue(), encoderContext);
        }

        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<InventoryContentContainer> getEncoderClass() {
        return InventoryContentContainer.class;
    }
}
