package basicmod.effects;

import basicmod.Deathbringer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeDur;
import com.megacrit.cardcrawl.helpers.ScreenShake.ShakeIntensity;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.UpgradeHammerImprintEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineParticleEffect;

import static basicmod.Deathbringer.UNSHEATHE_KEY;

public class ProgressionUnsheatheEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private boolean soundPlayed = false;
    private boolean slashEffectAdded = false;


    public ProgressionUnsheatheEffect(float x, float y) {
        this.x = x;
        this.y = y;
        this.duration = 0.8F;
    }

    public void update() {
        // Sound plays quarter-way through the total duration (0.2F for 0.8F total duration)
        if (this.duration < 0.65F && !this.soundPlayed) {
            CardCrawlGame.sound.play(UNSHEATHE_KEY); // Play sound
            this.soundPlayed = true; // Ensure sound is only played once
        }

        // CustomSlashEffect still plays halfway through the total duration (0.4F for 0.8F total duration)
        if (this.duration < 0.4F && !this.slashEffectAdded) {
            float middleX = Settings.WIDTH / 2.0F;
            float middleY = Settings.HEIGHT / 2.0F;
            AbstractDungeon.topLevelEffectsQueue.add(new CustomSlashEffect(middleX, middleY)); // CustomSlashEffect in the middle
            this.slashEffectAdded = true; // Ensure effect is only added once
        }

        this.duration -= Gdx.graphics.getDeltaTime(); // Decrease duration

        if (this.duration < 0.0F) {
            this.isDone = true; // End effect
        }
    }


    public void render(SpriteBatch sb) {
        // Unchanged
    }

    public void dispose() {
        // Unchanged
    }
}
