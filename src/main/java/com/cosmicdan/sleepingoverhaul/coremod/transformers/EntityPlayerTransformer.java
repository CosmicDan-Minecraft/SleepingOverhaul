package com.cosmicdan.sleepingoverhaul.coremod.transformers;

import com.cosmicdan.cosmiclib.asm.AbstractInsnTransformer;
import com.cosmicdan.cosmiclib.asmhelper.ASMHelper;
import com.cosmicdan.cosmiclib.util.EnvInfo;
import com.cosmicdan.sleepingoverhaul.common.ModConfig;
import lombok.extern.log4j.Log4j2;
import net.minecraft.entity.player.EntityPlayer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
@Log4j2(topic = "CosmicLib/EntityPlayerTransformer")
public class EntityPlayerTransformer extends AbstractInsnTransformer<JumpInsnNode> {
	@Override
	public String getTargetClass() {
		return "net.minecraft.entity.player.EntityPlayer";
	}

	@Override
	public String getTargetMethod() {
		return (EnvInfo.isDevEnv() ? "onUpdate" : "func_70071_h_");
	}

	@Override
	public String getTargetDesc() {
		return "()V";
	}

	@Override
	public String getReason() {
		return "Additional check after isDaytime() to provide autoWakeAtDawn config capability";
	}

	@Override
	public boolean doesModifyClassNode() {
		return false;
	}

	@Override
	public ClassNode injectClass(ClassNode toInject) {
		return null;
	}

	@Override
	public JumpInsnNode getTargetNode(MethodNode m) {
		AbstractInsnNode firstInvokeVirtual = ASMHelper.findFirstInstructionWithOpcode(m, Opcodes.INVOKEVIRTUAL);
		MethodInsnNode nextInvokeVirtual = (MethodInsnNode) firstInvokeVirtual;
		JumpInsnNode targetNode = null;
		while (nextInvokeVirtual != ASMHelper.findLastInstructionWithOpcode(m, Opcodes.INVOKEVIRTUAL)) {
			nextInvokeVirtual = (MethodInsnNode) ASMHelper.findNextInstructionWithOpcode(nextInvokeVirtual, Opcodes.INVOKEVIRTUAL);
			if (nextInvokeVirtual.name.equals(EnvInfo.isDevEnv() ? "isDaytime" : "func_72935_r")) {
				// found "world.isDaytime()" check, get the next jump node
				targetNode = (JumpInsnNode) ASMHelper.findNextInstructionWithOpcode(nextInvokeVirtual, Opcodes.IFEQ);
			}
		}
		return targetNode;
	}

	@Override
	public boolean shouldInjectOpsBeforeNode() {
		return false;
	}

	@Override
	public InsnList injectOps(JumpInsnNode targetNode) {
		InsnList toInject = new InsnList();
		toInject.add(new VarInsnNode(Opcodes.ALOAD, 0)); // (EntityPlayer) this
		toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/cosmicdan/sleepingoverhaul/coremod/transformers/EntityPlayerTransformer", "allowSleepDuringDay", "(Lnet/minecraft/entity/player/EntityPlayer;)Z", false));
		toInject.add(new JumpInsnNode(Opcodes.IFNE, targetNode.label));
		return toInject;
	}

	public static boolean allowSleepDuringDay(EntityPlayer player) {
		return ModConfig.CMC.allowSleepDuringDay;
	}
}
