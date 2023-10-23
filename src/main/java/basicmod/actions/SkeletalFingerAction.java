package basicmod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class SkeletalFingerAction extends AbstractGameAction {
    private DamageInfo info;
    private int intentDamage;
    private int hits;
    private int currentHit = 0;
    private float delayTime = 0.5F;
    private float elapsedDelay = 0.0F;

    public SkeletalFingerAction(AbstractMonster target, AbstractPlayer source, DamageInfo info, boolean upgraded) {
        this.target = target;
        this.source = source;
        this.info = info;
        this.hits = upgraded ? 2 : 1;
        this.duration = 0.1F;
    }

    public void update() {
        if (this.duration > 0) {
            this.duration -= Gdx.graphics.getDeltaTime();
        }

        if (this.duration <= 0.0F && this.target != null) {
            if (elapsedDelay >= delayTime || currentHit == 0) {
                elapsedDelay = 0.0F;
                this.target.damage(this.info);

                // Check if the enemy actually took HP damage, not just block damage
                if (((AbstractMonster) this.target).lastDamageTaken > 0) {
                    checkIntentAndApplyExtraDamage();
                }

                currentHit++;

                if (currentHit >= hits) {
                    this.isDone = true;
                }
            } else {
                elapsedDelay += Gdx.graphics.getDeltaTime();
            }
        }
    }

    private void checkIntentAndApplyExtraDamage() {
        switch (((AbstractMonster) this.target).intent) {
            case ATTACK:
            case ATTACK_BUFF:
            case ATTACK_DEBUFF:
            case ATTACK_DEFEND:
                intentDamage = ((AbstractMonster) this.target).getIntentDmg();
                addToTop(new LoseHPAction(target, source, intentDamage));
                break;
            default:
                intentDamage = 0;
                break;
        }
    }
}

