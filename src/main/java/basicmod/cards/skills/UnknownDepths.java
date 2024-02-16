package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.cards.attacks.*;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class UnknownDepths extends BaseCard {
    public static final String ID = makeID("UnknownDepths");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    public UnknownDepths() {
        super(ID, info);
        this.cost = 1;
        this.costForTurn = 1;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> shadowCardList = new ArrayList<>();

        // Manually add cards to the list
        shadowCardList.add(new Admire());
        shadowCardList.add(new Breach());
        shadowCardList.add(new ConcealedBlade());
        shadowCardList.add(new FinishTheJob());
        shadowCardList.add(new Injection());
        shadowCardList.add(new Killswitch());
        shadowCardList.add(new LightsOut());
        shadowCardList.add(new Lookout());
        shadowCardList.add(new Malison());
        shadowCardList.add(new Mantle());
        shadowCardList.add(new Nightlight());
        shadowCardList.add(new NothingPersonal());
        shadowCardList.add(new Protrusion());
        shadowCardList.add(new Quench());
        shadowCardList.add(new Sanctuary());
        shadowCardList.add(new Seethe());
        shadowCardList.add(new Shadowstep());
        shadowCardList.add(new ShadowStrike());
        shadowCardList.add(new Shroud());
        shadowCardList.add(new Skulker());
        shadowCardList.add(new SubconsciousKiller());
        shadowCardList.add(new VanishingAct());


        AbstractCard randomCard = shadowCardList.get(AbstractDungeon.cardRandomRng.random(0, shadowCardList.size() - 1)).makeCopy();

        // Set cost to 0 if the card is not SubconsciousKiller or Sanctuary
        if (!randomCard.cardID.equals(SubconsciousKiller.ID) && !randomCard.cardID.equals(Sanctuary.ID) && !randomCard.cardID.equals(LightsOut.ID)) {
            randomCard.setCostForTurn(0);
        }

        // Add the card to player's hand
        addToBot(new MakeTempCardInHandAction(randomCard, 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new UnknownDepths();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
            initializeDescription();
        }
    }
}