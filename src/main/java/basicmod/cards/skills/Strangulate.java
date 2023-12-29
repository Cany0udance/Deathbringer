package basicmod.cards.skills;

import basicmod.actions.UpgradeStrangulateAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.OutburstPower;
import basicmod.powers.StranglePower;
import basicmod.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static basicmod.cards.FlavorConstants.FLAVOR_BOX_COLOR;
import static basicmod.cards.FlavorConstants.FLAVOR_TEXT_COLOR;

public class Strangulate extends BaseCard {
    public static final String ID = makeID("Strangulate");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL, // Updated to Skill
            CardRarity.COMMON, // Updated to Common
            CardTarget.ENEMY,
            1
    );

    private static final int STRANGLE_AMT = 4;
    private static final int UPG_STRANGLE_AMT = 6;

    public Strangulate() {
        super(ID, info);
        baseMagicNumber = magicNumber = STRANGLE_AMT;
        if (!upgraded) {
            FlavorText.AbstractCardFlavorFields.boxColor.set(this, FLAVOR_BOX_COLOR);
            FlavorText.AbstractCardFlavorFields.textColor.set(this, FLAVOR_TEXT_COLOR);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_STRANGLE_AMT - STRANGLE_AMT);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
            removeFlavorText();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new StranglePower(m, magicNumber), magicNumber));

        // Check for the Progressive condition only if the card is unupgraded
        if (!this.upgraded) {
            if (m.hasPower(StranglePower.POWER_ID) && m.getPower(StranglePower.POWER_ID).amount >= 15) {
                addToBot(new UpgradeStrangulateAction(this));
            }
        }
        if (this.upgraded) {
            addToBot(new ApplyPowerAction(p, p, new OutburstPower(p, 1), 1));
        }
    }

    private void removeFlavorText() {
        // Implement logic to remove or hide the flavor text
        // For example, set it to an empty string or null
        FlavorText.AbstractCardFlavorFields.boxColor.set(this, null);
        FlavorText.AbstractCardFlavorFields.textColor.set(this, null);
    }
    @Override
    public AbstractCard makeCopy() {
        return new Strangulate();
    }
}