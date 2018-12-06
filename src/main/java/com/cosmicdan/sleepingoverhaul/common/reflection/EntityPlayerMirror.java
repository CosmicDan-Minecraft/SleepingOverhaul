package com.cosmicdan.sleepingoverhaul.common.reflection;

import com.cosmicdan.cosmiclib.reflection.ConstructorMirror;
import com.cosmicdan.cosmiclib.reflection.FieldMirror;
import com.cosmicdan.cosmiclib.reflection.MethodMirror;
import com.cosmicdan.cosmiclib.obfuscation.ObfuscatedString;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

@SuppressWarnings("unused")
@UtilityClass
@Log4j2(topic = "SleepingTweaks/EntityPlayerMirror")
public final class EntityPlayerMirror {
	public static final MethodMirror<Boolean> METHOD___BED_IN_RANGE
			= new MethodMirror<>(new ObfuscatedString("func_190774_a", "bedInRange"), EntityPlayer.class, BlockPos.class, EnumFacing.class);
	public static final MethodMirror<Object> METHOD___SPAWN_SHOULDER_ENTITIES
			= new MethodMirror<>(new ObfuscatedString("func_192030_dh", "spawnShoulderEntities"), EntityPlayer.class);
	public static final MethodMirror<Object> METHOD___SET_RENDER_OFFSET_FOR_SLEEP
			= new MethodMirror<>(new ObfuscatedString("func_175139_a", "setRenderOffsetForSleep"), EntityPlayer.class, EnumFacing.class);
	public static final FieldMirror<Boolean> FIELD___SLEEPING
			= new FieldMirror<>(new ObfuscatedString("field_71083_bS", "sleeping"), EntityPlayer.class);
	public static final FieldMirror<Integer> FIELD___SLEEP_TIMER
			= new FieldMirror<>(new ObfuscatedString("field_71076_b", "sleepTimer"), EntityPlayer.class);
	public static final ConstructorMirror CONSTRUCTOR_SLEEP_ENEMY_PREDICATE
			= new ConstructorMirror("net.minecraft.entity.player.EntityPlayer$SleepEnemyPredicate", EntityPlayer.class);
}
