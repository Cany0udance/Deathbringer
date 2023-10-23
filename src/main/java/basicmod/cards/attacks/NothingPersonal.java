package basicmod.cards.attacks;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
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

    private static final int DAMAGE = 20;
    private static final int UPG_DAMAGE = 26;
    private static final int BLOCK = 10;
    private static final int UPG_BLOCK = 15;

    public NothingPersonal() {
        super(ID, info);
        setDamage(DAMAGE, UPG_DAMAGE);
        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.intent != AbstractMonster.Intent.ATTACK &&
                m.intent != AbstractMonster.Intent.ATTACK_BUFF &&
                m.intent != AbstractMonster.Intent.ATTACK_DEBUFF &&
                m.intent != AbstractMonster.Intent.ATTACK_DEFEND) {

            addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
            addToBot(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, block), block));
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, "That enemy intends to attack!", true));
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
            upgradeBlock(UPG_BLOCK - BLOCK);
            initializeDescription();
        }
    }
}
