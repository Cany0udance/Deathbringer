package basicmod.cards.attacks;

import basicmod.actions.QuickKickAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static basicmod.Deathbringer.RED_BORDER_GLOW_COLOR;

public class QuickKick extends BaseCard {
    public static final String ID = makeID("QuickKick");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            2 // Energy cost
    );

    private static final int DAMAGE = 3;
    private static final int UPG_DAMAGE = 10;
    private static final int OUTBURST = 2;
    private static final int UPG_OUTBURST = 1; // Upgrade amount

    public QuickKick() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(OUTBURST, UPG_OUTBURST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal Damage
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

        // Call the custom QuickKickAction to evaluate stun logic
        addToBot(new QuickKickAction(p, m, damage, magicNumber));
    }

/*
    @Override
    public void triggerOnGlowCheck() {
        // Retrieve the OutburstPower from the player if it exists
        AbstractPower outburstPower = AbstractDungeon.player.getPower("Deathbringer:Outburst");

        if (outburstPower != null && outburstPower.amount >= 3) {
            // Glow red if playing this card will trigger the Outburst explosion
            this.glowColor = RED_BORDER_GLOW_COLOR.cpy();
        } else {
            // Glow blue otherwise
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
*/

    @Override
    public AbstractCard makeCopy() {
        return new QuickKick();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            upgradeMagicNumber(UPG_OUTBURST);
            initializeDescription();
        }
    }
}
