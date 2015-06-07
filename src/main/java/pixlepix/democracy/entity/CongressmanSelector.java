package pixlepix.democracy.entity;

import com.google.common.base.Predicate;
import pixlepix.democracy.data.EnumStage;

/**
 * Created by localmacaccount on 5/25/15.
 */
public class CongressmanSelector implements Predicate {

    public EnumStage stage;

    public CongressmanSelector(EnumStage stage) {
        this.stage = stage;

    }

    @Override
    public boolean apply(Object e) {
        return e instanceof EntityCongressman && !((EntityCongressman) e).isSpeaker && ((EntityCongressman) e).type == stage;
    }
}
