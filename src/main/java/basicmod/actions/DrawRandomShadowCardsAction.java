package basicmod.actions;

import basicmod.character.Deathbringer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static basemod.BaseMod.logger;

public class DrawRandomShadowCardsAction extends AbstractGameAction {
    private int numCardsToDraw;

    public DrawRandomShadowCardsAction(AbstractCreature source, int numCardsToDraw) {
        this.source = source;
        this.numCardsToDraw = numCardsToDraw;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    public void update() {
        ArrayList<AbstractCard> shadowCards = new ArrayList<>();
        ArrayList<AbstractCard> originalDrawPile = new ArrayList<>(AbstractDungeon.player.drawPile.group);

        // Iterate through draw pile to find all shadow cards
        for (AbstractCard c : originalDrawPile) {
            if (c.tags.contains(Deathbringer.Enums.SHADOW)) {
                shadowCards.add(c);
                AbstractDungeon.player.drawPile.removeCard(c);
            }
        }

        // Shuffle and draw up to numCardsToDraw shadow cards
        Collections.shuffle(shadowCards);
        for (int i = 0; i < Math.min(numCardsToDraw, shadowCards.size()); i++) {
            AbstractDungeon.player.drawPile.addToTop(shadowCards.get(i));
            addToBot(new DrawCardAction(1)); // Actually draw the card
        }

        // Put the remaining cards back into the draw pile
        for (AbstractCard c : originalDrawPile) {
            if (!AbstractDungeon.player.drawPile.contains(c)) {
                AbstractDungeon.player.drawPile.addToBottom(c);
            }
        }

        isDone = true;
    }
}