package basicmod.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.UpgradeShineParticleEffect;
import com.badlogic.gdx.math.MathUtils;

import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.core.Settings;

public class CustomSlashEffect extends AbstractGameEffect {
    private AnimatedSlashEffect innerEffect;

    public CustomSlashEffect(float x, float y) {
        // Initialize AnimatedSlashEffect with targetScale parameter doubled (e.g., 4.0F instead of 2.0F)
        innerEffect = new AnimatedSlashEffect(x, y, 0, 0, 45, 4.0F, Color.YELLOW, Color.BLACK);
    }

    @Override
    public void update() {
        innerEffect.update();
        this.isDone = innerEffect.isDone;
    }

    @Override
    public void render(SpriteBatch sb) {
        innerEffect.render(sb);
    }

    @Override
    public void dispose() {
        innerEffect.dispose();
    }
}
