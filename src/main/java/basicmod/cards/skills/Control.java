package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class Control extends BaseCard {
    public static final String ID = makeID("Control");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1 // Cost
    );

    private static final int ARTIFACT = 1;
    private static final int UPG_ARTIFACT = 1; // Upgrade amount

    public Control() {
        super(ID, info);
        this.exhaust = true;  // The card is exhausted after use
        setMagic(ARTIFACT, UPG_ARTIFACT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Remove all Outburst stacks
        addToBot(new RemoveSpecificPowerAction(p, p, "Deathbringer:Outburst"));

        // Apply Artifact using magicNumber
        addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Control();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_ARTIFACT);
            initializeDescription();
        }
    }
}
