package ch.mcserver.goliathPaperCore.common.packet;

public final class GoliathPacket {

    private final ProtocolLibHook protocolLibHook;

    public GoliathPacket(ProtocolLibHook protocolLibHook) {
        this.protocolLibHook = protocolLibHook;
    }

    public boolean isEnabled() {
        return protocolLibHook.isEnabled();
    }

    public ProtocolLibHook getProtocolLibHook() {
        return protocolLibHook;
    }
}
