package deathbringer.actions;

import deathbringer.character.Deathbringer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class DiscardShadowCardsAction extends AbstractGameAction {
    private int damagePerCard;
    private AbstractCreature target;

    public DiscardShadowCardsAction(AbstractCreature source, AbstractCreature target, int damagePerCard) {
        this.source = source;
        this.target = target;
        this.damagePerCard = damagePerCard;
        this.actionType = ActionType.DAMAGE; // Set action type if needed
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            // Temporary list to hold Shadow cards
            ArrayList<AbstractCard> shadowCards = new ArrayList<>();

            // Identify Shadow cards
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card.tags.contains(Deathbringer.Enums.SHADOW)) {
                    shadowCards.add(card);
                }
            }

            // Discard identified Shadow cards and schedule damage actions
            for (AbstractCard shadowCard : shadowCards) {
                AbstractDungeon.player.hand.moveToDiscardPile(shadowCard);
                shadowCard.triggerOnManualDiscard();
                // Schedule damage action for each discarded card
                addToBot(new DamageAction(target, new DamageInfo(source, damagePerCard, DamageInfo.DamageType.NORMAL), AttackEffect.BLUNT_LIGHT));
            }

            this.isDone = true;
        }
    }
}