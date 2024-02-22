package deathbringer.potions;

import basemod.BaseMod;
import deathbringer.cards.attacks.*;
import deathbringer.cards.skills.*;
import deathbringer.character.Deathbringer;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;

import static deathbringer.Deathbringer.makeID;

public class TwistedPair extends BasePotion {
    public static final String ID = makeID(TwistedPair.class.getSimpleName());

    private static final Color LIQUID_COLOR = CardHelper.getColor(0, 0, 0);
    private static final Color HYBRID_COLOR = CardHelper.getColor(0, 0, 0);
    private static final Color SPOTS_COLOR = CardHelper.getColor(0, 0, 0);

    public TwistedPair() {
        super(ID, 2, PotionRarity.UNCOMMON, PotionSize.S, LIQUID_COLOR, HYBRID_COLOR, SPOTS_COLOR);
        playerClass = Deathbringer.Enums.DEATHBRINGER;
        this.tips.add(new PowerTip(TipHelper.capitalize(BaseMod.getKeywordTitle("deathbringer:shadow")), BaseMod.getKeywordDescription("deathbringer:shadow")));
        labOutlineColor = new Color(129f/255f, 20f/255f, 20f/255f, 1f);
    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            ArrayList<AbstractCard> shadowCardList = new ArrayList<>();

            shadowCardList.add(new Admire());
            shadowCardList.add(new Breach());
            shadowCardList.add(new ConcealedBlade());
            shadowCardList.add(new FinishTheJob());
            shadowCardList.add(new Injection());
            shadowCardList.add(new Killswitch());
            shadowCardList.add(new Lookout());
            shadowCardList.add(new Malison());
            shadowCardList.add(new Mantle());
            shadowCardList.add(new Nightlight());
            shadowCardList.add(new NothingPersonal());
            shadowCardList.add(new Protrusion());
            shadowCardList.add(new Quench());
            shadowCardList.add(new Seethe());
            shadowCardList.add(new Shadowstep());
            shadowCardList.add(new ShadowStrike());
            shadowCardList.add(new Shroud());
            shadowCardList.add(new Skulker());
            shadowCardList.add(new VanishingAct());

            AbstractCard randomCard = shadowCardList.get(AbstractDungeon.cardRandomRng.random(0, shadowCardList.size() - 1)).makeCopy();
            randomCard.modifyCostForCombat(-randomCard.cost); // Set its cost to 0 for the turn

            addToBot(new MakeTempCardInHandAction(randomCard, this.potency));
        }
    }

    @Override
    public String getDescription() {
        return potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
    }

}
