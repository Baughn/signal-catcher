package info.brage.minecraft.signalcatcher;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.server.MinecraftServer;
import sun.misc.Signal;
import sun.misc.SignalHandler;

@Mod(modid = SignalCatcher.MODID, version = SignalCatcher.VERSION, acceptedMinecraftVersions = "[1.7.10,)", acceptableRemoteVersions = "*")
public class SignalCatcher {
    static final String MODID = "SignalCatcher";
    static final String VERSION = "1.0";

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Don't run client-side, it makes kill not kill the whole client.
        if (event.getSide() != Side.SERVER) return;
        sig("TERM");
        sig("INT");
        sig("HUP");
    }

    private static final SignalHandler handler = new SignalHandler() {
        @Override
        public void handle(Signal signal) {
            System.out.println("Terminating from signal");
            MinecraftServer.getServer().initiateShutdown();
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
