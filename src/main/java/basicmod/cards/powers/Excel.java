package basicmod.cards.powers;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.ExcelPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Excel extends BaseCard {
    public static final String ID = makeID("Excel");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1 // Energy cost
    );

    private static final int MAX_HP_GAIN = 7;
    private static final int UPG_MAX_HP_GAIN = 3;

    public Excel() {
        super(ID, info);
        setMagic(MAX_HP_GAIN, UPG_MAX_HP_GAIN);
        this.isInnate = true;
        this.tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // The action to apply the power will be created later
        addToBot(new ApplyPowerAction(p, p, new ExcelPower(p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Excel();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.actionManager.turn <= 1) {
            return true;
        }
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_MAX_HP_GAIN);
            initializeDescription();
        }
    }
}
