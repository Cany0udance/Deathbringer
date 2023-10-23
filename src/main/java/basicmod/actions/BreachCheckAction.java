package basicmod.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class BreachCheckAction extends AbstractGameAction {
    private AbstractMonster m;
    private AbstractPlayer p;
    private int initialBlock;

    public BreachCheckAction(AbstractMonster m, AbstractPlayer p, int initialBlock) {
        this.m = m;
        this.p = p;
        this.initialBlock = initialBlock;
    }

    public void update() {
        if (initialBlock > 0 && m.currentBlock == 0) {
            addToBot(new RemoveSpecificPowerAction(m, p, ArtifactPower.POWER_ID));
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, 5, false), 5));
            addToBot(new StunMonsterAction(m, p, 1));
        }
        this.isDone = true;
    }
}
