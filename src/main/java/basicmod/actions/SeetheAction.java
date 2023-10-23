package basicmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;

import java.util.Iterator;

public class SeetheAction extends AbstractGameAction {
    private DamageInfo info;

    public SeetheAction(AbstractCreature target, DamageInfo info) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.WAIT;
        this.attackEffect = AttackEffect.FIRE;
    }

    public void update() {
        int count = 0;

        // First, add the damage actions to the queue; they will happen last since we add them first.
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.type != AbstractCard.CardType.ATTACK) {
                count++;
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.type != AbstractCard.CardType.ATTACK) {
                count++;
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c.type != AbstractCard.CardType.ATTACK) {
                count++;
            }
        }

        for (int i = 0; i < count; ++i) {
            addToTop(new DamageAction(this.target, new DamageInfo(AbstractDungeon.player, this.info.base, DamageInfo.DamageType.NORMAL), AttackEffect.FIRE));
        }

        // Then, add the Exhaust actions.
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.type != AbstractCard.CardType.ATTACK) {
                addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.type != AbstractCard.CardType.ATTACK) {
                addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.drawPile));
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c.type != AbstractCard.CardType.ATTACK) {
                addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.discardPile));
            }
        }

        if (count >= 5) {
            CardCrawlGame.sound.play("STANCE_ENTER_WRATH");
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.SCARLET, true));
            AbstractDungeon.effectsQueue.add(new StanceChangeParticleGenerator(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, "Wrath"));
        }

        this.isDone = true;
    }
}
