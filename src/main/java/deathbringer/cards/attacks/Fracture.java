package deathbringer.cards.attacks;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class Fracture extends BaseCard {
    public static final String ID = makeID("Fracture");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 5;
    private static final int UPG_DAMAGE = 7;

    public Fracture() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        // Removed exhaust flag
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (m.lastDamageTaken > 0) {
                    addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -1), -1));
                    addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, 1), 1));
                }
                this.isDone = true;
            }
        });

        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (m.lastDamageTaken > 0) {
                    addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -1), -1));
                    addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, 1), 1));
                }
                this.isDone = true;
            }
        });
    }


    @Override
    public AbstractCard makeCopy() {
        return new Fracture();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            initializeDescription();
        }
    }
}