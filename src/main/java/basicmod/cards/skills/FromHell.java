package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.OutburstPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static basicmod.Deathbringer.RED_BORDER_GLOW_COLOR;

public class FromHell extends BaseCard {
    public static final String ID = makeID("FromHell");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY,
            1
    );

    public FromHell() {
        super(ID, info);
        this.baseMagicNumber = 2; // Represents both Vulnerable and Strength
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;  // Card will be exhausted when played
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new ApplyPowerAction(monster, p, new VulnerablePower(monster, magicNumber, false), magicNumber));
        }

        AbstractCard thisCard = this; // Keep a reference to this card to exclude it later
        int handSize = p.hand.size() - 1; // Exclude this card

        // Exhaust cards
        for (AbstractCard card : p.hand.group) {
            if (card != thisCard) {
                addToBot(new ExhaustSpecificCardAction(card, p.hand));
            }
        }
    }


    @Override
    public AbstractCard makeCopy() {
        return new FromHell();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1); // Upgrades both Vulnerable applied and Strength gained
            initializeDescription();
        }
    }
}