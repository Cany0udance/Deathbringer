package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class SubconsciousKiller extends BaseCard {
    public static final String ID = makeID("SubconsciousKiller");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            -2  // Energy cost set to -2 for unplayable cards
    );

    private static final int STRENGTH_GAIN = 2;
    private static final int VIGOR_GAIN = 5;
    private static final int UPG_VIGOR_GAIN = 8;  // New constant for upgraded Vigor

    private int actualVigorGain = VIGOR_GAIN;  // Variable to hold the current Vigor value

    public SubconsciousKiller() {
        super(ID, info);
        this.cost = -2;  // Making the card Unplayable
        this.magicNumber = this.baseMagicNumber = VIGOR_GAIN;  // Set the magic number for Vigor gain
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Do nothing because it's unplayable
    }

    public void triggerShadowplayEffect() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STRENGTH_GAIN), STRENGTH_GAIN));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VigorPower(AbstractDungeon.player, magicNumber), magicNumber));  // Use the magic number
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.selfRetain = true;  // Retain this card when upgraded
            upgradeMagicNumber(UPG_VIGOR_GAIN - VIGOR_GAIN);  // Upgrade the magic number for Vigor
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }


    @Override
    public AbstractCard makeCopy() {
        return new SubconsciousKiller();
    }
}
