package com.teamwizardry.wizardry.common.command;

import com.teamwizardry.wizardry.Wizardry;
import com.teamwizardry.wizardry.api.spell.SpellRingCache;
import com.teamwizardry.wizardry.api.spell.module.ModuleRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public class CommandReloadModules extends CommandBase {

	@NotNull
	@Override
	public String getName() {
		return "reloadmodules";
	}

	@NotNull
	@Override
	public String getUsage(@NotNull ICommandSender sender) {
		return "wizardry.command." + getName() + ".usage";
	}

	@Override
	public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, @NotNull String[] args) {
		SpellRingCache.INSTANCE.clear();
		ModuleRegistry.INSTANCE.loadUnprocessedModules();
		ModuleRegistry.INSTANCE.loadOverrideDefaults();
		ModuleRegistry.INSTANCE.copyMissingModules(Wizardry.proxy.getWizardryDirectory());
		ModuleRegistry.INSTANCE.loadModules(Wizardry.proxy.getWizardryDirectory());

		notifyCommandListener(sender, this, "wizardry.command." + getName() + ".success");
	}
}
