package net.frozenblock.wilderwild.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.wilderwild.networking.packet.WilderControlledSeedParticlePacket;
import net.frozenblock.wilderwild.networking.packet.WilderFloatingSculkBubbleParticlePacket;
import net.frozenblock.wilderwild.networking.packet.WilderJellyfishStingPacket;
import net.frozenblock.wilderwild.networking.packet.WilderLightningStrikePacket;
import net.frozenblock.wilderwild.networking.packet.WilderSeedParticlePacket;
import net.frozenblock.wilderwild.networking.packet.WilderSensorHiccupPacket;
import net.frozenblock.wilderwild.networking.packet.WilderTermiteParticlePacket;

@Environment(EnvType.CLIENT)
public class WilderClientNetworking {

	public static void registerPacketReceivers() {
		WilderFloatingSculkBubbleParticlePacket.receive();
		WilderSeedParticlePacket.receive();
		WilderControlledSeedParticlePacket.receive();
		WilderTermiteParticlePacket.receive();
		WilderSensorHiccupPacket.receive();
		WilderJellyfishStingPacket.receive();
		WilderLightningStrikePacket.receive();
	}

}
