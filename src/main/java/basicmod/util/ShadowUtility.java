package basicmod.util;

import basicmod.cards.attacks.*;
import basicmod.cards.skills.*;
import basicmod.cards.statuses.Slipup;
import basicmod.effects.ShadowEffect;
import basicmod.powers.VeilPower;
import basicmod.relics.OminousNote;
import basicmod.relics.RepairedCompass;
import basicmod.relics.ShatteredCompass;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShadowUtility {

    private static final List<String> EXCEPTION_CARD_IDS = Arrays.asList("Deathbringer:Imprudence", "Deathbringer:Hide", "Deathbringer:UnknownDepths", "Deathbringer:Trade", "Deathbringer:Arsenal");
    public static boolean shatteredTriggered = false;  // For tracking Shattered Compass
    public static boolean repairedTriggered = false;   // For tracking Repaired Compass
    private static final Logger logger = LogManager.getLogger(ShadowUtility.class.getName());

    public static void triggerGeneralShadowEffect(AbstractCard playedCard) {
        AbstractPlayer p = AbstractDungeon.player;
        ArrayList<AbstractCard> shadowCards = new ArrayList<>();
        List<String> nonTriggerCards = Arrays.asList("Deathbringer:Slipup", "Deathbringer:SubconsciousKiller", "Deathbringer:Sanctuary");


        boolean hasShattered = p.hasRelic(ShatteredCompass.ID);
        boolean hasRepaired = p.hasRelic(RepairedCompass.ID);

        // Step 1: The card is played normally (This happens before this function is called)

        // Step 2: Another Shadow card is selected randomly
        for (AbstractCard c : p.hand.group) {
            if (c.keywords.contains("deathbringer:shadow") && c != playedCard && !EXCEPTION_CARD_IDS.contains(c.cardID)) {                shadowCards.add(c);
            }
        }

        if (!shadowCards.isEmpty()) {
            Collections.shuffle(shadowCards, AbstractDungeon.cardRandomRng.random);
            AbstractCard randomShadowCard = shadowCards.get(0);

            // Capture the card's current coordinates
            float capturedX = randomShadowCard.hb.cX;
            float capturedY = randomShadowCard.hb.cY;

            // Step 3: The VFX plays
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new ShadowEffect(capturedX, capturedY)));

            AbstractMonster m = null;
            int attempts = 0;  // Counter to avoid infinite loops

            do {
                m = AbstractDungeon.getRandomMonster();
                attempts++;
                if (attempts >= 10) {
                    logger.warn("Exiting early: No valid targets found after 10 attempts.");
                    return;
                }
            } while (m == null || m.isDying || m.isDead);

            if (m != null && !m.isDying && !m.isDead) {

                if ((hasShattered && !shatteredTriggered) || (hasRepaired && !repairedTriggered)) {
                    if (!nonTriggerCards.contains(randomShadowCard.cardID)) {
                        if (randomShadowCard instanceof Shadowstep) {
                            ((Shadowstep) randomShadowCard).triggerFullEffect();
                        }
                        if (randomShadowCard instanceof Protrusion) {
                            ((Protrusion) randomShadowCard).triggerFullEffect();
                        }
                        if (randomShadowCard instanceof Mantle) {
                            ((Mantle) randomShadowCard).triggerFullEffect();
                        }
                        if (randomShadowCard instanceof Injection) {
                            ((Injection) randomShadowCard).triggerFullEffect();
                        }
                        if (randomShadowCard instanceof VanishingAct) {
                            ((VanishingAct) randomShadowCard).triggerFullEffect();
                        }
                        if (randomShadowCard instanceof Admire) {
                            ((Admire) randomShadowCard).triggerFullEffect();
                        }
                        if (randomShadowCard instanceof Eclipse) {
                            ((Eclipse) randomShadowCard).triggerFullEffect();
                        }
                        if (randomShadowCard instanceof Shroud) {
                            ((Shroud) randomShadowCard).triggerFullEffect();
                        }
                        if (randomShadowCard instanceof Liability) {
                            ((Liability) randomShadowCard).triggerFullEffect();
                        }

                        if (randomShadowCard instanceof Expurgate) {
                            ((Expurgate) randomShadowCard).triggerFullEffect();
                        }

                        if (randomShadowCard instanceof ShadowStrike) {
                            ((ShadowStrike) randomShadowCard).triggerFullEffect();
                        }

                        if (randomShadowCard instanceof ShadowDefend) {
                            ((ShadowDefend) randomShadowCard).triggerFullEffect();
                        }

                        if (randomShadowCard instanceof ConcealedBlade) {
                            ((ConcealedBlade) randomShadowCard).triggerFullEffect();
                        }

                        if (randomShadowCard instanceof Intuition) {
                            ((Intuition) randomShadowCard).triggerFullEffect();
                        }

                        if (hasShattered) {
                            com.megacrit.cardcrawl.relics.AbstractRelic shatteredCompass = p.getRelic(ShatteredCompass.ID);
                            shatteredCompass.flash();  // Flash the relic
                            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, shatteredCompass));
                            shatteredCompass.grayscale = true;  // Turn it grayscale
                            shatteredTriggered = true;
                        }

                        if (hasRepaired) {
                            com.megacrit.cardcrawl.relics.AbstractRelic repairedCompass = p.getRelic(RepairedCompass.ID);
                            repairedCompass.flash();  // Flash the relic
                            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, repairedCompass));
                            repairedCompass.grayscale = true;  // Turn it grayscale
                            repairedTriggered = true;
                        }
                    }

                } else {
                    if (randomShadowCard instanceof Shadowstep) {
                        ((Shadowstep) randomShadowCard).triggerHalfEffect();
                    }
                    if (randomShadowCard instanceof Protrusion) {
                        ((Protrusion) randomShadowCard).triggerHalfEffect();
                    }
                    if (randomShadowCard instanceof Mantle) {
                        ((Mantle) randomShadowCard).triggerHalfEffect();
                    }
                    if (randomShadowCard instanceof Injection) {
                        ((Injection) randomShadowCard).triggerHalfEffect();
                    }
                    if (randomShadowCard instanceof VanishingAct) {
                        ((VanishingAct) randomShadowCard).triggerHalfEffect();
                    }
                    if (randomShadowCard instanceof Admire) {
                        ((Admire) randomShadowCard).triggerHalfEffect();
                    }
                    if (randomShadowCard instanceof Eclipse) {
                        ((Eclipse) randomShadowCard).triggerHalfEffect();
                    }
                    if (randomShadowCard instanceof Shroud) {
                        ((Shroud) randomShadowCard).triggerHalfEffect();
                    }
                    if (randomShadowCard instanceof Liability) {
                        ((Liability) randomShadowCard).triggerHalfEffect();
                    }

                    if (randomShadowCard instanceof Expurgate) {
                        ((Expurgate) randomShadowCard).triggerHalfEffect();
                    }

                    if (randomShadowCard instanceof ShadowStrike) {
                        ((ShadowStrike) randomShadowCard).triggerHalfEffect();
                    }

                    if (randomShadowCard instanceof ShadowDefend) {
                        ((ShadowDefend) randomShadowCard).triggerHalfEffect();
                    }

                    if (randomShadowCard instanceof ConcealedBlade) {
                        ((ConcealedBlade) randomShadowCard).triggerHalfEffect();
                    }

                    if (randomShadowCard instanceof Intuition) {
                        ((Intuition) randomShadowCard).triggerFullEffect();
                    }

                }

                // This block will run regardless of the relic conditions above
                if (randomShadowCard.keywords.contains("deathbringer:shadowplay")) {
                    triggerShadowplayEffect(randomShadowCard);
                }

                // Introduce a delay before discarding
                AbstractDungeon.actionManager.addToBottom(new WaitAction(1.0f));

                // Step 5: The card is discarded
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                    @Override
                    public void update() {
                        p.hand.moveToDiscardPile(randomShadowCard);
                        this.isDone = true;
                    }
                });

                if (AbstractDungeon.player.hasRelic(OminousNote.ID)) {
                    applyOminousNoteEffect();
                }
            }
        }
    }

    private static boolean firstShadowplayTriggered = false;

    public static void resetFirstShadowplayTrigger() {
        firstShadowplayTriggered = false;
    }


    public static void triggerShadowplayFromExternal(AbstractCard card) {
        triggerShadowplayEffect(card);
    }


    private static void triggerShadowplayEffect(AbstractCard card) {
        // Implement your Shadowplay-specific effects here
        if (card instanceof Protrusion) {
            ((Protrusion) card).triggerShadowplayEffect();
        }
        if (card instanceof Mantle) {
            ((Mantle) card).triggerShadowplayEffect();
        }
        if (card instanceof SubconsciousKiller) {
            ((SubconsciousKiller) card).triggerShadowplayEffect();
        }
        if (card instanceof Injection) {
            ((Injection) card).triggerShadowplayEffect();
        }
        if (card instanceof VanishingAct) {
            ((VanishingAct) card).triggerShadowplayEffect();
        }
        if (card instanceof Eclipse) {
            ((Eclipse) card).triggerShadowplayEffect();
        }
        if (card instanceof Slipup) {
            ((Slipup) card).triggerShadowplayEffect();
        }
        if (card instanceof Sanctuary) {
            ((Sanctuary) card).triggerShadowplayEffect();
        }
        if (card instanceof Liability) {
            ((Liability) card).triggerShadowplayEffect();
        }
        if (card instanceof ShadowStrike) {
            ((ShadowStrike) card).triggerShadowplayEffect();
        }
        if (card instanceof ShadowDefend) {
            ((ShadowDefend) card).triggerShadowplayEffect();
        }
        if (card instanceof ConcealedBlade) {
            ((ConcealedBlade) card).triggerShadowplayEffect();
        }
        if (!firstShadowplayTriggered) {
            AbstractPlayer p = AbstractDungeon.player;
            int veilStack = 0;

            if (p.hasPower(VeilPower.POWER_ID)) {
                veilStack = p.getPower(VeilPower.POWER_ID).amount;
            }

            for (int i = 1; i <= veilStack; i++) {
                // Re-trigger the same Shadowplay effect for each Veil stack
                if (card instanceof Protrusion) {
                    ((Protrusion) card).triggerShadowplayEffect();
                }
                if (card instanceof Mantle) {
                    ((Mantle) card).triggerShadowplayEffect();
                }
                if (card instanceof SubconsciousKiller) {
                    ((SubconsciousKiller) card).triggerShadowplayEffect();
                }
                if (card instanceof Injection) {
                    ((Injection) card).triggerShadowplayEffect();
                }
                if (card instanceof VanishingAct) {
                    ((VanishingAct) card).triggerShadowplayEffect();
                }
                if (card instanceof Eclipse) {
                    ((Eclipse) card).triggerShadowplayEffect();
                }
                if (card instanceof Slipup) {
                    ((Slipup) card).triggerShadowplayEffect();
                }
                if (card instanceof Sanctuary) {
                    ((Sanctuary) card).triggerShadowplayEffect();
                }
                if (card instanceof Liability) {
                    ((Liability) card).triggerShadowplayEffect();
                }
                if (card instanceof ShadowStrike) {
                    ((ShadowStrike) card).triggerShadowplayEffect();
                }
                if (card instanceof ShadowDefend) {
                    ((ShadowDefend) card).triggerShadowplayEffect();
                }
                if (card instanceof ConcealedBlade) {
                    ((ConcealedBlade) card).triggerShadowplayEffect();
                }
            }
            firstShadowplayTriggered = true;
        }
    }

    private static void applyOminousNoteEffect() {
        ArrayList<AbstractMonster> targets = new ArrayList<>(AbstractDungeon.getCurrRoom().monsters.monsters);
        targets.removeIf(m -> m.isDying || m.isDead || m.halfDead);

        if (!targets.isEmpty()) {
            AbstractMonster target = targets.get(AbstractDungeon.cardRandomRng.random(0, targets.size() - 1));
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(target, AbstractDungeon.player, new PoisonPower(target, AbstractDungeon.player, 2), 2)
            );

            OminousNote relic = (OminousNote) AbstractDungeon.player.getRelic(OminousNote.ID);
            if (relic != null) {
                relic.flash();
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, relic));
            }
        }
    }
}