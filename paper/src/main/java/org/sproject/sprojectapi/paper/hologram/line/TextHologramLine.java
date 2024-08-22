package org.sproject.sprojectapi.paper.hologram.line;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.sproject.sprojectapi.api.logger.Logger;

public class TextHologramLine extends ServerSideHologramLine<TextHologramLine> {

    @Getter private Component text;
    private static final float DEFAULT_HEIGHT = 0.275f;

    public TextHologramLine() {}

    public TextHologramLine setText(Component text) {
        this.text = text;
        return this;
    }

    @Override
    public Entity spawn() {
        this.yTranslation = this.parrent.getHeight().get();
        TextDisplay textDisplay = this.getParrent().getLocation().getWorld().spawn(
                this.getParrent().getLocation(),//.clone().add(0, this.yTranslation, 0),
                TextDisplay.class
        );

        textDisplay.text(this.text);
        textDisplay.setBillboard(this.billboard);

        float heightChange = DEFAULT_HEIGHT * textDisplay.getTransformation().getScale().y() - this.height;
        this.height = DEFAULT_HEIGHT * textDisplay.getTransformation().getScale().y();
        this.parrent.getHeight().set(this.parrent.getHeight().get() + heightChange + this.spacing);

        this.entity = textDisplay;
        return this.entity;
    }
}
