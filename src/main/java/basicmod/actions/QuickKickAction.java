package basicmod.actions;

import basicmod.powers.OutburstPower;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class QuickKickAction extends AbstractGameAction {
    private final AbstractPlayer player;
    private final AbstractMonster monster;
    private final int damage;

    private final int magicNumber;

    public QuickKickAction(AbstractPlayer player, AbstractMonster monster, int damage, int magicNumber) {
        this.player = player;
        this.monster = monster;
        this.damage = damage;
        this.magicNumber = magicNumber;
    }

    @Override
    public void update() {
        if (monster.currentHealth < player.currentHealth) {
            addToBot(new StunMonsterAction(monster, player, 1));
            addToBot(new ApplyPowerAction(player, player, new OutburstPower(player, magicNumber), magicNumber));
        }
        isDone = true;
    }
}

