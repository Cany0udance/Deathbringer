package basicmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.UUID;

public class ModifyMagicAction extends AbstractGameAction {
    private UUID uuid;

    public ModifyMagicAction(UUID targetUUID, int amount) {
        this.setValues(this.target, this.source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.uuid = targetUUID;
    }

    public void update() {
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card.uuid.equals(this.uuid)) {
                card.baseMagicNumber += this.amount;
                if (card.baseMagicNumber < 0) {
                    card.baseMagicNumber = 0;
                }
                card.magicNumber = card.baseMagicNumber;
                card.isMagicNumberModified = true;
            }
        }

        this.isDone = true;
    }
}