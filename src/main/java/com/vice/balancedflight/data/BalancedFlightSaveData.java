package com.vice.balancedflight.data;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldSavedData;

import java.util.*;

public class BalancedFlightSaveData extends WorldSavedData
{
    public static BalancedFlightSaveData Instance;
    public static String ID = "BalancedFlightSaveData";
    public static HashMap<UUID, ArrayList<BlockPos>> PlayerFlightAnchors = new HashMap<>();

    public BalancedFlightSaveData()
    {
        super(ID);
        Instance = this;
    }


    public static ArrayList<BlockPos> getAnchorsForPlayer(PlayerEntity player)
    {
        return PlayerFlightAnchors.get(player.getGameProfile().getId());
    }

    public static boolean addAnchor(PlayerEntity player, BlockPos pos, boolean AllowMultiple)
    {
        ArrayList<BlockPos> anchors = getAnchorsForPlayer(player);

        if (!AllowMultiple && anchors.size() > 0)
            return false;

        anchors.add(pos);
        Instance.setDirty();
        return true;
    }

    @Override
    public void load(CompoundNBT nbt)
    {
        for (String key : nbt.getAllKeys())
        {
            ArrayList<BlockPos> ret = new ArrayList<>();
            CompoundNBT data = nbt.getCompound(key);

            for (String savedPositions : data.getAllKeys())
            {
                int[] pos = data.getIntArray(savedPositions);
                ret.add(new BlockPos(pos[0], pos[1], pos[2]));
            }

            PlayerFlightAnchors.put(UUID.fromString(key), ret);
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt)
    {
        for(Map.Entry<UUID, ArrayList<BlockPos>> kvp : PlayerFlightAnchors.entrySet())
        {
            CompoundNBT save = new CompoundNBT();

            int index = 0;
            for (BlockPos pos : kvp.getValue())
            {
                save.putIntArray(String.valueOf(index), new int[] {
                        pos.getX(),
                        pos.getY(),
                        pos.getZ()
                });

                index++;
            }

            nbt.put(kvp.getKey().toString(), save);
        }

        return nbt;
    }
}
