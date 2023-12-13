package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoBlockPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static basicmod.Deathbringer.makeID;

public class Nope extends BaseCard {
    public static final String ID = makeID("Nope");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            AbstractCard.CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2  // Cost
    );


    public Nope() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = 18;  // Initialize magicNumber and baseMagicNumber
        this.isInnate = true; // The card is Innate
        this.exhaust = true;  // The card is exhausted after use
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AddTemporaryHPAction(p, p, magicNumber));
        addToBot(new ApplyPowerAction(p, p, new NoBlockPower(p, 2, false), 2));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(5);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Nope();
    }
}
