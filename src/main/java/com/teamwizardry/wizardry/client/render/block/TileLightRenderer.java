package com.teamwizardry.wizardry.client.render.block;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.math.interpolate.numeric.InterpFloatInOut;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpColorHSV;
import com.teamwizardry.librarianlib.features.tesr.TileRenderHandler;
import com.teamwizardry.wizardry.Wizardry;
import com.teamwizardry.wizardry.api.NBTConstants;
import com.teamwizardry.wizardry.api.util.RandUtil;
import com.teamwizardry.wizardry.api.util.interp.InterpScale;
import com.teamwizardry.wizardry.common.tile.TileLight;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

/**
 * Created by Demoniaque.
 */
@SideOnly(Side.CLIENT)
public class TileLightRenderer extends TileRenderHandler<TileLight> {

	private TileLight te;

	public TileLightRenderer(TileLight te) {
		super(te);
		this.te = te;
	}

	@Override
	public void render(float partialTicks, int destroyStage, float alpha) {
		if (RandUtil.nextInt(4) == 0) {
			Color primaryColor;
			Color secondaryColor;
			if (te.getModule() != null) {
				primaryColor = te.getModule().getPrimaryColor();
				secondaryColor = te.getModule().getSecondaryColor();
			} else {
				// NOTE: Usually should never happen, if tile entity is initialized correctly in ModuleEffectLight.
				primaryColor = new Color(0xAA00AA);    // Purple color.
				secondaryColor = new Color(0x000000);
			}

			ParticleBuilder glitter = new ParticleBuilder(30);
			glitter.setRender(new ResourceLocation(Wizardry.MODID, NBTConstants.MISC.SPARKLE_BLURRED));
			glitter.setAlphaFunction(new InterpFloatInOut(0.3f, 0.3f));
			glitter.setColorFunction(new InterpColorHSV(Color.CYAN, Color.BLUE));
			glitter.setScaleFunction(new InterpScale((float) RandUtil.nextDouble(1, 3), 0));
			ParticleSpawner.spawn(glitter, te.getWorld(), new StaticInterp<>(new Vec3d(te.getPos()).add(0.5, 0.5, 0.5)), 1, 0, (i, build) -> {
				build.setMotion(new Vec3d(
						RandUtil.nextDouble(-0.01, 0.01),
						RandUtil.nextDouble(0, 0.03),
						RandUtil.nextDouble(-0.01, 0.01)));

				if (RandUtil.nextBoolean()) {
					build.setColorFunction(new InterpColorHSV(primaryColor, secondaryColor));
				} else {
					build.setColorFunction(new InterpColorHSV(secondaryColor, primaryColor));
				}
			});
		}
	}
}
