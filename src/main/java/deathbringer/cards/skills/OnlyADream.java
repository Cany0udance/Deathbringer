package deathbringer.cards.skills;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.powers.OnlyADreamPower;
import deathbringer.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class OnlyADream extends BaseCard {
    public static final String ID = makeID("OnlyADream");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            3  // Initial energy cost
    );

    private static final int UPGRADE_COST = 2;

    public OnlyADream() {
        super(ID, info);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CardGroup validCards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : p.hand.group) {
            if (card.rarity == AbstractCard.CardRarity.BASIC || card.rarity == AbstractCard.CardRarity.COMMON) {
                validCards.addToTop(card);
            }
        }

        if (!validCards.isEmpty()) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractDungeon.gridSelectScreen.open(validCards, 1, "Choose a basic or common card to replay each turn.", false, false, false, false);
                    isDone = true;
                }
            });

            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    if (AbstractDungeon.gridSelectScreen.selectedCards.size() == 1) {
                        AbstractCard selectedCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                        addToBot(new ApplyPowerAction(p, p, new OnlyADreamPower(p, selectedCard), 1));
                        addToBot(new ExhaustSpecificCardAction(selectedCard, p.hand));
                        AbstractDungeon.gridSelectScreen.selectedCards.clear();
                        isDone = true;
                    }
                }
            });
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new OnlyADream();
    }
}
