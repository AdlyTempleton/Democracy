package pixlepix.democracy.entity;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import pixlepix.democracy.data.EnumStage;

/**
 * Created by localmacaccount on 5/25/15.
 */
public class CongressmanSelector implements IEntitySelector {

    public EnumStage stage;

    public CongressmanSelector(EnumStage stage) {
        this.stage = stage;

    }

    @Override
    public boolean isEntityApplicable(Entity e) {
        return e instanceof EntityCongressman && !((EntityCongressman) e).isSpeaker && ((EntityCongressman) e).type == stage;
    }
}
