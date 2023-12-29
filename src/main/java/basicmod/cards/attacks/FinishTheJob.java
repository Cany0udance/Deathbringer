package basicmod.cards.attacks;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class FinishTheJob extends BaseCard {
    public static final String ID = makeID("FinishTheJob");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ALL,
            0
    );

    private static final int DAMAGE = 30;
    private static final int UPG_DAMAGE = 40;

    public FinishTheJob() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        // Add explosion effect on all enemies before dealing damage.
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new VFXAction(new ExplosionSmallEffect(monster.hb.cX, monster.hb.cY), 0.1F));
            }
        }

        addToBot(new DamageAllEnemiesAction(p, multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                boolean combatEnded = true;

                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (!monster.isDying && !monster.isDead) {
                        combatEnded = false;
                        break;
                    }
                }

                if (!combatEnded) {
                    this.addToBot(new VFXAction(new LightningEffect(p.hb.cX, p.hb.cY)));
                    this.addToBot(new LoseHPAction(p, p, 99999));
                }

                this.isDone = true;
            }
        });
    }



    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m);
    }

    @Override
    public AbstractCard makeCopy() {
        return new FinishTheJob();
    }
}