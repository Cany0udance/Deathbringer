
package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.ConvertPlatedArmorToStranglePower;
import basicmod.powers.GainStrangleEqualToPlatedArmorPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.EndTurnAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public class ReactiveNet extends BaseCard {
    public static final String ID = makeID("ReactiveNet");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.ENEMY,
            2
    );

    private static final int PLATED_ARMOR = 15;
    private static final int UPG_PLATED_ARMOR = 0; // No upgrade to the plated armor amount

    public ReactiveNet() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = PLATED_ARMOR;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, m, new PlatedArmorPower(m, this.magicNumber), this.magicNumber));

        if (upgraded) {
            addToBot(new ApplyPowerAction(m, m, new ConvertPlatedArmorToStranglePower(m, 1), 1));
        } else {
            addToBot(new ApplyPowerAction(m, m, new GainStrangleEqualToPlatedArmorPower(m, 1), 1));
        }

        addToBot(new PressEndTurnButtonAction());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }


    @Override
    public AbstractCard makeCopy() {
        return new ReactiveNet();
    }
}