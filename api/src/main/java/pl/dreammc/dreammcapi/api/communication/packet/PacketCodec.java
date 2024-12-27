package pl.dreammc.dreammcapi.api.communication.packet;

import io.lettuce.core.codec.RedisCodec;
import pl.dreammc.dreammcapi.api.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PacketCodec implements RedisCodec<String, Packet> {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    @Override
    public String decodeKey(ByteBuffer byteBuffer) {
        return CHARSET.decode(byteBuffer).toString();
    }

    @Override
    public Packet decodeValue(ByteBuffer byteBuffer) {
        try {
            byte[] buffer = new byte[byteBuffer.remaining()];
            byteBuffer.get(buffer);
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(buffer));

            final Packet packet = (Packet) is.readObject();

            is.close();
            return packet;
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
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bytes);
            os.writeObject(packet);

            final ByteBuffer wrap = ByteBuffer.wrap(bytes.toByteArray());

            os.close();
            return wrap;
        } catch (Exception e) {
            Logger.sendError(e.getMessage());
        }

        return null;
    }
}
