package basicmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static basicmod.Deathbringer.FLASHLIGHTOFF_KEY;
import static basicmod.Deathbringer.FLASHLIGHTON_KEY;

public class NightlightEffect extends AbstractGameEffect {
    private boolean flashlightOnPlayed = false;
    private boolean flashlightOffPlayed = false;

    public NightlightEffect() {
        this.duration = 2.0F;  // Changed from 3.0F to 2.0F
        this.color = new Color(1.0F, 1.0F, 0.8F, 0.5F);
    }

    public void update() {
        if (this.duration <= 2.0F && !flashlightOnPlayed && this.duration > 2.0F - 0.25F) {  // Changed 3.0F to 2.0F
            CardCrawlGame.sound.play(FLASHLIGHTON_KEY);
            flashlightOnPlayed = true;
        }

        if (this.duration <= 0.5F && !flashlightOffPlayed) {
            CardCrawlGame.sound.play(FLASHLIGHTOFF_KEY);
            flashlightOffPlayed = true;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration > 1.0F) {
            this.color.a = Interpolation.pow5In.apply(0.5F, 0.0F, (this.duration - 1.0F) / 1.0F);
        } else if (this.duration <= 0.5F) {  // Fade out in the last 0.5 seconds
            this.color.a = Interpolation.exp10In.apply(0.0F, 0.5F, this.duration / 0.5F);
        } else {  // This makes sure the alpha stays constant at 0.5 in between
            this.color.a = 0.5F;
        }

        if (this.duration < 0.0F) {
            this.color.a = 0.0F;
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);

        float xOffset = 500.0f;  // Adjust this value to move the spotlight's starting position.
        float playerX = AbstractDungeon.player.drawX + xOffset; // x-coordinate where the spotlight starts
        float playerY = AbstractDungeon.player.drawY;  // y-coordinate where the spotlight starts

        sb.draw(
                ImageMaster.SPOTLIGHT_VFX,   // Texture
                playerX,                     // x-coordinate where the spotlight starts
                playerY,                     // y-coordinate where the spotlight starts
                ImageMaster.SPOTLIGHT_VFX.getWidth() / 2,    // Origin x (for rotation)
                ImageMaster.SPOTLIGHT_VFX.getHeight() / 2,   // Origin y (for rotation)
                ImageMaster.SPOTLIGHT_VFX.getWidth(),        // Width of the image
                ImageMaster.SPOTLIGHT_VFX.getHeight(),       // Height of the image
                0.5f, 5f,                    // Scale x and y
                90,                         // Rotation in degrees (90 degrees to make it horizontal)
                0, 0,                       // Texture region x and y
                ImageMaster.SPOTLIGHT_VFX.getWidth(),
                ImageMaster.SPOTLIGHT_VFX.getHeight(),
                false, false                 // Flip x and y
        );

        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}