package basicmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class OnslaughtAction extends AbstractGameAction {
    private DamageInfo info;
    private int hpLoss;
    private int hitCount; // New variable to keep track of the number of hits

    public OnslaughtAction(AbstractCreature target, DamageInfo info, int hpLoss, int hitCount) {
        this.info = info;
        this.target = target;
        this.actionType = ActionType.WAIT;
        this.attackEffect = AttackEffect.BLUNT_HEAVY;
        this.hpLoss = hpLoss;
        this.duration = Settings.ACTION_DUR_FAST;
        this.hitCount = hitCount; // Initialize the hitCount
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (target != null && target.currentHealth > 0 && hitCount < 40) { // Check for maximum hits
                this.addToBot(new DamageAction(target, info, AttackEffect.BLUNT_HEAVY));
                this.addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, hpLoss));
                this.addToBot(new OnslaughtAction(target, info, hpLoss, hitCount + 1)); // Increment hitCount
            }
        }

        this.tickDuration();
        this.isDone = true;
    }
}