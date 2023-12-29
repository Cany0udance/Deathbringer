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
    private static final int UPG_STRENGTH_GAIN = 3;
    private static final int VIGOR_GAIN = 5;
    private static final int UPG_VIGOR_GAIN = 10;  // New constant for upgraded Vigor

    public SubconsciousKiller() {
        super(ID, info);
        this.cost = -2;  // Making the card Unplayable
        this.magicNumber = this.baseMagicNumber = STRENGTH_GAIN;  // Set the magic number for Vigor
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
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VigorPower(AbstractDungeon.player, 5), 5));  // Use the magic number
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_STRENGTH_GAIN - STRENGTH_GAIN);  // Upgrade the magic number for Vigor
            initializeDescription();
        }
    }


    @Override
    public AbstractCard makeCopy() {
        return new SubconsciousKiller();
    }
}
