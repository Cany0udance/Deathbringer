package basicmod.cards.attacks;

import basemod.BaseMod;
import basemod.interfaces.MaxHPChangeSubscriber;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.OutburstPower;
import basicmod.util.CardStats;
import basicmod.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.EndTurnDeathPower;

import static basicmod.Deathbringer.RED_BORDER_GLOW_COLOR;

public class Liability extends BaseCard {
    public static final String ID = makeID("Liability");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 10;
    private static final int MULTIPLIER = 2;
    private static final int UPG_MULTIPLIER = 1;

    public Liability() {
        super(ID, info);
        setDamage(DAMAGE);
        this.magicNumber = this.baseMagicNumber = MULTIPLIER;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; ++i) {
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }

        addToBot(new ApplyPowerAction(p, p, new OutburstPower(p, 2), 2));

        ShadowUtility.triggerGeneralShadowEffect(this);
    }

    public void triggerFullEffect() {
        AbstractPlayer p = AbstractDungeon.player;

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster m = AbstractDungeon.getRandomMonster();
                if (m != null) {
                    int calculatedDamage = damage;
                    if (m.hasPower("Vulnerable")) {
                        calculatedDamage *= 1.5;
                    }
                    for (int i = 0; i < magicNumber; ++i) {
                        addToBot(new DamageAction(m, new DamageInfo(p, calculatedDamage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                    }
                }
                addToBot(new ApplyPowerAction(p, p, new OutburstPower(p, 2), 2));
                this.isDone = true;
            }
        });
    }

    public void triggerHalfEffect() {
        AbstractPlayer p = AbstractDungeon.player;

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster m = AbstractDungeon.getRandomMonster();
                if (m != null) {
                    int calculatedDamage = damage / 2;
                    if (m.hasPower("Vulnerable")) {
                        calculatedDamage *= 1.5;
                    }
                    for (int i = 0; i < magicNumber; ++i) {
                        addToBot(new DamageAction(m, new DamageInfo(p, calculatedDamage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                    }
                }
                addToBot(new ApplyPowerAction(p, p, new OutburstPower(p, 1), 1));
                this.isDone = true;
            }
        });
    }

   /* @Override
    public void triggerOnGlowCheck() {
        // Retrieve the OutburstPower from the player if it exists
        AbstractPower outburstPower = AbstractDungeon.player.getPower("Deathbringer:Outburst");

        if (outburstPower != null && outburstPower.amount >= 3) {
            // Glow red if playing this card will trigger the Outburst explosion
            this.glowColor = RED_BORDER_GLOW_COLOR.cpy();
        } else {
            // Glow blue otherwise
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
*/
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_MULTIPLIER);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Liability();
    }
}