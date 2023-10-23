package basicmod.cards.skills;

import basicmod.actions.UpgradeStrangulateAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.StranglePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Strangulate extends BaseCard {
    public static final String ID = makeID("Strangulate");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL, // Updated to Skill
            CardRarity.COMMON, // Updated to Common
            CardTarget.ENEMY,
            1
    );

    private static final int STRANGLE_AMT = 3;
    private static final int UPG_STRANGLE_AMT = 5;

    public Strangulate() {
        super(ID, info);
        baseMagicNumber = magicNumber = STRANGLE_AMT;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_STRANGLE_AMT - STRANGLE_AMT);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
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
    }
    @Override
    public AbstractCard makeCopy() {
        return new Strangulate();
    }
}