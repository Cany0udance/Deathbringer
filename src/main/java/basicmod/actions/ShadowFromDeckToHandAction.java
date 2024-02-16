package basicmod.actions;

import basemod.BaseMod;
import basicmod.character.Deathbringer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ShadowFromDeckToHandAction extends AbstractGameAction {
    private AbstractPlayer p;

    public ShadowFromDeckToHandAction() {
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.p.drawPile.group) {
                if (c.tags.contains(Deathbringer.Enums.SHADOW)) { // Assuming SHADOW is a custom tag for your Shadow cards
                    tmp.addToRandomSpot(c);
                }
            }

            if (tmp.isEmpty()) {
                this.isDone = true;
                return;
            }

            if (tmp.size() == 1) {
                AbstractCard card = tmp.getTopCard();
                moveCardToHand(card);
            } else {
                AbstractDungeon.gridSelectScreen.open(tmp, 1, "Select a Shadow card to add to your hand.", false);
                this.tickDuration();
                return;
            }
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                card.unhover();
                moveCardToHand(card);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        this.tickDuration();
    }

    private void moveCardToHand(AbstractCard card) {
        if (this.p.hand.size() == BaseMod.MAX_HAND_SIZE) {
            this.p.drawPile.moveToDiscardPile(card);
            this.p.createHandIsFullDialog();
        } else {
            this.p.drawPile.removeCard(card);
            this.p.hand.addToTop(card);
            this.p.hand.refreshHandLayout();
            this.p.hand.applyPowers();
        }
        this.isDone = true;
    }
}

