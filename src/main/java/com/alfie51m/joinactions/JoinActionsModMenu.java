package com.alfie51m.joinactions;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class JoinActionsModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            JoinActionsConfig.load();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Join Actions"));

            builder.setSavingRunnable(JoinActionsConfig::save);

            ConfigCategory category = builder.getOrCreateCategory(
                    Text.literal("Servers")
            );

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            for (int i = 0; i < 4; i++) {
                int index = i;

                category.addEntry(
                        entryBuilder.startStrField(
                                        Text.literal("Server " + (i + 1)),
                                        JoinActionsConfig.servers[i]
                                )
                                .setSaveConsumer(value ->
                                        JoinActionsConfig.servers[index] = value.toLowerCase()
                                )
                                .build()
                );

                category.addEntry(
                        entryBuilder.startStrField(
                                        Text.literal("Command " + (i + 1)),
                                        JoinActionsConfig.commands[i]
                                )
                                .setSaveConsumer(value ->
                                        JoinActionsConfig.commands[index] = value
                                )
                                .build()
                );
            }

            return builder.build();
        };
    }
}
