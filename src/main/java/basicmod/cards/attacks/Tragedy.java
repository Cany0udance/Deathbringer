package basicmod.cards.attacks;

import basicmod.actions.UpgradeTragedyAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Tragedy extends BaseCard {
    public static final String ID = makeID("Tragedy");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            1 // Energy cost
    );

    private static final int DAMAGE = 2;
    private static final int UPG_DAMAGE = 2; // No change when upgraded, but we can add this for clarity
    private static final int HITS = 5;
    private static final int UPG_HITS = 7;

    public Tragedy() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(HITS, UPG_HITS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; ++i) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }

        // Check for the Progressive condition only if the card is unupgraded
        if (!this.upgraded) {
            if (p.hasPower(StrengthPower.POWER_ID) && p.getPower(StrengthPower.POWER_ID).amount >= 5) {
                addToBot(new UpgradeTragedyAction(this));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Tragedy();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_HITS - HITS);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
