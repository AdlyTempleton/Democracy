package pixlepix.democracy.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
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

    public EntityCongressman(World world, EnumStage type) {

        this(world);
        this.type = type;
    }


    public EntityCongressman(World world) {
        super(world);
        Random r = new Random();
        for (int i = 0; i < r.nextInt(3) + 1; i++) {
            Ammendment amend = Ammendment.potentialAmendments.get(r.nextInt(Ammendment.potentialAmendments.size()));
            if (!desiredAmendments.contains(amend)) {
                desiredAmendments.add(amend);
            }
        }
        for (int i = 0; i < r.nextInt(2); i++) {
            Ammendment amend = Ammendment.potentialAmendments.get(r.nextInt(Ammendment.potentialAmendments.size()));
            if (!desiredAmendments.contains(amend) && !hatedAmendments.contains(amend)) {
                hatedAmendments.add(amend);
            }
        }
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
        for(int i : desiredArray){
            desiredAmendments.add(Ammendment.potentialAmendments.get(i));
        }

        int[] hatedArray = nbt.getIntArray("hatedAmendments");
        hatedAmendments = new ArrayList<Ammendment>();
        for(int i : hatedArray){
            hatedAmendments.add(Ammendment.potentialAmendments.get(i));
        }
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        data.writeBoolean(isSpeaker);
        data.writeInt(type.ordinal());

        data.writeByte(desiredAmendments.size());
        for(Ammendment ammendment: desiredAmendments){
            data.writeByte(ammendment.id);
        }

        data.writeByte(hatedAmendments.size());
        for(Ammendment ammendment: hatedAmendments){
            data.writeByte(ammendment.id);
        }
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        isSpeaker = data.readBoolean();
        type = EnumStage.values()[data.readByte()];

        int desiredSize = data.readByte();
        desiredAmendments = new ArrayList<Ammendment>();
        for(int i = 0; i < desiredSize; i++){
            desiredAmendments.add(Ammendment.potentialAmendments.get(data.readByte()));
        }

        int hatedSize = data.readByte();
        hatedAmendments = new ArrayList<Ammendment>();
        for(int i = 0; i < hatedSize; i++){
            hatedAmendments.add(Ammendment.potentialAmendments.get(data.readByte()));
        }
    }

    public void writeCustomNBT(NBTTagCompound nbt){
        nbt.setBoolean("isSpeaker", isSpeaker);
        nbt.setInteger("type", type.ordinal());

        int[] desiredArray = new int[desiredAmendments.size()];
        for(int i = 0; i < desiredArray.length; i++){
            desiredArray[i] = desiredAmendments.get(i).id;
        }
        nbt.setIntArray("desiredAmendments", desiredArray);

        int[] hatedArray = new int[hatedAmendments.size()];
        for(int i = 0; i < hatedArray.length; i++){
            hatedArray[i] = hatedAmendments.get(i).id;
        }
        nbt.setIntArray("hatedAmendments", hatedArray);
    }

    @Override
    protected boolean interact(EntityPlayer player) {
        ItemStack stack = player.inventory.getCurrentItem();
        if(stack != null && player.capabilities.isCreativeMode){
            Item item = stack.getItem();
            if(item == Items.diamond){
                isSpeaker = !isSpeaker;
            }
            if(item == Items.ender_pearl){
                type = EnumStage.COMMITTEE;
            }
            if(item == Items.blaze_rod){
                type = EnumStage.HOUSE;
            }
            if(item == Items.spider_eye){
                type = EnumStage.SENATE;
            }
            if(item == Items.redstone){
                type = EnumStage.PRESIDENT;
            }

        }
        return false;
    }

    @Override
    protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_) {
        return;
    }

    public int getOpinion(){
        BillData bill = BillData.bill;
        int points = -5;
        for(Ammendment ammendment : desiredAmendments){
            if(bill.amendments.contains(ammendment)){
                points += 10;
            }
        }

        for(Ammendment ammendment : hatedAmendments){
            if(bill.amendments.contains(ammendment)){
                points -= 10;
            }
        }
        if(bill.porkBarrelCongressmen.contains(this)){
            points += 20;
        }
        return points;
    }
    
    public boolean isVotingYes(){
         return  getOpinion() >= 0;
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
                worldObj.spawnParticle("heart", posX, posY + 2, posZ, 0, 0, 0);
            } else {
                worldObj.spawnParticle("reddust", posX, posY + 2, posZ, 0, 0, 0);

            }
        }
    }

    @Override
    public ItemStack[] getLastActiveItems() {
        return new ItemStack[0];
    }

}