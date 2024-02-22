package deathbringer.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static deathbringer.Deathbringer.makeID;

public class OpeningPower extends BasePower {
    public static final String POWER_ID = makeID("OpeningPower");
    private int vigorAmount;

    public OpeningPower(AbstractCreature owner, int strengthAmount, int vigorAmount) {
        super(POWER_ID, PowerType.BUFF, false, owner, strengthAmount);
        this.vigorAmount = vigorAmount;
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.vigorAmount += 6; // Assuming each stack adds 6 Vigor
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount, this.vigorAmount);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        boolean enemyAttacking = false;

        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            // Check if the enemy is capable of attacking and their intent is of type ATTACK.
            if (isEnemyCapableOfAttacking(m) && isAttackIntent(m.intent)) {
                enemyAttacking = true;
                break;
            }
        }

        if (!enemyAttacking) {
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
            addToBot(new ApplyPowerAction(owner, owner, new VigorPower(owner, vigorAmount), vigorAmount));
        }
    }

    private boolean isAttackIntent(AbstractMonster.Intent intent) {
        return intent == AbstractMonster.Intent.ATTACK ||
                intent == AbstractMonster.Intent.ATTACK_BUFF ||
                intent == AbstractMonster.Intent.ATTACK_DEBUFF ||
                intent == AbstractMonster.Intent.ATTACK_DEFEND;
    }

    private boolean isEnemyCapableOfAttacking(AbstractMonster monster) {
        // Implement logic to determine if the monster is incapacitated or should not be considered for attack.
        // This could include checks for stun status, sleep status, etc.
        return !monster.isDeadOrEscaped(); // Example condition
    }

}
