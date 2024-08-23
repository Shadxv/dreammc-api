package org.sproject.sprojectapi.paper.hologram.line;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class TextHologramLine extends ServerSideHologramLine<TextHologramLine> {

    @Getter private Component text;
    @Getter private TextDisplay.TextAlignment textAlignment;
    @Getter private Color backgroundColor;
    @Getter private boolean defaultBackground = true;
    @Getter private boolean shadowed = false;
    @Getter private byte textOpacity = -1;
    @Getter private float scale = 1.0f;
    private static final float DEFAULT_HEIGHT = 0.275f;

    public TextHologramLine() {}

    public TextHologramLine setText(Component text) {
        this.text = text;
        if(this.parrent.isSpawned()) ((TextDisplay) this.entity).text(text);
        return this;
    }

    public TextHologramLine setTextAlignment(TextDisplay.TextAlignment alignment) {
        this.textAlignment = alignment;
        if(this.parrent.isSpawned()) ((TextDisplay) this.entity).setAlignment(alignment);
        return this;
    }

    public TextHologramLine setBackgroundColor(@Nullable Color backgroundColor) {
        this.defaultBackground = backgroundColor == null;
        this.backgroundColor = backgroundColor;
        if(this.parrent.isSpawned()) {
            TextDisplay textDisplay = (TextDisplay) this.entity;
            textDisplay.setBackgroundColor(backgroundColor);
            textDisplay.setDefaultBackground(this.defaultBackground);
        }
        return this;
    }

    public TextHologramLine setShadowed(boolean shadowed) {
        this.shadowed = shadowed;
        if(this.parrent.isSpawned()) ((TextDisplay) this.entity).setShadowed(shadowed);
        return this;
    }

    public TextHologramLine setTextOpacity(byte opacity) {
        this.textOpacity = opacity;
        if(this.parrent.isSpawned()) ((TextDisplay) this.entity).setTextOpacity(opacity);
        return this;
    }

    public TextHologramLine setScale(float scale) {
        this.scale = scale;
        if(this.parrent.isSpawned()) {
            TextDisplay textDisplay = (TextDisplay) this.entity;
            (textDisplay).setTransformation(
                    new Transformation(
                            textDisplay.getTransformation().getTranslation(),
                            textDisplay.getTransformation().getLeftRotation(),
                            new Vector3f(this.scale),
                            textDisplay.getTransformation().getRightRotation()
                    )
            );
        }
        return this;
    }

    @Override
    public Entity spawn() {
        this.yTranslation = this.parrent.getHeight().get();
        TextDisplay textDisplay = this.getParrent().getLocation().getWorld().spawn(
                this.getParrent().getLocation(),
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
