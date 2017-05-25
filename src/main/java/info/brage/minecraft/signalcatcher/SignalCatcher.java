package info.brage.minecraft.signalcatcher;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.relauncher.Side;
import sun.misc.Signal;
import sun.misc.SignalHandler;

@Mod(modid = SignalCatcher.MODID, version = SignalCatcher.VERSION, acceptedMinecraftVersions = "[1.10.2,)", acceptableRemoteVersions = "*")
public class SignalCatcher {
    static final String MODID = "SignalCatcher";
    static final String VERSION = "1.0";

    private MinecraftServer server;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Don't run client-side, it makes kill not kill the whole client.
        if (event.getSide() != Side.SERVER) return;
        sig("TERM");
        sig("INT");
        sig("HUP");
    }

    @Mod.EventHandler
    public void serverSet(FMLServerAboutToStartEvent event) {
        server = event.getServer();
    }

    private final SignalHandler handler = new SignalHandler() {
        @Override
        public void handle(Signal signal) {
            System.out.println("Terminating from signal");
            if (server != null)
                server.initiateShutdown();
        }
    };

    private void sig(String s) {
        try {
            Signal.handle(new Signal(s), handler);
        } catch (Exception e) {
            System.out.printf("Failed to catch %s: %s", s, e);
        }
    }
}
