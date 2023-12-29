package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;


import static basicmod.Deathbringer.makeID;
import static basemod.BaseMod.logger;

public class OutburstPower extends BasePower implements CloneablePowerInterface {
    public static final String POWER_ID = makeID("Outburst");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public OutburstPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    // Add a flag at class level to track if an Outburst is already triggered.
    private boolean isOutburstInProgress = false;

    @Override
    public void stackPower(int stackAmount) {
        this.flash();
        this.amount += stackAmount;

        if (this.amount >= 5 && !isOutburstInProgress) {
            isOutburstInProgress = true; // Set the flag to true
            logger.info("Triggering Outburst explosion");

            this.flash();
            int damageToEnemies = 25;

            if (AbstractDungeon.player.hasPower(ThresholdPower.POWER_ID)) {
                damageToEnemies += AbstractDungeon.player.getPower(ThresholdPower.POWER_ID).amount;
            }

            AbstractPlayer p = AbstractDungeon.player;

            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    addToBot(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F));
                }
            }

            addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(damageToEnemies, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));

         //   if (p.hasPower(ThrivePower.POWER_ID)) {
         //       ThrivePower thrivePower = (ThrivePower) p.getPower(ThrivePower.POWER_ID);
         //       thrivePower.onOutburstTrigger();
         //   }

            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    isOutburstInProgress = false; // Reset the flag to false
                    isDone = true;
                }
            });

            addToBot(new RemoveSpecificPowerAction(p, p, this));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new OutburstPower(this.owner, this.amount);
    }
}