package pl.dreammc.dreammcapi.api.communication.packet;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.lettuce.core.codec.RedisCodec;
import pl.dreammc.dreammcapi.api.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PacketCodec implements RedisCodec<String, Packet> {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Class<? extends Packet>> packetTypes = new HashMap<>();

    public void registerPacketType(String typeId, Class<? extends Packet> clazz) {
        packetTypes.put(typeId, clazz);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Override
    public String decodeKey(ByteBuffer byteBuffer) {
        return CHARSET.decode(byteBuffer).toString();
    }

    @Override
    public Packet decodeValue(ByteBuffer byteBuffer) {
        try {
            byte[] buffer = new byte[byteBuffer.remaining()];
            byteBuffer.get(buffer);
            String json = new String(buffer, CHARSET);

            JsonNode root = mapper.readTree(json);

            String type = root.get("type").asText();
            Class<? extends Packet> clazz = packetTypes.get(type);

            return mapper.treeToValue(root, clazz);
        } catch (Exception e) {
            Logger.sendError(e.getMessage());
        }

        return null;
    }

    @Override
    public ByteBuffer encodeKey(String s) {
        return CHARSET.encode(s);
    }

    @Override
    public ByteBuffer encodeValue(Packet packet) {
        try {
            ObjectNode json = mapper.valueToTree(packet);

            String type = PacketHelper.getPacketType(packet.getClass());
            json.put("type", type);

            return ByteBuffer.wrap(mapper.writeValueAsBytes(json));
        } catch (Exception e) {
            Logger.sendError(e.getMessage());
        }

        return null;
    }
}
