package basicmod.cards.attacks;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.powers.StranglePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

public class NothingPersonal extends BaseCard {
    public static final String ID = makeID("NothingPersonal");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            2
    );

    private static final int DAMAGE = 13;
    private static final int UPG_DAMAGE = 17;
    private static final int STRANGLE = 3;
    private static final int UPG_STRANGLE = 1; // Upgrade amount

    public NothingPersonal() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(STRANGLE, UPG_STRANGLE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if (m == null || (m.intent != AbstractMonster.Intent.ATTACK &&
                m.intent != AbstractMonster.Intent.ATTACK_BUFF &&
                m.intent != AbstractMonster.Intent.ATTACK_DEBUFF &&
                m.intent != AbstractMonster.Intent.ATTACK_DEFEND)) {
            addToBot(new ApplyPowerAction(m, p, new StranglePower(m, magicNumber), magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NothingPersonal();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            upgradeMagicNumber(UPG_STRANGLE);
            initializeDescription();
        }
    }
}
