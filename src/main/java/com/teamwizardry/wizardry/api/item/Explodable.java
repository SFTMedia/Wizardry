package com.teamwizardry.wizardry.api.item;

import com.teamwizardry.wizardry.lib.LibParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Saad on 6/30/2016.
 */
public interface Explodable {

	List<Integer> potions = new ArrayList<>();

	default void explode(Entity entityIn) {
		if (potions.isEmpty()) {
			potions.add(1);
			potions.add(3);
			potions.add(5);
			potions.add(8);
			potions.add(11);
			potions.add(12);
			potions.add(21);
		}

		Random rand = new Random();
		int range = 5;
		List<EntityLivingBase> entitys = entityIn.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(entityIn.posX - range, entityIn.posY - range, entityIn.posZ - range, entityIn.posX + range, entityIn.posY + range, entityIn.posZ + range));
		for (EntityLivingBase e : entitys)
			e.addPotionEffect(new PotionEffect(Potion.getPotionById(potions.get(rand.nextInt(potions.size()))), rand.nextInt(30) * 20, rand.nextInt(2) + 1));

		LibParticles.FIZZING_EXPLOSION(entityIn.getPositionVector());
	}
}
