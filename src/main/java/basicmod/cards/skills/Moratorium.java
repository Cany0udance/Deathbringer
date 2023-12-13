package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.OutburstPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static basicmod.Deathbringer.RED_BORDER_GLOW_COLOR;

public class Moratorium extends BaseCard {
    public static final String ID = makeID("Moratorium");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            3 // Cost
    );

    private static final int BLOCK_GAIN = 70;
    private static final int UPG_BLOCK_GAIN = 80;
    private static final int OUTBURST_GAIN = 3;
    private static final int UPG_OUTBURST_GAIN = 4;

    public Moratorium() {
        super(ID, info);
        setBlock(BLOCK_GAIN, UPG_BLOCK_GAIN);
        this.baseMagicNumber = this.magicNumber = OUTBURST_GAIN;
        this.exhaust = true; // The card is exhausted after use
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK_GAIN - BLOCK_GAIN);
            upgradeMagicNumber(UPG_OUTBURST_GAIN - OUTBURST_GAIN);
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new ApplyPowerAction(p, p, new OutburstPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void triggerOnGlowCheck() {
        // Retrieve the OutburstPower from the player if it exists
        AbstractPower outburstPower = AbstractDungeon.player.getPower("Deathbringer:Outburst");

        // Determine the threshold for the red glow based on whether the card is upgraded
        int redGlowThreshold = this.upgraded ? 1 : 2;

        if (outburstPower != null && outburstPower.amount >= redGlowThreshold) {
            // Glow red if playing this card will trigger the Outburst explosion
            this.glowColor = RED_BORDER_GLOW_COLOR.cpy();
        } else {
            // Glow blue otherwise
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }


    @Override
    public AbstractCard makeCopy() {
        return new Moratorium();
    }
}
