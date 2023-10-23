package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.effects.NightlightEffect;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.GrandFinalEffect;

public class Nightlight extends BaseCard {
    public static final String ID = makeID("Nightlight");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1  // Energy cost
    );

    private static final int WEAK_AMOUNT = 1;  // Apply 1 Weak
    private static boolean vfxPlayed = false;

    public Nightlight() {
        super(ID, info);
        setMagic(WEAK_AMOUNT);  // Set the Weak effect values
        this.selfRetain = true;  // Card will be retained
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DiscardAction(p, p, 1, false));  // Discard a card (always 1)
        // Apply Weak to EVERYONE
        if (!vfxPlayed) {
            this.addToBot(new VFXAction(new NightlightEffect(), 2.0F));
            vfxPlayed = true;  // Set the VFX status to true once played
        }
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new ApplyPowerAction(monster, p, new WeakPower(monster, this.magicNumber, false)));
        }
        if (!upgraded) {
            addToBot(new ApplyPowerAction(p, p, new WeakPower(p, this.magicNumber, false)));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    public static void resetNightlightVFX() {
        vfxPlayed = false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Nightlight();
    }
}