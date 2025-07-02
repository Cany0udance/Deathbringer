package deathbringer.actions;

import basemod.BaseMod;
import deathbringer.cards.skills.LightsOut;
import deathbringer.character.Deathbringer;
import deathbringer.interfaces.ShadowEffectable;
import deathbringer.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import deathbringer.powers.AssassinFormPower;

import java.util.ArrayList;
import java.util.List;

public class AssassinFormAction extends AbstractGameAction {
    private int stacks;
    private AssassinFormPower powerInstance;

    public AssassinFormAction(int stacks, AssassinFormPower powerInstance) {
        this.stacks = stacks;
        this.powerInstance = powerInstance;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        boolean shadowPlayTriggered = false;

        for (int i = 0; i < stacks; i++) {
            // Create a snapshot of shadowplay cards to avoid concurrent modification
            List<AbstractCard> shadowplayCards = new ArrayList<>();
            for (AbstractCard card : p.hand.group) {
                if (card.tags.contains(Deathbringer.Enums.SHADOWPLAY)) {
                    shadowplayCards.add(card);
                }
            }

            // Trigger each shadowplay effect
            for (AbstractCard card : shadowplayCards) {
                // Double-check card is still in hand
                if (p.hand.contains(card)) {
                    shadowPlayTriggered = true;
                    triggerShadowplayEffect(card);
                }
            }
        }

        if (shadowPlayTriggered) {
            powerInstance.setFlash();
        }

        isDone = true;
    }

    // AssassinFormAction.triggerShadowplayEffect()
    private void triggerShadowplayEffect(AbstractCard card) {
        if (card instanceof ShadowEffectable) {
            ((ShadowEffectable) card).triggerShadowplayEffect();
        } else {
            // Fallback for cards not yet converted to interface
            ShadowUtility.triggerShadowplayFromExternal(card);
        }
    }
}