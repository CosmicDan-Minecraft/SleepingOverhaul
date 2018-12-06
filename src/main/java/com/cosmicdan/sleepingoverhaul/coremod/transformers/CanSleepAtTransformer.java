package com.cosmicdan.sleepingoverhaul.coremod.transformers;

import com.cosmicdan.cosmiclib.annotations.ForgeCoremodTransformer;
import com.cosmicdan.cosmiclib.annotations.ForgeEntryPoint;
import com.cosmicdan.cosmiclib.asm.AbstractHookMethodStartTransformer;
import com.cosmicdan.sleepingoverhaul.common.ModConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
@ForgeCoremodTransformer
public class CanSleepAtTransformer extends AbstractHookMethodStartTransformer {
	@Override
	public String getTargetClass() {
		return "net.minecraft.world.WorldProvider";
	}

	@Override
	public String getTargetMethod() {
		// this is a Forge-added method so it's never obfuscated
		return "canSleepAt";
	}

	@Override
	public String getTargetDesc() {
		return "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/WorldProvider$WorldSleepResult;";
	}

	@Override
	public String getReason() {
		return "Capability to override, allowing sleeping in any dimension";
	}

	@Override
	public boolean doesModifyClassNode() {
		return false;
	}

	@Override
	public ClassNode doClassInjection(final ClassNode toInject) {
		return null;
	}

	@Override
	public InsnList doNodeInjection(final AbstractInsnNode targetNode) {
		final InsnList toInject = new InsnList();
		final LabelNode labelContinue = new LabelNode();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 1)); // push first parameter (EntityPlayer) onto stack
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 2)); // push second parameter (BlockPos) onto stack
		toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/cosmicdan/sleepingoverhaul/coremod/transformers/CanSleepAtTransformer", "canSleepHere", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/math/BlockPos;)Z", false));
		toInject.add(new JumpInsnNode(Opcodes.IFEQ, labelContinue)); // if result is false, go to labelContinue
		toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/world/WorldProvider$WorldSleepResult", "ALLOW", "Lnet/minecraft/world/WorldProvider$WorldSleepResult;"));
		toInject.add(new InsnNode(Opcodes.ARETURN)); // return the ALLOW value
		toInject.add(labelContinue); // continue with vanilla behavior
		return toInject;
	}

	@SuppressWarnings("unused")
	@ForgeEntryPoint
	public static boolean canSleepHere(final EntityPlayer player, final BlockPos pos) {
		return ModConfig.RESTRICTIONS.sleepAnywhere;
	}
}
