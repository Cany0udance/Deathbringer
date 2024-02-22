package deathbringer.character;

import basemod.abstracts.CustomEnergyOrb;
import basemod.abstracts.CustomPlayer;
import basemod.abstracts.CustomSavableRaw;
import basemod.animations.AbstractAnimation;
import deathbringer.cards.attacks.ShadowStrike;
import deathbringer.cards.attacks.Strike;
import deathbringer.cards.skills.Defend;
import deathbringer.cards.skills.Pocket;
import deathbringer.cards.skills.ShadowDefend;
import deathbringer.relics.ShatteredCompass;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static deathbringer.Deathbringer.characterPath;
import static deathbringer.Deathbringer.makeID;

public class Deathbringer extends CustomPlayer implements CustomSavableRaw {

    public static HashMap<String, Boolean> enemiesMarkedForExtinction;
    public static HashMap<String, Integer> pocketCardProgress;


    public static final int ENERGY_PER_TURN = 3;
    public static final int MAX_HP = 78;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    private static final String ID = makeID("Deathbringer");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    private static final String SHOULDER_1 = characterPath("shoulder.png");
    private static final String SHOULDER_2 = characterPath("shoulder2.png");
    private static final String CORPSE = characterPath("corpse.png");

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass DEATHBRINGER;
        @SpireEnum(name = "DEATHBRINGER")
        public static AbstractCard.CardColor CARD_COLOR;
        @SpireEnum(name = "DEATHBRINGER")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
        @SpireEnum
        public static AbstractCard.CardTags SHADOW;
        @SpireEnum
        public static AbstractCard.CardTags SHADOWPLAY;
        @SpireEnum
        public static AbstractCard.CardTags UMBRA;
        @SpireEnum
        public static AbstractCard.CardTags PENUMBRA;
    }

    public Deathbringer() {
        super(NAMES[0], Enums.DEATHBRINGER,
                new CustomEnergyOrb(null, null, null), //Energy Orb
                new AbstractAnimation() { //Change the animation line to this.
                    @Override
                    public Type type() {
                        return Type.NONE; //A NONE animation results in img being used instead.
                    }
                });

        initializeClass(characterPath("DeathbringerBattleActual2.png"),
                SHOULDER_2,
                SHOULDER_1,
                CORPSE, getLoadout(),
                20.0F, -5.0F, 200.0F, 250.0F,
                new EnergyManager(ENERGY_PER_TURN));
        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 220.0F * Settings.scale);

        enemiesMarkedForExtinction = new HashMap<>();
        pocketCardProgress = new HashMap<>();

    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        //List of IDs of cards for your starting deck.
        //If you want multiple of the same card, you have to add it multiple times.
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(ShadowStrike.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(ShadowDefend.ID);
        retVal.add(Pocket.ID);

        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        //IDs of starting relics. You can have multiple, but one is recommended.
        retVal.add(ShatteredCompass.ID);

        return retVal;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        //This card is used for the Gremlin card matching game.
        //It should be a non-strike non-defend starter card, but it doesn't have to be.
        return new Pocket();
    }

    /*- Below this is methods that you should *probably* adjust, but don't have to. -*/

    @Override
    public int getAscensionMaxHPLoss() {
        return 8; //Max hp reduction at ascension 14+
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        //These attack effects will be used when you attack the heart.
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        };
    }

    private final Color cardRenderColor = new Color(129f/255f, 20f/255f, 20f/255f, 1f); // Used for some vfx on moving cards (sometimes) (maybe)
    private final Color cardTrailColor = new Color(129f/255f, 20f/255f, 20f/255f, 1f); // Used for card trail vfx during gameplay.
    private final Color slashAttackColor = new Color(129f/255f, 20f/255f, 20f/255f, 1f); // Used for a screen tint effect when you attack the heart.
    @Override
    public Color getCardRenderColor() {
        return cardRenderColor;
    }

    @Override
    public Color getCardTrailColor() {
        return cardTrailColor;
    }

    @Override
    public Color getSlashAttackColor() {
        return slashAttackColor;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        //Font used to display your current energy.
        //energyNumFontRed, Blue, Green, and Purple are used by the basegame characters.
        //It is possible to make your own, but not convenient.
        return FontHelper.energyNumFontRed;
    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> panels = new ArrayList<>();
        panels.add(new CutscenePanel("deathbringer/images/ending/ending1.png", "BLOOD_SPLAT"));
        panels.add(new CutscenePanel("deathbringer/images/ending/ending2real.png", "combat/weightyImpact"));
        panels.add(new CutscenePanel("deathbringer/images/ending/ending3.png", "STANCE_ENTER_CALM"));
        return panels;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        //This occurs when you click the character's button in the character select screen.
        //See SoundMaster for a full list of existing sound effects, or look at BaseMod's wiki for adding custom audio.
        CardCrawlGame.sound.playA("ATTACK_DAGGER_2", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        //Similar to doCharSelectScreenSelectEffect, but used for the Custom mode screen. No shaking.
        return "ATTACK_DAGGER_2";
    }

    //Don't adjust these four directly, adjust the contents of the CharacterStrings.json file.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }
    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAMES[1];
    }
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }
    @Override
    public String getVampireText() {
        return TEXT[2]; //Generally, the only difference in this text is how the vampires refer to the player.
    }

    /*- You shouldn't need to edit any of the following methods. -*/

    //This is used to display the character's information on the character selection screen.
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                MAX_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this,
                getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return Enums.CARD_COLOR;
    }

    public void resetEnemiesMarkedForExtinction() {  // Add this method
        enemiesMarkedForExtinction.clear();
    }

    // At the beginning of each battle, mark monsters as needed


    public String getSensoryStoneText()
    {
        return "#r~DISHONOR.~ NL NL Following your #ycompass, you are guided to the location of your next hit; an uncharted Spire. As you approach the structure, you hear a rumbling sound, and a #p@purple@ #p@conglomerate@ emerges from the earth! NL NL Your fierce battle with the entity leaves you with many wounds and one less eye. Then, you hear a rumbling sound from behind you... #r@THWACK!@ You collapse, embarrassed by your performance. It all goes dark... NL NL ~\"I~ ~patched~ ~you~ ~up...\"~";
    }


    @Override
    public JsonElement onSaveRaw() {
        JsonObject json = new JsonObject();
        for (Map.Entry<String, Boolean> entry : enemiesMarkedForExtinction.entrySet()) {
            json.addProperty(entry.getKey(), entry.getValue());
        }
        return json;
    }

    @Override
    public void onLoadRaw(JsonElement jsonElement) {
        enemiesMarkedForExtinction.clear();
        JsonObject json = jsonElement.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            enemiesMarkedForExtinction.put(entry.getKey(), entry.getValue().getAsBoolean());
        }
    }

    @Override
    public AbstractPlayer newInstance() {
        //Makes a new instance of your character class.
        return new Deathbringer();
    }
}