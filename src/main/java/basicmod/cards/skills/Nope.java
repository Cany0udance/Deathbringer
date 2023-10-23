package basicmod.cards.skills;

import basicmod.cards.BaseCard;
import basicmod.character.Deathbringer;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static basicmod.Deathbringer.makeID;

public class Nope extends BaseCard {
    public static final String ID = makeID("Nope");
    private static final CardStats info = new CardStats(
            Deathbringer.Enums.CARD_COLOR,
            AbstractCard.CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            3  // Cost
    );

    public Nope() {
        super(ID, info);

        this.isInnate = true; // The card is Innate
        this.isEthereal = true; // The card is Ethereal
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Lose 5 HP if not upgraded; no HP loss if upgraded
        if (!upgraded) {
            addToBot(new LoseHPAction(p, p, 5));
        }

        // Check if it's a normal combat, then escape
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.getCurrRoom().smoked = true;
            AbstractDungeon.player.hideHealthBar();
            AbstractDungeon.player.isEscaping = true;
            AbstractDungeon.player.flipHorizontal = !AbstractDungeon.player.flipHorizontal;
            AbstractDungeon.overlayMenu.endTurnButton.disable();
            AbstractDungeon.player.escapeTimer = 2.5F;
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

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (super.canUse(p, m)) {
            if (p.hasPower("Surrounded")) {
                this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
                return false;
            }

            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (monster.type == AbstractMonster.EnemyType.BOSS) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }


    @Override
    public AbstractCard makeCopy() {
        return new Nope();
    }
}
