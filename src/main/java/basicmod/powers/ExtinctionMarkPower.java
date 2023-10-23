package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import basicmod.effects.ExtinctionDustEffect;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import static basemod.BaseMod.logger;

public class ExtinctionMarkPower extends BasePower implements InvisiblePower, CloneablePowerInterface {
    public static final String POWER_ID = "Deathbringer:ExtinctionMarkPower";

    public ExtinctionMarkPower(final AbstractCreature owner) {
        super(POWER_ID, NeutralPowertypePatch.NEUTRAL, false, owner, 1);
        this.ID = POWER_ID;
        this.name = "Extinction Mark";
        this.owner = owner;
        this.type = NeutralPowertypePatch.NEUTRAL;
        this.isTurnBased = false;
        this.amount = 1;
    }

    @Override
    public void onDeath() {
        // Add ExhaustEmberEffect on the enemy's location
        AbstractDungeon.effectList.add(new ExtinctionDustEffect(owner.hb.cX, owner.hb.cY));

        // Play the "bell" sound at varying pitch
        CardCrawlGame.sound.playA("BELL", MathUtils.random(-0.2F, -0.3F));
    }


    @Override
    public void updateDescription() {
        this.description = "Instantly kills this monster when its turn starts.";
    }

    @Override
    public AbstractPower makeCopy() {
        return new ExtinctionMarkPower(this.owner);
    }
}
