package basicmod.util;

import basicmod.Deathbringer;
import basicmod.cards.attacks.*;
import basicmod.cards.powers.Trade;
import basicmod.cards.skills.*;
import basicmod.cards.statuses.Slipup;
import basicmod.effects.ShadowEffect;
import basicmod.powers.LivelihoodPower;
import basicmod.powers.TradePower;
import basicmod.powers.VeilPower;
import basicmod.relics.OminousNote;
import basicmod.relics.RepairedCompass;
import basicmod.relics.ShatteredCompass;
import basicmod.relics.TornCloth;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.*;
import java.util.stream.Collectors;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShadowUtility {

    public static boolean shatteredTriggered = false;  // For tracking Shattered Compass
    public static boolean clothTriggered = false;  // For tracking Torn Cloth
    private static final Logger logger = LogManager.getLogger(ShadowUtility.class.getName());

    public static void triggerGeneralShadowEffect(AbstractCard playedCard) {
        AbstractPlayer p = AbstractDungeon.player;
        ArrayList<AbstractCard> shadowCards = new ArrayList<>();
        ArrayList<AbstractCard> umbraCards = new ArrayList<>();
        ArrayList<AbstractCard> penumbraCards = new ArrayList<>();
        List<String> nonTriggerCards = Arrays.asList(Slipup.ID, SubconsciousKiller.ID, Sanctuary.ID);

        shadowCards.add(playedCard);

        boolean hasShattered = p.hasRelic(ShatteredCompass.ID);
        boolean hasRepaired = p.hasRelic(RepairedCompass.ID);
        boolean hasCloth = p.hasRelic(TornCloth.ID);

        // Step 1: The card is played normally (This happens before this function is called)

        // Step 2: Another Shadow card is selected randomly
        for (AbstractCard c : p.hand.group) {
            if (c.tags.contains(basicmod.character.Deathbringer.Enums.SHADOW) && c != playedCard) {
                shadowCards.add(c);
                if (c.tags.contains(basicmod.character.Deathbringer.Enums.UMBRA)) {
                    umbraCards.add(c);
                } else if (c.tags.contains(basicmod.character.Deathbringer.Enums.PENUMBRA)) {
                    penumbraCards.add(c);
                }
            }
        }

        if (shadowCards.size() > 1) {
            shadowCards.remove(playedCard);
            AbstractCard randomShadowCard = null;
            if (!umbraCards.isEmpty()) {
                Collections.shuffle(umbraCards, AbstractDungeon.cardRandomRng.random);
                randomShadowCard = umbraCards.get(0);
            }
// Then consider regular Shadow cards if no Umbra card is available
            else if (!shadowCards.isEmpty()) {
                shadowCards.removeAll(umbraCards); // Remove Umbra cards to avoid duplication
                shadowCards.removeAll(penumbraCards); // Remove Penumbra cards for now
                Collections.shuffle(shadowCards, AbstractDungeon.cardRandomRng.random);
                randomShadowCard = shadowCards.isEmpty() ? null : shadowCards.get(0);
            }
// Finally, consider Penumbra cards if no other cards are available
            if (randomShadowCard == null && !penumbraCards.isEmpty()) {
                Collections.shuffle(penumbraCards, AbstractDungeon.cardRandomRng.random);
                randomShadowCard = penumbraCards.get(0);
            }

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

// Assuming 'playedCard' is the card initially played and 'randomShadowCard' is the "Lights Out" card
                if (randomShadowCard.cardID.equals(LightsOut.ID)) {
                    triggerLightsOutEffect(p, playedCard, randomShadowCard); // Pass both cards as exclusions
                } else {
                    // Existing logic for triggering half effect and shadowplay effect
                    if (!nonTriggerCards.contains(randomShadowCard.cardID)) {
                        triggerHalfEffect(randomShadowCard);
                        Deathbringer.shadowplaysThisCombat++;
                    }
                    if (randomShadowCard.tags.contains(basicmod.character.Deathbringer.Enums.SHADOWPLAY)) {
                        triggerShadowplayEffect(randomShadowCard);
                    }
                }

                // Introduce a delay before discarding
                AbstractDungeon.actionManager.addToBottom(new WaitAction(1.0f));

                final AbstractCard finalRandomShadowCard = randomShadowCard;

                // Step 5: The card is discarded
                if (!finalRandomShadowCard.cardID.equals(Quench.ID)) {
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        @Override
                        public void update() {
                            p.hand.moveToDiscardPile(finalRandomShadowCard);
                            this.isDone = true;
                        }
                    });
                }

                if (finalRandomShadowCard.tags.contains(basicmod.character.Deathbringer.Enums.PENUMBRA)) {
                    if (hasCloth && !clothTriggered) {
                        com.megacrit.cardcrawl.relics.AbstractRelic tornCloth = p.getRelic(TornCloth.ID);
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1), 1));
                        tornCloth.flash();  // Flash the relic
                        tornCloth.grayscale = true;  // Turn it grayscale
                        clothTriggered = true;
                    }
                }

                if (AbstractDungeon.player.hasRelic(OminousNote.ID)) {
                    applyOminousNoteEffect();
                }

                if (AbstractDungeon.player.hasPower(TradePower.POWER_ID)) {
                    TradePower tradePower = (TradePower) AbstractDungeon.player.getPower(TradePower.POWER_ID);
                    int tradePowerAmount = tradePower.amount; // Now you have the correct amount
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, tradePowerAmount));
                }
                if (AbstractDungeon.player.hasPower(LivelihoodPower.POWER_ID)) {
                    LivelihoodPower livelihoodPower = (LivelihoodPower) AbstractDungeon.player.getPower(LivelihoodPower.POWER_ID);
                    int livelihoodAmount = livelihoodPower.amount;

                    for (int i = 0; i < livelihoodAmount; i++) {
                        // Filter the hand for cards that can be upgraded
                        ArrayList<AbstractCard> upgradableCards = AbstractDungeon.player.hand.group.stream()
                                .filter(card -> card.canUpgrade() && !card.equals(finalRandomShadowCard) && !card.equals(playedCard))
                                .collect(Collectors.toCollection(ArrayList::new));

                        if (!upgradableCards.isEmpty()) {
                            // Choose a random card to upgrade
                            AbstractCard cardToUpgrade = upgradableCards.get(AbstractDungeon.cardRandomRng.random(upgradableCards.size() - 1));
                            cardToUpgrade.upgrade();
                            cardToUpgrade.superFlash();
                        }
                    }
                }

            }
        } else if (shadowCards.size() == 1) {
            AbstractCard randomShadowCard = playedCard;  // The played card is the only card

            if (hasShattered && !shatteredTriggered || hasRepaired) {
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
                    if (!nonTriggerCards.contains(randomShadowCard.cardID)) {
                        triggerHalfEffect(randomShadowCard);
                        Deathbringer.shadowplaysThisCombat++;
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
                    }

                    if (randomShadowCard.tags.contains(basicmod.character.Deathbringer.Enums.SHADOWPLAY)) {
                        triggerShadowplayEffect(randomShadowCard);
                    }

                    // Introduce a delay before discarding
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(1.0f));

                    if (randomShadowCard.tags.contains(basicmod.character.Deathbringer.Enums.PENUMBRA)) {
                        if (hasCloth && !clothTriggered) {
                            com.megacrit.cardcrawl.relics.AbstractRelic tornCloth = p.getRelic(TornCloth.ID);
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1), 1));
                            tornCloth.flash();  // Flash the relic
                            tornCloth.grayscale = true;  // Turn it grayscale
                            clothTriggered = true;
                        }
                    }

                    if (AbstractDungeon.player.hasRelic(OminousNote.ID)) {
                        applyOminousNoteEffect();
                    }

                    if (AbstractDungeon.player.hasPower(TradePower.POWER_ID)) {
                        TradePower tradePower = (TradePower) AbstractDungeon.player.getPower(TradePower.POWER_ID);
                        int tradePowerAmount = tradePower.amount;
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, tradePowerAmount));
                    }
                    if (AbstractDungeon.player.hasPower(LivelihoodPower.POWER_ID)) {
                        LivelihoodPower livelihoodPower = (LivelihoodPower) AbstractDungeon.player.getPower(LivelihoodPower.POWER_ID);
                        int livelihoodAmount = livelihoodPower.amount;

                        for (int i = 0; i < livelihoodAmount; i++) {
                            // Filter the hand for cards that can be upgraded
                            ArrayList<AbstractCard> upgradableCards = AbstractDungeon.player.hand.group.stream()
                                    .filter(card -> card.canUpgrade() && !card.equals(randomShadowCard) && !card.equals(playedCard))
                                    .collect(Collectors.toCollection(ArrayList::new));

                            if (!upgradableCards.isEmpty()) {
                                // Choose a random card to upgrade
                                AbstractCard cardToUpgrade = upgradableCards.get(AbstractDungeon.cardRandomRng.random(upgradableCards.size() - 1));
                                cardToUpgrade.upgrade();
                                cardToUpgrade.superFlash();
                            }
                        }
                    }
                }
            }
            // If none of the special conditions are met, the card is played as normal without the Shadow effects
        }
    }

    private static boolean firstShadowplayTriggered = false;

    public static void resetFirstShadowplayTrigger() {
        firstShadowplayTriggered = false;
    }


    public static void triggerShadowplayFromExternal(AbstractCard card) {
        triggerShadowplayEffect(card);
    }

    private static void triggerHalfEffect(AbstractCard card) {
        if (card instanceof Shadowstep) {
            ((Shadowstep) card).triggerHalfEffect();
        }
        if (card instanceof Protrusion) {
            ((Protrusion) card).triggerHalfEffect();
        }
        if (card instanceof Mantle) {
            ((Mantle) card).triggerHalfEffect();
        }
        if (card instanceof Injection) {
            ((Injection) card).triggerHalfEffect();
        }
        if (card instanceof Breach) {
            ((Breach) card).triggerHalfEffect();
        }
        if (card instanceof Quench) {
            ((Quench) card).triggerHalfEffect();
        }
        if (card instanceof Seethe) {
            ((Seethe) card).triggerHalfEffect();
        }
        if (card instanceof Malison) {
            ((Malison) card).triggerHalfEffect();
        }
        if (card instanceof Skulker) {
            ((Skulker) card).triggerHalfEffect();
        }
        if (card instanceof FinishTheJob) {
            ((FinishTheJob) card).triggerHalfEffect();
        }
        if (card instanceof NothingPersonal) {
            ((NothingPersonal) card).triggerHalfEffect();
        }
        if (card instanceof VanishingAct) {
            ((VanishingAct) card).triggerHalfEffect();
        }
        if (card instanceof Admire) {
            ((Admire) card).triggerHalfEffect();
        }
        if (card instanceof Shroud) {
            ((Shroud) card).triggerHalfEffect();
        }
        if (card instanceof Killswitch) {
            ((Killswitch) card).triggerHalfEffect();
        }

        if (card instanceof ShadowStrike) {
            ((ShadowStrike) card).triggerHalfEffect();
        }

        if (card instanceof ShadowDefend) {
            ((ShadowDefend) card).triggerHalfEffect();
        }

        if (card instanceof ConcealedBlade) {
            ((ConcealedBlade) card).triggerHalfEffect();
        }

        if (card instanceof Lookout) {
            ((Lookout) card).triggerHalfEffect();
        }

        if (card instanceof Nightlight) {
            ((Nightlight) card).triggerHalfEffect();
        }
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
        if (card instanceof Breach) {
            ((Breach) card).triggerShadowplayEffect();
        }
        if (card instanceof Quench) {
            ((Quench) card).triggerShadowplayEffect();
        }
        if (card instanceof Seethe) {
            ((Seethe) card).triggerShadowplayEffect();
        }
        if (card instanceof Skulker) {
            ((Skulker) card).triggerShadowplayEffect();
        }
        if (card instanceof FinishTheJob) {
            ((FinishTheJob) card).triggerShadowplayEffect();
        }
        if (card instanceof NothingPersonal) {
            ((NothingPersonal) card).triggerShadowplayEffect();
        }
        if (card instanceof VanishingAct) {
            ((VanishingAct) card).triggerShadowplayEffect();
        }
        if (card instanceof Slipup) {
            ((Slipup) card).triggerShadowplayEffect();
        }
        if (card instanceof Sanctuary) {
            ((Sanctuary) card).triggerShadowplayEffect();
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
        if (card instanceof Shroud) {
            ((Shroud) card).triggerShadowplayEffect();
        }
        if (card instanceof Nightlight) {
            ((Nightlight) card).triggerShadowplayEffect();
        }
        if (card instanceof LightsOut) {
            AbstractPlayer p = AbstractDungeon.player;
            AssassinFormLightsOut(p);
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
                if (card instanceof Breach) {
                    ((Breach) card).triggerShadowplayEffect();
                }
                if (card instanceof Quench) {
                    ((Quench) card).triggerShadowplayEffect();
                }
                if (card instanceof Seethe) {
                    ((Seethe) card).triggerShadowplayEffect();
                }
                if (card instanceof Skulker) {
                    ((Skulker) card).triggerShadowplayEffect();
                }
                if (card instanceof FinishTheJob) {
                    ((FinishTheJob) card).triggerShadowplayEffect();
                }
                if (card instanceof NothingPersonal) {
                    ((NothingPersonal) card).triggerShadowplayEffect();
                }
                if (card instanceof VanishingAct) {
                    ((VanishingAct) card).triggerShadowplayEffect();
                }
                if (card instanceof Slipup) {
                    ((Slipup) card).triggerShadowplayEffect();
                }
                if (card instanceof Sanctuary) {
                    ((Sanctuary) card).triggerShadowplayEffect();
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
                if (card instanceof Shroud) {
                    ((Shroud) card).triggerShadowplayEffect();
                }
                if (card instanceof Nightlight) {
                    ((Nightlight) card).triggerShadowplayEffect();
                }
                if (card instanceof LightsOut) {
                    AssassinFormLightsOut(p);
                }
            }
            firstShadowplayTriggered = true;
        }
    }

    private static void triggerLightsOutEffect(AbstractPlayer p, AbstractCard excludeCard1, AbstractCard excludeCard2) {
        for (AbstractCard c : new ArrayList<>(p.hand.group)) { // Use a copy of the group to avoid ConcurrentModificationException
            if (c.tags.contains(basicmod.character.Deathbringer.Enums.SHADOW) && c != excludeCard1 && c != excludeCard2) { // Exclude both specified cards
                if (c instanceof LightsOut) continue; // Skip "Lights Out"
                float cardX = c.hb.cX;
                float cardY = c.hb.cY;

                // Play the VFX
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new ShadowEffect(cardX, cardY), 0.1F));

                // Trigger half effect and shadowplay effect
                triggerHalfEffect(c);
                if (c.tags.contains(basicmod.character.Deathbringer.Enums.SHADOWPLAY)) {
                    triggerShadowplayEffect(c);
                }
                Deathbringer.shadowplaysThisCombat++;

                // Discard the card
                final AbstractCard cardToDiscard = c;
                if (!cardToDiscard.cardID.equals(Quench.ID)) { // Assuming Quench.ID is the card ID for Quench
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        @Override
                        public void update() {
                            p.hand.moveToDiscardPile(cardToDiscard);
                            cardToDiscard.triggerOnManualDiscard();
                            this.isDone = true;
                        }
                    });
                }

                if (cardToDiscard.tags.contains(basicmod.character.Deathbringer.Enums.PENUMBRA)) {
                    boolean hasCloth = p.hasRelic(TornCloth.ID);
                    if (hasCloth && !clothTriggered) {
                        com.megacrit.cardcrawl.relics.AbstractRelic tornCloth = p.getRelic(TornCloth.ID);
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1), 1));
                        tornCloth.flash();  // Flash the relic
                        tornCloth.grayscale = true;  // Turn it grayscale
                        clothTriggered = true;
                    }
                }

                if (AbstractDungeon.player.hasRelic(OminousNote.ID)) {
                    applyOminousNoteEffect();
                }

                if (AbstractDungeon.player.hasPower(TradePower.POWER_ID)) {
                    TradePower tradePower = (TradePower) AbstractDungeon.player.getPower(TradePower.POWER_ID);
                    int tradePowerAmount = tradePower.amount;
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, tradePowerAmount));
                }
                if (AbstractDungeon.player.hasPower(LivelihoodPower.POWER_ID)) {
                    LivelihoodPower livelihoodPower = (LivelihoodPower) AbstractDungeon.player.getPower(LivelihoodPower.POWER_ID);
                    int livelihoodAmount = livelihoodPower.amount;

                    for (int i = 0; i < livelihoodAmount; i++) {
                        // Filter the hand for cards that can be upgraded
                        ArrayList<AbstractCard> upgradableCards = AbstractDungeon.player.hand.group.stream()
                                .filter(card -> card.canUpgrade() && !card.equals(cardToDiscard))
                                .collect(Collectors.toCollection(ArrayList::new));

                        if (!upgradableCards.isEmpty()) {
                            // Choose a random card to upgrade
                            AbstractCard cardToUpgrade = upgradableCards.get(AbstractDungeon.cardRandomRng.random(upgradableCards.size() - 1));
                            cardToUpgrade.upgrade();
                            cardToUpgrade.superFlash();
                        }
                    }
                }
            }
        }
    }

    private static void AssassinFormLightsOut(AbstractPlayer p) {
        for (AbstractCard c : new ArrayList<>(p.hand.group)) {
            if (c.tags.contains(basicmod.character.Deathbringer.Enums.SHADOW)) {
                // Exclude "Lights Out" from triggering its own Shadowplay effect again
                if (c instanceof LightsOut) continue; // Skip "Lights Out"

                float cardX = c.hb.cX;
                float cardY = c.hb.cY;

                // Play the VFX
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new ShadowEffect(cardX, cardY), 0.1F));

                // Trigger half effect and shadowplay effect
                triggerHalfEffect(c);
                if (c.tags.contains(basicmod.character.Deathbringer.Enums.SHADOWPLAY)) {
                    triggerShadowplayEffect(c);
                }
                Deathbringer.shadowplaysThisCombat++;

                // Discard the card
                final AbstractCard cardToDiscard = c;
                if (!cardToDiscard.cardID.equals(Quench.ID)) {
                    AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                        @Override
                        public void update() {
                            p.hand.moveToDiscardPile(cardToDiscard);
                            cardToDiscard.triggerOnManualDiscard();
                            this.isDone = true;
                        }
                    });
                }

                if (cardToDiscard.tags.contains(basicmod.character.Deathbringer.Enums.PENUMBRA)) {
                    boolean hasCloth = p.hasRelic(TornCloth.ID);
                    if (hasCloth && !clothTriggered) {
                        com.megacrit.cardcrawl.relics.AbstractRelic tornCloth = p.getRelic(TornCloth.ID);
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1), 1));
                        tornCloth.flash();  // Flash the relic
                        tornCloth.grayscale = true;  // Turn it grayscale
                        clothTriggered = true;
                    }
                }

                if (AbstractDungeon.player.hasRelic(OminousNote.ID)) {
                    applyOminousNoteEffect();
                }

                if (AbstractDungeon.player.hasPower(TradePower.POWER_ID)) {
                    TradePower tradePower = (TradePower) AbstractDungeon.player.getPower(TradePower.POWER_ID);
                    int tradePowerAmount = tradePower.amount;
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, tradePowerAmount));
                }
                if (AbstractDungeon.player.hasPower(LivelihoodPower.POWER_ID)) {
                    LivelihoodPower livelihoodPower = (LivelihoodPower) AbstractDungeon.player.getPower(LivelihoodPower.POWER_ID);
                    int livelihoodAmount = livelihoodPower.amount;

                    for (int i = 0; i < livelihoodAmount; i++) {
                        // Filter the hand for cards that can be upgraded
                        ArrayList<AbstractCard> upgradableCards = AbstractDungeon.player.hand.group.stream()
                                .filter(card -> card.canUpgrade() && !card.equals(cardToDiscard))
                                .collect(Collectors.toCollection(ArrayList::new));

                        if (!upgradableCards.isEmpty()) {
                            // Choose a random card to upgrade
                            AbstractCard cardToUpgrade = upgradableCards.get(AbstractDungeon.cardRandomRng.random(upgradableCards.size() - 1));
                            cardToUpgrade.upgrade();
                            cardToUpgrade.superFlash();
                        }
                    }
                }
            }
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