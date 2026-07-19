package dev.elrol.arrowlib.neoforge;

import dev.elrol.arrowlib.Arrowlib;
import dev.elrol.arrowlib.libs.ArrowConstants;
import net.neoforged.fml.common.Mod;

@Mod(ArrowConstants.MODID)
public final class ArrowlibNeoForge {
    public ArrowlibNeoForge() {
        // Run our common setup.
        Arrowlib.init();
    }
}
