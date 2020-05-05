package com.teamwizardry.wizardry.asm;

import com.teamwizardry.wizardry.Wizardry;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by Demoniaque.
 */
public class WizardryTransformer implements IClassTransformer {

	private static final String CLASS_BLOCK = "net/minecraft/block/Block";
	private static final String CLASS_BLOCK_POS = "net/minecraft/util/math/BlockPos";
	private static final String CLASS_BLOCK_STATE = "net/minecraft/block/state/IBlockState";
	private static final String CLASS_BLOCK_ACCESS = "net/minecraft/world/IBlockAccess";
	private static final String CLASS_ENTITY_PLAYER = "net/minecraft/entity/player/EntityPlayer";
	private static final String CLASS_ENTITY = "net/minecraft/entity/Entity";
	private static final String CLASS_ENTITY_LIVING_BASE = "net/minecraft/entity/EntityLivingBase";
	private static final String CLASS_MOVER_TYPE = "net/minecraft/entity/MoverType";
	private static final String CLASS_EVENT = "net/minecraftforge/fml/common/eventhandler/Event";
	private static final String CLASS_MOVE_EVENT = "com/teamwizardry/wizardry/api/events/EntityMoveEvent";
	private static final String CLASS_TRAVEL_EVENT = "com/teamwizardry/wizardry/api/events/EntityTravelEvent";

	private static final String ASM_HOOKS = "com/teamwizardry/wizardry/asm/WizardryASMHooks";

	private static void log(String str) {
		System.out.println("[" + Wizardry.MODID + " ASM] " + str);
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		return basicClass;
	}

	private boolean equalsEither(String name, String srgName, String mcpName) {
		return name.equals(srgName) || name.equals(mcpName);
	}

	private byte[] transformSingleMethod(byte[] basicClass, String srgName, String mcpName,
										 String desc, Predicate<MethodNode> transformer) {
		return transformClass(basicClass, classNode -> {
			for (MethodNode methodNode : classNode.methods) {
				if (equalsEither(methodNode.name, srgName, mcpName) && methodNode.desc.equals(desc)) {
					if (transformer.test(methodNode)) {
						log("Successfully patched -> '" + srgName + "', '" + mcpName + "' with '" + desc + "'");
					} else {
						log("Failed to patch      -> '" + srgName + "', '" + mcpName + "' with '" + desc + "'");
					}
				}
			}
		});
	}

	private byte[] transformClass(byte[] basicClass, Consumer<ClassNode> transformer) {
		ClassReader reader = new ClassReader(basicClass);
		ClassNode classNode = new ClassNode();
		reader.accept(classNode, 0);

		transformer.accept(classNode);

		SafeClassWriter writer = new SafeClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES) {
			@Override
			protected String getCommonSuperClass(final String type1, final String type2) {
				//  the default asm merge uses Class.forName(), this prevents that.
				return "java/lang/Object";
			}
		};
		classNode.accept(writer);
		return writer.toByteArray();
	}
}
