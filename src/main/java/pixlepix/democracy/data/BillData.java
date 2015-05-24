package pixlepix.democracy.data;

import pixlepix.democracy.entity.EntityCongressman;

import java.util.ArrayList;

/**
 * Created by localmacaccount on 5/23/15.
 */
public class BillData {
    public static BillData bill;
    
    public ArrayList<Ammendment> amendments = new ArrayList<Ammendment>();
    
    public ArrayList<EntityCongressman> porkBarrelCongressmen = new ArrayList<EntityCongressman>();
    
    public EnumStage stage = EnumStage.COMMITTEE;
    
    public static void init(){
        bill = new BillData();
    }
}
