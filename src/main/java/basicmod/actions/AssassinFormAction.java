package basicmod.actions;

import basicmod.character.Deathbringer;
import basicmod.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import basicmod.powers.AssassinFormPower;

public class AssassinFormAction extends AbstractGameAction {
    private int stacks;
    private AssassinFormPower powerInstance;

    public AssassinFormAction(int stacks, AssassinFormPower powerInstance) {
        this.stacks = stacks;
        this.powerInstance = powerInstance;
    }

    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        boolean shadowPlayTriggered = false;

        for (int i = 0; i < stacks; i++) {
            for (AbstractCard card : p.hand.group) {
                if (card.tags.contains(Deathbringer.Enums.SHADOWPLAY)) {
                    shadowPlayTriggered = true;
                    ShadowUtility.triggerShadowplayFromExternal(card);
                }
            }
        }

        if (shadowPlayTriggered) {
            powerInstance.setFlash(); // Make the power flash if at least one Shadowplay effect was triggered
        }

        isDone = true;
    }
}
