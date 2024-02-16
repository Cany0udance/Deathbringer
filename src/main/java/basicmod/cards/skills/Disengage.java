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
            0  // Energy cost
    );

    private static final int BASE_BLOCK = 4;
    private static final int UPGRADE_PLUS_BLOCK = 2;  // Increase block by 2 when upgraded

    public Disengage() {
        super(ID, info);
        this.baseBlock = BASE_BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Always gain block once
        addToBot(new GainBlockAction(p, p, this.baseBlock));

        // Repeat for each Penumbra (or Umbra if upgraded) card in hand
        for (AbstractCard c : p.hand.group) {
            if (c.tags.contains(Deathbringer.Enums.PENUMBRA) || (upgraded && c.tags.contains(Deathbringer.Enums.UMBRA))) {
                addToBot(new GainBlockAction(p, p, this.baseBlock));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Disengage();
    }
}
