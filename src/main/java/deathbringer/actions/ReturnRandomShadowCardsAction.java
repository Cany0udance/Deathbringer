package deathbringer.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import deathbringer.character.Deathbringer;

import java.util.ArrayList;

public class ReturnRandomShadowCardsAction extends AbstractGameAction {
    private AbstractPlayer player;
    private int numberOfCards;

    public ReturnRandomShadowCardsAction(AbstractPlayer player, int numberOfCards) {
        this.player = player;
        this.numberOfCards = numberOfCards;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> eligibleCards = new ArrayList<>();
            for (AbstractCard card : player.discardPile.group) {
                if (card.tags.contains(Deathbringer.Enums.SHADOW)) {
                    eligibleCards.add(card);
                }
            }

            int cardsToReturn = Math.min(numberOfCards, eligibleCards.size());
            ArrayList<AbstractCard> cardsToMove = new ArrayList<>();

            // Select cards to move without modifying the original list
            for (int i = 0; i < cardsToReturn; i++) {
                if (!eligibleCards.isEmpty()) {
                    int randomIndex = MathUtils.random(eligibleCards.size() - 1);
                    AbstractCard cardToReturn = eligibleCards.get(randomIndex);
                    cardsToMove.add(cardToReturn);
                    eligibleCards.remove(randomIndex);
                }
            }

            // Move selected cards and update their state
            for (AbstractCard card : cardsToMove) {
                if (player.hand.size() < 10) {
                    player.hand.addToHand(card);
                    player.discardPile.removeCard(card);
                    card.lighten(false);
                    card.applyPowers();
                }
            }

            // Refresh hand layout
            player.hand.refreshHandLayout();
            this.isDone = true;
        }
    }
}