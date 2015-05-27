package pixlepix.democracy.data;

/**
 * Created by localmacaccount on 5/23/15.
 */
public enum  EnumStage {

    COMMITTEE("Committee", "Commitee member", true),
    HOUSE("House", "Representative", true),
    SENATE("Senate", "Senator", false),
    PRESIDENT("Awaiting signature or veto", "President", false),
    HOUSEPOSTVETO("House - Overriding veto", "Representative", true),
    SENATEPOSTVETO("Senate - Overriding veto", "Senator", false);
    public String name;
    public String memberName;
    public boolean canAmmend;


    EnumStage(String name, String memberName, boolean canAmmend) {
        this.name = name;
        this.memberName = memberName;
        this.canAmmend = canAmmend;
    }


}
