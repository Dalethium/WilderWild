package net.frozenblock.wilderwild.networking;

import java.util.concurrent.atomic.AtomicInteger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.frozenblock.lib.math.api.AdvancedMath;
import net.frozenblock.wilderwild.networking.packet.WilderLightningStrikePacket;
import net.frozenblock.wilderwild.particle.options.FloatingSculkBubbleParticleOptions;
import net.frozenblock.wilderwild.particle.options.SeedParticleOptions;
import net.frozenblock.wilderwild.registry.RegisterParticles;
import net.frozenblock.wilderwild.registry.RegisterSounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class WilderClientNetworking {

	public static void registerPacketReceivers() {
		receiveEasyEchoerBubblePacket();
		receiveSeedPacket();
		receiveControlledSeedPacket();
		receiveTermitePacket();
		receiveSensorHiccupPacket();
		receiveJellyStingPacket();
		WilderLightningStrikePacket.receive();
	}
	private static void receiveEasyEchoerBubblePacket() {
		ClientPlayNetworking.registerGlobalReceiver(WilderNetworking.FLOATING_SCULK_BUBBLE_PACKET, (ctx, handler, byteBuf, responseSender) -> {
			Vec3 pos = new Vec3(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
			double size = byteBuf.readDouble();
			int age = byteBuf.readInt();
			double yVel = byteBuf.readDouble();
			int count = byteBuf.readVarInt();
			ctx.execute(() -> {
				if (ctx.level == null)
					throw new IllegalStateException("why is your world null");
				var random = AdvancedMath.random();
				for (int i = 0; i < count; i++) {
					double xVel = (random.nextDouble() - 0.5) / 9.5;
					double zVel = (random.nextDouble() - 0.5) / 9.5;
					if (size >= 1) {
						xVel = (random.nextDouble() - 0.5) / 10.5;
						zVel = (random.nextDouble() - 0.5) / 10.5;
					}
					ctx.level.addParticle(new FloatingSculkBubbleParticleOptions(size, age, new Vec3(xVel, yVel, zVel)), pos.x, pos.y, pos.z, 0, 0, 0);
				}
			});
		});
	}

	private static void receiveSeedPacket() {
		ClientPlayNetworking.registerGlobalReceiver(WilderNetworking.SEED_PACKET, (ctx, handler, byteBuf, responseSender) -> {
			Vec3 pos = new Vec3(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
			int count = byteBuf.readVarInt();
			boolean milkweed = byteBuf.readBoolean();
			ctx.execute(() -> {
				if (ctx.level == null)
					throw new IllegalStateException("why is your world null");
				for (int i = 0; i < count; i++) {
					ctx.level.addParticle(new SeedParticleOptions(milkweed, false), pos.x, pos.y, pos.z, 0, 0, 0);
				}
			});
		});
	}

	private static void receiveControlledSeedPacket() {
		ClientPlayNetworking.registerGlobalReceiver(WilderNetworking.CONTROLLED_SEED_PACKET, (ctx, handler, byteBuf, responseSender) -> {
			Vec3 pos = new Vec3(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
			double velx = byteBuf.readDouble();
			double vely = byteBuf.readDouble();
			double velz = byteBuf.readDouble();
			int count = byteBuf.readVarInt();
			boolean milkweed = byteBuf.readBoolean();
			double posRandomizer = byteBuf.readDouble();
			ctx.execute(() -> {
				if (ctx.level == null)
					throw new IllegalStateException("why is your world null");
				for (int i = 0; i < count; i++) {
					ctx.level.addParticle(new SeedParticleOptions(milkweed, true), pos.x, pos.y + ((ctx.level.random.nextBoolean() ? -1 : 1) * (ctx.level.random.nextDouble() * posRandomizer)), pos.z, velx, vely + (ctx.level.random.nextDouble() * 0.07), velz);
				}
			});
		});
	}

	private static void receiveTermitePacket() {
		ClientPlayNetworking.registerGlobalReceiver(WilderNetworking.TERMITE_PARTICLE_PACKET, (ctx, handler, byteBuf, responseSender) -> {
			Vec3 pos = new Vec3(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
			AtomicInteger count = new AtomicInteger(byteBuf.readVarInt());
			ctx.execute(() -> {
				if (ctx.level == null)
					throw new IllegalStateException("why is your world null");

				count.addAndGet(-1);
				ctx.level.addAlwaysVisibleParticle(RegisterParticles.TERMITE, pos.x, pos.y, pos.z, 0, 0, 0);
				for (int i = 0; i < count.get(); i++) {
					ctx.level.addParticle(RegisterParticles.TERMITE, pos.x, pos.y, pos.z, 0, 0, 0);
				}
			});
		});
	}

	private static void receiveSensorHiccupPacket() {
		ClientPlayNetworking.registerGlobalReceiver(WilderNetworking.SENSOR_HICCUP_PACKET, (ctx, handler, byteBuf, responseSender) -> {
			Vec3 pos = new Vec3(byteBuf.readDouble(), byteBuf.readDouble(), byteBuf.readDouble());
			ctx.execute(() -> {
				if (ctx.level == null)
					throw new IllegalStateException("why is your world null");
				ClientLevel level = ctx.level;
				int i = 5578058;
				boolean bl2 = level.random.nextBoolean();
				if (bl2) {
					double d = (double) (i >> 16 & 255) / 255.0D;
					double e = (double) (i >> 8 & 255) / 255.0D;
					double f = (double) (i & 255) / 255.0D;
					level.addParticle(ParticleTypes.ENTITY_EFFECT, pos.x, pos.y, pos.z, d, e, f);
				}
			});
		});
	}

	private static void receiveJellyStingPacket() {
		ClientPlayNetworking.registerGlobalReceiver(WilderNetworking.JELLY_STING_PACKET, (ctx, handler, byteBuf, responseSender) -> ctx.execute(() -> {
			boolean baby = byteBuf.readBoolean();
			if (ctx.level != null) {
				LocalPlayer player = ctx.player;
				if (player != null) {
					ctx.level.playSound(player, player.getX(), player.getY(), player.getZ(), RegisterSounds.ENTITY_JELLYFISH_STING, SoundSource.NEUTRAL, 1.0F, ctx.level.random.nextFloat() * 0.2F + (baby ? 1.2F : 0.9F));
				}
			}
		}));
	}

}
