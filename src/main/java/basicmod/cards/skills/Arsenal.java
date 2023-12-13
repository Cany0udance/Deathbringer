package basicmod.cards.skills;


import basemod.BaseMod;
import basemod.interfaces.PostDrawSubscriber;
import basicmod.actions.ResetFlagAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.OutburstPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Arrays;

import static basicmod.Deathbringer.RED_BORDER_GLOW_COLOR;

public class Arsenal extends BaseCard implements PostDrawSubscriber {
    public static final String ID = makeID("Arsenal");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 10;
    private static final int UPG_BLOCK = 12;

    private static final int DRAW = 3;
    private static final int UPG_DRAW = 4;

    private boolean triggeredDraw = false;  // Add this flag

    public Arsenal() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
        magicNumber = baseMagicNumber = DRAW;
        BaseMod.subscribe(this);
    }

    public void setTriggeredDraw(boolean value) {
        this.triggeredDraw = value;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.triggeredDraw = true;
        addToBot(new DrawCardAction(p, magicNumber));
        addToBot(new ResetFlagAction(this));
    }

    @Override
    public void triggerOnGlowCheck() {
        // Retrieve the OutburstPower from the player if it exists
        AbstractPower outburstPower = AbstractDungeon.player.getPower("Deathbringer:Outburst");

        // Determine the threshold for the red glow based on whether the card is upgraded
        int redGlowThreshold = this.upgraded ? 1 : 2;

        if (outburstPower != null && outburstPower.amount >= redGlowThreshold) {
            // Glow red if playing this card will trigger the Outburst explosion
            this.glowColor = RED_BORDER_GLOW_COLOR.cpy();
        } else {
            // Glow blue otherwise
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }


    @Override
    public void receivePostDraw(AbstractCard c) {
        // Only act if triggeredDraw is true
        if (this.triggeredDraw) {
            ArrayList<String> shadowCardIDs = new ArrayList<>(Arrays.asList(
                    "Deathbringer:Mantle", "Deathbringer:Shroud", "Deathbringer:Protrusion",
                    "Deathbringer:Eclipse", "Deathbringer:Liability",
                    "Deathbringer:Injection", "Deathbringer:Admire", "Deathbringer:Expurgate",
                    "Deathbringer:Shadowstep", "Deathbringer:VanishingAct",
                    "Deathbringer:Sanctuary", "Deathbringer:SubconsciousKiller", "Deathbringer:ShadowStrike", "Deathbringer:ShadowDefend", "Deathbringer:ConcealedBlade", "Deathbringer:Intuition"
            ));
            if (shadowCardIDs.contains(c.cardID)) {
                addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new OutburstPower(AbstractDungeon.player, 1), 1));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Arsenal();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK - BLOCK);
            upgradeMagicNumber(UPG_DRAW - DRAW);
            initializeDescription();
        }
    }
}
