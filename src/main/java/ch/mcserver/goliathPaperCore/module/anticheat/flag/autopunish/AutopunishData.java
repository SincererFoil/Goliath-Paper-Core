package ch.mcserver.goliathPaperCore.module.anticheat.flag.autopunish;

import ch.mcserver.goliathPaperCore.module.anticheat.flag.FlagData;

public record AutopunishData(

        FlagData flagData,
        PunishReason punishReason

) {

}
