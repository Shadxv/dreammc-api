package pl.dreammc.dreammcapi.paper.input;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import pl.dreammc.dreammcapi.paper.manager.InputManager;

public abstract class InputModel {

    public enum InputType {
        CHAT,
        SIGN
    }

    @Getter private final Player owner;
    @Getter private final InputType type;
    @Getter private final Component[] textLines;
    @Getter @Setter
    private String inputValue;

    public InputModel(Player owner, InputType type, Component... textLines) {
        this.owner = owner;
        this.type = type;
        this.textLines = textLines;
        this.takeInput();
    }

    private void openSign(Component firstLine, Component secondLine) {

        // NOT IMPLEMENTED YET!!!

        /*
        Location signLocation = this.owner.getLocation().add(0, 1, 0);
        BlockPos signBlockPos = new BlockPos(signLocation.getBlockX(), signLocation.getBlockY(), signLocation.getBlockZ());
        this.owner.sendBlockChange(signLocation, Material.OAK_SIGN.createBlockData());

        ListTag firstLineTag = new ListTag();
        firstLineTag.add(TagParser.parseTag(""));

        Tag signNbt = new CompoundTag().put("BlockEntityTag", new CompoundTag()
                .put("front_text", new CompoundTag()
                        .put("messages", new ListTag()
                                .add(firstLineTag.)
                        )
                )
        );

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        FriendlyByteBuf packetData = new FriendlyByteBuf(buf);
        packetData.writeBlockPos(signBlockPos);
        packetData.writeId(BuiltInRegistries.BLOCK_ENTITY_TYPE, BlockEntityType.SIGN);
        packetData.writeNbt(signNbt);

        var signData = new ClientboundBlockEntityDataPacket(packetData);
        var openSignPacket = new ClientboundOpenSignEditorPacket(signBlockPos, true);
        */
    }

    private void useChat(Component message) {
        if(message == null) return;
        this.owner.sendMessage(message);
    }

    public void reuseChat() {
        this.useChat(null);
    }

    public void reopenSing(Component secondLine) {
        this.openSign(Component.text("Invalid input").color(TextColor.fromHexString("#ff5555")), secondLine);
    }

    public void takeInput() {
        if(!InputManager.getInstance().addInputRequest(this)) return;
        switch (type) {
            case CHAT -> {
                useChat(textLines[0]);
            }
            case SIGN -> {
                Component firstLine = this.textLines.length > 0 ? ((TextComponent)this.textLines[0]) : Component.empty();
                Component secondLine = this.textLines.length > 1 ? ((TextComponent)this.textLines[1]) : Component.empty();
                openSign(firstLine, secondLine);
            }
        }
    }

    public abstract boolean validate();
}
