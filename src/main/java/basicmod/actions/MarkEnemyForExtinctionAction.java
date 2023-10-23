package basicmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import basicmod.character.Deathbringer;  // import your character class
import static basemod.BaseMod.logger;

public class MarkEnemyForExtinctionAction extends AbstractGameAction {

    private String enemyID;

    public MarkEnemyForExtinctionAction(AbstractCreature target, String enemyID) {
        this.target = target;
        this.enemyID = enemyID;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        logger.info("Entered MarkEnemyForExtinctionAction.update");
        if (!this.isDone) {
            Deathbringer.enemiesMarkedForExtinction.put(enemyID, true);
            this.isDone = true;
        }
        logger.info("Added monster ID to enemiesMarkedForExtinction: " + enemyID);
    }
}
