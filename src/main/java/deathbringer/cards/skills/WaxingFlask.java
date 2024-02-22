package deathbringer.cards.skills;

import deathbringer.cards.BaseCard;
import deathbringer.character.Deathbringer;
import deathbringer.util.CardStats;
import deathbringer.util.ShadowUtility;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class WaxingFlask extends BaseCard {
    public static final String ID = makeID("WaxingFlask");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            3  // Energy cost
    );

    private static final int BLOCK = 10;
    private static final int POISON_AMOUNT = 10;

    public WaxingFlask() {
        super(ID, info);
        this.baseBlock = BLOCK;
        this.baseMagicNumber = this.magicNumber = POISON_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new GainBlockAction(p, p, block));

        addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, magicNumber), magicNumber));
        if (upgraded) {
            ShadowUtility.triggerGeneralShadowEffect(this);
        }
    }

    public void triggerHalfEffect() {
        // Gain half block twice
        int halfBlock = this.block / 2;
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, halfBlock));
        addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, halfBlock));

        // Apply half the poison amount to the same random enemy twice
        AbstractMonster randomTarget = AbstractDungeon.getRandomMonster();
        int halfPoison = this.magicNumber / 2;
        addToBot(new ApplyPowerAction(randomTarget, AbstractDungeon.player, new PoisonPower(randomTarget, AbstractDungeon.player, halfPoison), halfPoison));
        addToBot(new ApplyPowerAction(randomTarget, AbstractDungeon.player, new PoisonPower(randomTarget, AbstractDungeon.player, halfPoison), halfPoison));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName(); // Marks the card as upgraded and updates its name accordingly
            setBackgroundTexture("deathbringer/images/character/cardback/shadowskill.png", "deathbringer/images/character/cardback/shadowskill_p.png");
            setOrbTexture("deathbringer/images/character/cardback/shadowenergyorb.png", "deathbringer/images/character/cardback/shadowenergyorb_p.png");
            this.tags.add(Deathbringer.Enums.SHADOW); // Assuming SHADOW is a defined tag in your Enums for Shadow cards
            this.upgraded = true;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WaxingFlask();
    }
}
