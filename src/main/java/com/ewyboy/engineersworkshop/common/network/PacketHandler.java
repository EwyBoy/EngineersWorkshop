package engineers.workshop.common.network;

import engineers.workshop.common.table.TileTable;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.networking.CustomPayloadPacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.packet.CustomPayloadClientPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.CustomPayloadServerPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.io.IOException;

public class PacketHandler {

	public static final Identifier CLIENT = new Identifier("engineersworkshop", "client_packet");
	public static final Identifier SERVER = new Identifier("engineersworkshop", "server_packet");

	public static DataPacket getPacket(TileTable table, PacketId id) {
		DataPacket dw = new DataPacket();
		dw.packetId = id;
		if (!id.isInInterface()) {
			dw.tablePos = table.getPos();
		}
		return dw;
	}

	public static void sendToPlayer(DataPacket dw, PlayerEntity player) {
		ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
		try {
			serverPlayerEntity.networkHandler.sendPacket(createClientPacket(dw));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static CustomPayloadClientPacket createClientPacket(DataPacket packet) throws IOException {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		packet.writeData(buf);
		return new CustomPayloadClientPacket(CLIENT, buf);
	}

	private static CustomPayloadServerPacket createServerPacket(DataPacket packet) throws IOException {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		packet.writeData(buf);
		return new CustomPayloadServerPacket(SERVER, buf);
	}

	public static void sendToServer(DataPacket dw) {
		try {
			MinecraftClient.getInstance().getNetworkHandler().sendPacket(createServerPacket(dw));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void init(){
		CustomPayloadPacketRegistry.CLIENT.register(CLIENT, (packetContext, packetByteBuf) -> {
			DataPacket dataPacket = new DataPacket();
			try {
				dataPacket.readData(packetByteBuf);
			} catch (IOException e) {
				e.printStackTrace();
			}

			dataPacket.processData(packetContext);
		});
		CustomPayloadPacketRegistry.SERVER.register(SERVER, (packetContext, packetByteBuf) -> {
			DataPacket dataPacket = new DataPacket();
			try {
				dataPacket.readData(packetByteBuf);
			} catch (IOException e) {
				e.printStackTrace();
			}

			dataPacket.processData(packetContext);
		});
	}

}
