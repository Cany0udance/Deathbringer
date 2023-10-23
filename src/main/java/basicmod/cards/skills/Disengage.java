package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Disengage extends BaseCard {
    public static final String ID = makeID("Disengage");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0 // Cost
    );

    private static final int BASE_BLOCK = 4;
    private static final int ADDITIONAL_BLOCK = 6;
    private static final int UPG_ADDITIONAL_BLOCK = 10;

    public Disengage() {
        super(ID, info);
        setBlock(BASE_BLOCK);
        this.baseMagicNumber = this.magicNumber = ADDITIONAL_BLOCK;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeMagicNumber(UPG_ADDITIONAL_BLOCK - ADDITIONAL_BLOCK);
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));

        if (p.hasPower("Deathbringer:Outburst")) {
            addToBot(new GainBlockAction(p, p, this.magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Disengage();
    }
}
