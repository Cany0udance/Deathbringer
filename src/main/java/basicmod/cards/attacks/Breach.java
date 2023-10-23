package basicmod.cards.attacks;

import basicmod.actions.BreachCheckAction;
import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import static basemod.BaseMod.logger;

public class Breach extends BaseCard {
    public static final String ID = makeID("Breach");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            2 // Energy cost
    );

    private static final int DAMAGE = 3;
    private static final int UPG_DAMAGE = 4;
    private static final int MULTI_HITS = 3;
    private static final int UPG_MULTI_HITS = 4;

    public Breach() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MULTI_HITS, UPG_MULTI_HITS);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int initialBlock = m.currentBlock;
        for (int i = 0; i < magicNumber; ++i) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
        addToBot(new BreachCheckAction(m, p, initialBlock));
    }



    @Override
    public AbstractCard makeCopy() {
        return new Breach();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            upgradeMagicNumber(UPG_MULTI_HITS - MULTI_HITS);
            initializeDescription();
        }
    }
}