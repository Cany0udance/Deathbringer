package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class Desperation extends BaseCard {
    public static final String ID = makeID("Desperation");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0  // Energy cost
    );

    private static final int ENERGY_GAIN = 1;
    private static final int EXTRA_ENERGY_IF_POISONED = 1;  // Additional energy gain if poisoned

    public Desperation() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = ENERGY_GAIN;
        this.exhaust = true;  // The card exhausts after use
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Always gain 1 energy
        addToBot(new GainEnergyAction(this.magicNumber));

        // Gain additional energy if poisoned
        if (p.hasPower(PoisonPower.POWER_ID)) {
            addToBot(new GainEnergyAction(EXTRA_ENERGY_IF_POISONED));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.retain = true;  // Card now retains when upgraded
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Desperation();
    }
}
