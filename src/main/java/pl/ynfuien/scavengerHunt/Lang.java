package pl.ynfuien.scavengerHunt;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import pl.ynfuien.ydevlib.messages.LangBase;
import pl.ynfuien.ydevlib.messages.Messenger;
import pl.ynfuien.ydevlib.messages.colors.ColorFormatter;

import java.util.HashMap;

public class Lang extends LangBase {
    public enum Message implements LangBase.Message {
        PREFIX,
        COMMAND_USAGE,
        COMMAND_RELOAD_FAIL,
        COMMAND_RELOAD_SUCCESS,
        HUNT_ASSIGNED,
        HUNT_COMPLETED_HEADER,
        HUNT_COMPLETED_REWARD_MONEY,
        HUNT_COMPLETED_REWARD_EXPERIENCE,
        HUNT_COMPLETED_REWARD_ITEM,
        HUNT_ENDED,
        TASK_COMPLETED,
        COMMAND_HUNT_INFO_HEADER,
        COMMAND_HUNT_INFO_TASK_ITEM,
        COMMAND_HUNT_INFO_TASK_ITEM_COMPLETED,
        COMMAND_HUNT_INFO_TASK_MOB,
        COMMAND_HUNT_INFO_TASK_MOB_COMPLETED,
        COMMAND_HUNT_INFO_TASK_BIOME,
        COMMAND_HUNT_INFO_TASK_BIOME_COMPLETED,
        COMMAND_HUNT_INFO_TASK_TRADE,
        COMMAND_HUNT_INFO_TASK_TRADE_COMPLETED,
        COMMAND_HUNT_INFO_TASK_RIDE,
        COMMAND_HUNT_INFO_TASK_RIDE_COMPLETED,
        COMMAND_HUNT_INFO_FOOTER,
        COMMAND_HUNT_INFO_NO_ASSIGNMENT,
        COMMAND_HUNT_ASSIGN_USAGE,
        COMMAND_HUNT_ASSIGN_NO_PLAYER,
        COMMAND_HUNT_ASSIGN_SUCCESS,
        COMMAND_HUNT_ABORT_USAGE,
        COMMAND_HUNT_ABORT_NO_PLAYER,
        COMMAND_HUNT_ABORT_NO_HUNT,
        COMMAND_HUNT_ABORT_SUCCESS,
        ;

        /**
         * Gets name/path of this message.
         */
        @Override
        public String getName() {
            return name().toLowerCase().replace('_', '-');
        }

        /**
         * Gets original unformatted message.
         */
        public String get() {
            return Lang.get(getName());
        }

        /**
         * Gets message with parsed:
         * - {prefix} placeholder
         * - additional provided placeholders
         */
        public String get(HashMap<String, Object> placeholders) {
            return Lang.get(getName(), placeholders);
        }

        /**
         * Gets message with parsed:
         * - PlaceholderAPI
         * - {prefix} placeholder
         * - additional provided placeholders
         */
        public String get(CommandSender sender, HashMap<String, Object> placeholders) {
            return ColorFormatter.parsePAPI(sender, Lang.get(getName(), placeholders));
        }

        /**
         * Gets message as component with parsed:
         * - MiniMessage
         * - PlaceholderAPI
         * - {prefix} placeholder
         * - additional provided placeholders
         */
        public Component getComponent(CommandSender sender, HashMap<String, Object> placeholders) {
            return Messenger.parseMessage(sender, Lang.get(getName()), placeholders);
        }

        /**
         * Sends this message to provided sender.<br/>
         * Parses:<br/>
         * - MiniMessage<br/>
         * - PlaceholderAPI<br/>
         * - {prefix} placeholder
         */
        public void send(CommandSender sender) {
            this.send(sender, new HashMap<>());
        }

        /**
         * Sends this message to provided sender.<br/>
         * Parses:<br/>
         * - MiniMessage<br/>
         * - PlaceholderAPI<br/>
         * - {prefix} placeholder<br/>
         * - additional provided placeholders
         */
        public void send(CommandSender sender, HashMap<String, Object> placeholders) {
            Lang.sendMessage(sender, this, placeholders);
        }
    }
}
