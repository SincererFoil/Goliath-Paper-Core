package ch.mcserver.goliathPaperCore.module.anticheat.flag.autopunish;

public enum PunishReason {
    CHEATING("Cheating"),
    MAKE_A_TICKET("Make-A-Ticket"),
    DUPING("Duping");

    private final String text;

    PunishReason(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
