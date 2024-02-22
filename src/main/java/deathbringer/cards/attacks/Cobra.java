package deathbringer.cards.attacks;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class Cobra extends BaseCard {
    public static final String ID = makeID("Cobra");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1  // Energy cost
    );

    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DAMAGE = 5;  // Upgrade to 13 damage

    public Cobra() {
        super(ID, info);
        this.baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        if (deathbringer.Deathbringer.shadowplaysThisCombat > 0) {
            addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, deathbringer.Deathbringer.shadowplaysThisCombat), deathbringer.Deathbringer.shadowplaysThisCombat));
        }
        // Reset the description back to the original after use
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        // Update the description dynamically to reflect the number of Shadowplays
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0] + deathbringer.Deathbringer.shadowplaysThisCombat + (deathbringer.Deathbringer.shadowplaysThisCombat == 1 ? cardStrings.EXTENDED_DESCRIPTION[1] : cardStrings.EXTENDED_DESCRIPTION[2]);
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        // Reset the description when the card is moved to discard
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Cobra();
    }
}