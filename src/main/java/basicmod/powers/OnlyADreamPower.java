package basicmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

import static basicmod.Deathbringer.makeID;
import static basemod.BaseMod.logger;

public class OnlyADreamPower extends BasePower {
    public static final String POWER_ID = makeID("OnlyADream");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public static ArrayList<AbstractCard> globalCardsToReplay = new ArrayList<>();

    private AbstractCard cardToReplay;

    public OnlyADreamPower(AbstractCreature owner, AbstractCard card) {
        super(POWER_ID, TYPE, TURN_BASED, owner, -1); // -1 because the power doesn't stack in the traditional sense
        this.cardToReplay = card.makeStatEquivalentCopy();
        globalCardsToReplay.add(this.cardToReplay);
        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        this.flash();
        for (AbstractCard card : globalCardsToReplay) {
            AbstractCard q = card.makeStatEquivalentCopy();
            q.freeToPlayOnce = true;
            q.exhaust = true;
            addToBot(new NewQueueCardAction(q, true, true, true));
        }
    }

    @Override
    public void updateDescription() {
        if (!globalCardsToReplay.isEmpty()) {
            this.description = "At the start of each of your turns, the following cards will be played and Exhausted: ";
            for (AbstractCard card : globalCardsToReplay) {
                this.description += FontHelper.colorString(card.name, "y") + ", ";
            }
            this.description = this.description.substring(0, this.description.length() - 2) + ".";
        } else {
            this.description = "";
        }
    }
}