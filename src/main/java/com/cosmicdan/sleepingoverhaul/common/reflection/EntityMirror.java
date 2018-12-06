package com.cosmicdan.sleepingoverhaul.common.reflection;

import com.cosmicdan.cosmiclib.obfuscation.ObfuscatedString;
import com.cosmicdan.cosmiclib.reflection.MethodMirror;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import net.minecraft.entity.Entity;

@SuppressWarnings("unused")
@UtilityClass
@Log4j2(topic = "SleepingTweaks/EntityMirror")
public final class EntityMirror {
	public static final MethodMirror<Object> METHOD___SET_SIZE
			= new MethodMirror<>(new ObfuscatedString("func_70105_a", "setSize"), Entity.class, float.class, float.class);
}
