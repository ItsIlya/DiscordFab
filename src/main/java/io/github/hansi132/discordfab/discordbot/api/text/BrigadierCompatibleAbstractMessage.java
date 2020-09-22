package io.github.hansi132.discordfab.discordbot.api.text;

import com.mojang.brigadier.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.internal.entities.AbstractMessage;
import net.dv8tion.jda.internal.entities.DataMessage;

/**
 * Here we extend the {@link DataMessage} and implement {@link Message} to \
 * add the ability to use the {@link net.dv8tion.jda.api.MessageBuilder} for Brigadier Messages in JDA
 *
 * @see net.dv8tion.jda.api.MessageBuilder
 * @see net.dv8tion.jda.api.MessageBuilder
 */
public abstract class BrigadierCompatibleAbstractMessage extends AbstractMessage implements Message {
    public BrigadierCompatibleAbstractMessage(String content, String nonce, boolean isTTS) {
        super(content, nonce, isTTS);
    }

    @Override
    public String getString() {
        return super.getContentRaw();
    }
}
