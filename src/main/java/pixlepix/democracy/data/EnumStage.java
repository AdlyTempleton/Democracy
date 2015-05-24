package pixlepix.democracy.data;

/**
 * Created by localmacaccount on 5/23/15.
 */
public enum  EnumStage {
    COMMITTEE("Committee", "Commitee member"),
    HOUSE("House","Representative"),
    SENATE("Senate", "Senator"),
    PRESIDENT("Awaiting signature or veto", "President"),
    HOUSEPOSTVETO("House - Overriding veto", "Representative"),
    SENATEPOSTVETO("Senate - Overriding veto", "Senator");
    public String name;
    public String memberName;

    EnumStage(String name, String memberName){
        this.name = name;
        this.memberName = memberName;
    }
}
