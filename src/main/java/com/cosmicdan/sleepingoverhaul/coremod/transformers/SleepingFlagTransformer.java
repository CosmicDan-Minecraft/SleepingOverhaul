package com.cosmicdan.sleepingoverhaul.coremod.transformers;

import com.cosmicdan.cosmiclib.annotations.ForgeCoremodTransformer;
import com.cosmicdan.cosmiclib.annotations.ForgeEntryPoint;
import com.cosmicdan.cosmiclib.asm.AbstractHookMethodStartTransformer;
import com.cosmicdan.cosmiclib.util.EnvInfo;
import com.cosmicdan.sleepingoverhaul.common.eventhandlers.SleepingVoteHandler;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

/**
 * @author Daniel 'CosmicDan' Connolly
 */
@ForgeCoremodTransformer
public class SleepingFlagTransformer extends AbstractHookMethodStartTransformer {
	@Override
	public String getTargetClass() {
		return "net.minecraft.world.WorldServer";
	}

	@Override
	public String getTargetMethod() {
		return (EnvInfo.isDevEnv() ? "updateAllPlayersSleepingFlag" : "func_72854_c");
	}

	@Override
	public String getTargetDesc() {
		return "()V";
	}

	@Override
	public String getReason() {
		return "Ability to override the update when players are kicked as a result of sleep-vote action.";
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
		toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/cosmicdan/sleepingoverhaul/coremod/transformers/SleepingFlagTransformer", "shouldUpdate", "()Z", false));
		toInject.add(new JumpInsnNode(Opcodes.IFNE, labelContinue)); // if result is true, go to labelContinue
		toInject.add(new InsnNode(Opcodes.RETURN));
		toInject.add(labelContinue); // continue with vanilla behavior
		return toInject;
	}

	@ForgeEntryPoint
	public static boolean shouldUpdate() {
		return !SleepingVoteHandler.isKickVoteInProgress();
	}
}
