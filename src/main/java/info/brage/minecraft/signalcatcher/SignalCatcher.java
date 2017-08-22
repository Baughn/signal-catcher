package info.brage.minecraft.signalcatcher;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.relauncher.Side;
import sun.misc.Signal;
import sun.misc.SignalHandler;

@Mod(modid = SignalCatcher.MODID, version = SignalCatcher.VERSION, acceptedMinecraftVersions = "[1.10.2,)", acceptableRemoteVersions = "*")
public class SignalCatcher {
    static final String MODID = "signal-catcher";
    static final String VERSION = "1.1";

    private MinecraftServer server;

    private static int signalCount = 0;

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
            if (signalCount == 0) {
                System.out.println("Terminating from signal");
                if (server != null) {
                    server.initiateShutdown();
                } else {
                    System.out.println("...but no server found? Dazed, confused and doing nothing.");
                }
                System.out.println("A second signal will trigger standard Forge shutdown, a third will trigger abrupt termination.");
            } else if (signalCount == 1) {
                System.out.println("Caught second signal, triggering Forge-standard shutdown.");
                FMLCommonHandler.instance().exitJava(1, false);
            } else {
                System.out.println("Caught third signal, triggering abrupt termination. Corruption is possible.");
                FMLCommonHandler.instance().exitJava(2, true);
            }
            signalCount++;
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
