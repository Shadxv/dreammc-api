package pl.dreammc.dreammcapi.paper.database;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import pl.dreammc.dreammcapi.api.database.MongoService;

import java.util.HashMap;
import java.util.Map;

public class InventoryContentCodec implements Codec<Map<Integer, ItemStack>> {

    @Override
    public Map<Integer, ItemStack> decode(BsonReader bsonReader, DecoderContext decoderContext) {
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

        return map;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Map<Integer, ItemStack> integerItemStackMap, EncoderContext encoderContext) {
        Codec<ItemStack> itemStackCodec = MongoService.getCodec(ItemStack.class);

        bsonWriter.writeStartDocument();

        for (Map.Entry<Integer, ItemStack> entry : integerItemStackMap.entrySet()) {
            bsonWriter.writeName(entry.getKey().toString());
            itemStackCodec.encode(bsonWriter, entry.getValue(), encoderContext);
        }

        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<Map<Integer, ItemStack>> getEncoderClass() {
        return (Class<Map<Integer, ItemStack>>) (Class<?>) Map.class;
    }
}
