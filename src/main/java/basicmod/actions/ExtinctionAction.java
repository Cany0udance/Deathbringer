package basicmod.actions;

import basicmod.effects.ExtinctionDustEffect;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class ExtinctionAction extends AbstractGameAction {
    private AbstractMonster target;
    private DamageInfo info;

    public ExtinctionAction(AbstractMonster target, DamageInfo info) {
        this.target = target;
        this.info = info;
    }

    @Override
    public void update() {
        if (target != null && !target.isDeadOrEscaped()) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.NONE));
            target.damage(info);
            if ((target.isDying || target.currentHealth <= 0) && !target.halfDead && !target.hasPower("Minion")) {
                AbstractDungeon.actionManager.addToTop(new MarkEnemyForExtinctionAction(target, target.id));

                // Add ExhaustEmberEffect on the enemy's location
                AbstractDungeon.effectList.add(new ExtinctionDustEffect(target.hb.cX, target.hb.cY));

                // Play the "bell" sound at varying pitch
                CardCrawlGame.sound.playA("BELL", MathUtils.random(-0.2F, -0.3F));
            }
        }
        this.isDone = true;
    }
}
