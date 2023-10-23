package basicmod.actions;

import basicmod.cards.skills.Bulwark;
import basicmod.cards.skills.Strangulate;
import basicmod.effects.ProgressionUnsheatheEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.SpotlightEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
public class UpgradeBulwarkAction extends AbstractGameAction {
    private Bulwark bulwarkCard;

    public UpgradeBulwarkAction(Bulwark cardToUpgrade) {
        this.bulwarkCard = cardToUpgrade;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            if (bulwarkCard.canUpgrade()) {
                bulwarkCard.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(bulwarkCard);

                AbstractCard masterCard = getMasterDeckEquivalent(bulwarkCard);
                if (masterCard != null && masterCard.canUpgrade()) {
                    masterCard.upgrade();
                }

                // Adding visual effects
                AbstractDungeon.effectsQueue.add(new SpotlightEffect());
                AbstractDungeon.effectsQueue.add(new ProgressionUnsheatheEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(bulwarkCard.makeStatEquivalentCopy()));
            }
        }

        this.tickDuration();
    }

    public static AbstractCard getMasterDeckEquivalent(AbstractCard playingCard) {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.uuid.equals(playingCard.uuid)) {
                return c;
            }
        }
        return null;
    }
}