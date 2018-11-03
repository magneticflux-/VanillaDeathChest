package com.therandomlabs.vanilladeathchest;

import java.util.List;
import com.therandomlabs.vanilladeathchest.config.VDCConfig;
import com.therandomlabs.vanilladeathchest.util.CertificateHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.riftloader.listener.InitializationListener;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

public final class VanillaDeathChest implements InitializationListener {
	public static final String MOD_ID = "vanilladeathchest";
	public static final String CERTIFICATE_FINGERPRINT = "@FINGERPRINT@";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialization() {
		if(!verifyFingerprint()) {
			LOGGER.error("Invalid fingerprint detected for VanillaDeathChest!");
		}

		MixinBootstrap.init();
		Mixins.addConfiguration("mixins." + MOD_ID + ".json");

		VDCConfig.reload();
	}

	public static void crashReport(String message, Throwable throwable) {
		throw new ReportedException(new CrashReport(message, throwable));
	}

	private static boolean verifyFingerprint() {
		final List<String> fingerprints = CertificateHelper.getFingerprints(
				VanillaDeathChest.class.getProtectionDomain().getCodeSource().getCertificates()
		);

		for(String fingerprint : fingerprints) {
			if(CERTIFICATE_FINGERPRINT.equals(fingerprint)) {
				return true;
			}
		}

		return false;
	}
}
