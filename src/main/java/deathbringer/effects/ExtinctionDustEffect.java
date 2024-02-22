package deathbringer.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ExhaustBlurEffect;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;

public class ExtinctionDustEffect extends AbstractGameEffect {
    private float x, y;
    private static final float DUR = 1.0F;

    public ExtinctionDustEffect(float x, float y) {
        this.duration = DUR;
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (this.duration == DUR) {
            CardCrawlGame.sound.play("CARD_EXHAUST", 0.2F);

            int i;
            for(i = 0; i < 90; ++i) {
                AbstractDungeon.effectsQueue.add(new ExhaustBlurEffect(this.x, this.y));
            }

            for(i = 0; i < 50; ++i) {
                AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(this.x, this.y));
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        // No card to render
    }

    public void dispose() {
        // No resources to dispose
    }
}
