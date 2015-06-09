package pixlepix.democracy.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import pixlepix.democracy.ItemAmmendment;
import pixlepix.democracy.data.Ammendment;
import pixlepix.democracy.data.BillData;
import pixlepix.democracy.data.EnumStage;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by localmacaccount on 5/23/15.
 */
public class EntityCongressman extends EntityLiving implements IEntityAdditionalSpawnData {

    public ArrayList<Ammendment> desiredAmendments = new ArrayList<Ammendment>();
    public ArrayList<Ammendment> hatedAmendments = new ArrayList<Ammendment>();
    public EnumStage type;
    public boolean isSpeaker = false;
    private int timer = 0;

    public EntityCongressman(World world, EnumStage type) {

        this(world);
        this.type = type;
    }

    public EntityCongressman(World world) {
        super(world);

        type = EnumStage.HOUSE;
        Random r = new Random();
        for (int i = 0; i < r.nextInt(3) + 1; i++) {
            Ammendment amend = Ammendment.potentialAmendments.get(r.nextInt(Ammendment.potentialAmendments.size()));
            if (!desiredAmendments.contains(amend)) {
                desiredAmendments.add(amend);
            }
        }

        if (r.nextBoolean() && Ammendment.getPreferredBy(type).size() > 0) {
            Ammendment prop = Ammendment.getPreferredBy(type).get(r.nextInt(Ammendment.getPreferredBy(type).size()));
            if (!desiredAmendments.contains(prop)) {
                desiredAmendments.add(prop);
            }
        }

        for (int i = 0; i < r.nextInt(2); i++) {
            Ammendment amend = Ammendment.potentialAmendments.get(r.nextInt(Ammendment.potentialAmendments.size()));
            if (!desiredAmendments.contains(amend) && !hatedAmendments.contains(amend)) {
                hatedAmendments.add(amend);
            }
        }

        if (r.nextBoolean() && Ammendment.getHatedBy(type).size() > 0) {
            Ammendment prop = Ammendment.getHatedBy(type).get(r.nextInt(Ammendment.getHatedBy(type).size()));
            if (!desiredAmendments.contains(prop) && !hatedAmendments.contains(prop)) {
                hatedAmendments.add(prop);
            }
        }
    }

    @Override
    public boolean isNoDespawnRequired() {
        return true;
    }

    @Override
    protected boolean isMovementBlocked() {
        return true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        writeCustomNBT(nbt);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        readCustomNBT(nbt);
    }

    public void readCustomNBT(NBTTagCompound nbt) {
        isSpeaker = nbt.getBoolean("isSpeaker");
        type = EnumStage.values()[nbt.getInteger("type")];

        int[] desiredArray = nbt.getIntArray("desiredAmendments");
        desiredAmendments = new ArrayList<Ammendment>();
        for (int i : desiredArray) {
            desiredAmendments.add(Ammendment.potentialAmendments.get(i));
        }

        int[] hatedArray = nbt.getIntArray("hatedAmendments");
        hatedAmendments = new ArrayList<Ammendment>();
        for (int i : hatedArray) {
            hatedAmendments.add(Ammendment.potentialAmendments.get(i));
        }
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        data.writeBoolean(isSpeaker);
        data.writeInt(type.ordinal());

        data.writeByte(desiredAmendments.size());
        for (Ammendment ammendment : desiredAmendments) {
            data.writeByte(ammendment.id);
        }

        data.writeByte(hatedAmendments.size());
        for (Ammendment ammendment : hatedAmendments) {
            data.writeByte(ammendment.id);
        }
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        isSpeaker = data.readBoolean();
        type = EnumStage.values()[data.readInt()];

        int desiredSize = data.readByte();
        desiredAmendments = new ArrayList<Ammendment>();
        for (int i = 0; i < desiredSize; i++) {
            desiredAmendments.add(Ammendment.potentialAmendments.get(data.readByte()));
        }

        int hatedSize = data.readByte();
        hatedAmendments = new ArrayList<Ammendment>();
        for (int i = 0; i < hatedSize; i++) {
            hatedAmendments.add(Ammendment.potentialAmendments.get(data.readByte()));
        }
    }

    public void writeCustomNBT(NBTTagCompound nbt) {
        nbt.setBoolean("isSpeaker", isSpeaker);
        nbt.setInteger("type", type.ordinal());

        int[] desiredArray = new int[desiredAmendments.size()];
        for (int i = 0; i < desiredArray.length; i++) {
            desiredArray[i] = desiredAmendments.get(i).id;
        }
        nbt.setIntArray("desiredAmendments", desiredArray);

        int[] hatedArray = new int[hatedAmendments.size()];
        for (int i = 0; i < hatedArray.length; i++) {
            hatedArray[i] = hatedAmendments.get(i).id;
        }
        nbt.setIntArray("hatedAmendments", hatedArray);
    }


    @Override
    protected boolean interact(EntityPlayer player) {
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack != null) {
            Item item = stack.getItem();
            if (player.capabilities.isCreativeMode) {
                if (item == Items.diamond) {
                    isSpeaker = !isSpeaker;
                }
                if (item == Items.ender_pearl) {
                    type = EnumStage.COMMITTEE;
                }
                if (item == Items.blaze_rod) {
                    type = EnumStage.HOUSE;
                }
                if (item == Items.spider_eye) {
                    type = EnumStage.SENATE;
                }
                if (item == Items.redstone) {
                    type = EnumStage.PRESIDENT;
                }
                if (item == Items.diamond_sword) {
                    setDead();
                }

            } else if (stack.getItem() == Items.cooked_porkchop && !BillData.bill.porkBarrelCongressmen.contains(this)) {
                BillData.bill.porkBarrelCongressmen.add(this);
                if (!worldObj.isRemote) {
                    player.addChatComponentMessage(new ChatComponentText("Thanks for the pork barrel!"));
                }
            } else if (isSpeaker && item instanceof ItemAmmendment && type.canAmmend) {
                Ammendment ammendment = Ammendment.potentialAmendments.get(stack.getItemDamage());
                if (!BillData.bill.amendments.contains(ammendment)) {
                    if (!worldObj.isRemote) {

                        BillData.bill.amendments.add(ammendment);
                        player.addChatComponentMessage(new ChatComponentText(ammendment.name + " was added to the bill successfully"));
                    }

                }

            }
        } else if (isSpeaker && (BillData.bill.stage == this.type || ((BillData.bill.stage == EnumStage.HOUSEPOSTVETO) && type == EnumStage.HOUSE) || (BillData.bill.stage == EnumStage.SENATEPOSTVETO && type == EnumStage.SENATE))) {
            AxisAlignedBB axisAlignedBB = AxisAlignedBB.fromBounds(posX - 100, posY - 100, posZ - 100, posX + 100, posY + 100, posZ + 100);
            ArrayList<EntityCongressman> allCongressmen = (ArrayList<EntityCongressman>) worldObj.getEntitiesWithinAABB(EntityCongressman.class, axisAlignedBB, new CongressmanSelector(type));
            if (type != EnumStage.PRESIDENT) {
                int yea = 0;
                int nay = 0;
                for (EntityCongressman congressman : allCongressmen) {
                    if (congressman.isVotingYes()) {
                        yea += 1;
                    } else {
                        nay += 1;
                    }
                }
                if (!worldObj.isRemote) {
                    player.addChatComponentMessage(new ChatComponentText("Yea: " + yea));
                    player.addChatComponentMessage(new ChatComponentText("Nay: " + nay));
                }
                boolean successful = yea > nay;
                if (type == EnumStage.HOUSEPOSTVETO || type == EnumStage.SENATEPOSTVETO) {
                    int total = yea + nay;
                    successful = yea > (.75 * total);
                }
                if (successful) {

                    if (!worldObj.isRemote) {
                        player.addChatComponentMessage(new ChatComponentText("The bill passes!"));
                    }
                    if (type == EnumStage.SENATEPOSTVETO) {

                        if (!worldObj.isRemote) {
                            player.addChatComponentMessage(new ChatComponentText("The veto is overridden and the bill becomes law!"));

                            player.addChatComponentMessage(new ChatComponentText("You win!"));
                        }
                    } else {
                        EnumStage nextStage = EnumStage.COMMITTEE;
                        switch (type) {
                            case COMMITTEE:
                                nextStage = EnumStage.HOUSE;
                                break;
                            case HOUSEPOSTVETO:
                                nextStage = EnumStage.SENATEPOSTVETO;
                                break;
                            case HOUSE:
                                nextStage = EnumStage.SENATE;
                                break;
                            case SENATE:
                                nextStage = EnumStage.PRESIDENT;
                                break;
                        }

                        if (!worldObj.isRemote) {
                            player.addChatComponentMessage(new ChatComponentText("The bill moves on to the " + nextStage.name));

                            BillData.bill.stage = nextStage;
                        }
                    }
                }
            } else {
                if (isVotingYes()) {

                    if (!worldObj.isRemote) {
                        player.addChatComponentMessage(new ChatComponentText("The president signs the bill. You win!"));
                    }
                } else if (!worldObj.isRemote) {
                    player.addChatComponentMessage(new ChatComponentText("The bill is vetoed. It moves back into the house"));

                    BillData.bill.stage = EnumStage.HOUSEPOSTVETO;
                }
            }

        }


        return true;
    }

    @Override
    protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_) {
        return;
    }

    public int getOpinion() {
        BillData bill = BillData.bill;
        int points = -15;
        for (Ammendment ammendment : desiredAmendments) {
            if (bill.amendments.contains(ammendment)) {
                points += 10;
            }
        }

        for (Ammendment ammendment : hatedAmendments) {
            if (bill.amendments.contains(ammendment)) {
                points -= 10;
            }
        }
        if (bill.porkBarrelCongressmen.contains(this)) {
            points += 222;
        }
        points -= 2 * bill.porkBarrelCongressmen.size();
        return points;
    }

    public boolean isVotingYes() {
        return (getPeerPressure() + getOpinion()) >= 0;
    }

    public int getPeerPressure() {

        AxisAlignedBB axisAlignedBB = AxisAlignedBB.fromBounds(posX - 8, posY - 8, posZ - 8, posX + 8, posY + 8, posZ + 8);
        ArrayList<EntityCongressman> allCongressmen = (ArrayList<EntityCongressman>) worldObj.getEntitiesWithinAABB(EntityCongressman.class, axisAlignedBB, new CongressmanSelector(type));
        double pressure = 0;
        for (EntityCongressman entityCongressman : allCongressmen) {
            if (entityCongressman != this) {
                pressure += entityCongressman.getOpinion() / 4;
            }
        }
        return (int) pressure;
    }


    @Override
    public ItemStack getHeldItem() {
        return isVotingYes() ? new ItemStack(Items.emerald) : new ItemStack(Items.redstone);
    }

    @Override
    public ItemStack getEquipmentInSlot(int p_71124_1_) {
        return null;
    }

    @Override
    public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {

    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (worldObj.isRemote) {
            if (isVotingYes()) {
                timer++;
                if (timer >= 20) {
                    timer = 0;
                    worldObj.spawnParticle(EnumParticleTypes.HEART, posX, posY + 2.5, posZ, 0, 0, 0);
                }
            } else {
                if (worldObj.getTotalWorldTime() % 10 == 0) {
                    worldObj.spawnParticle(EnumParticleTypes.REDSTONE, posX, posY + 2.5, posZ, 0, 0, 0);
                }

            }
        }
    }
}
