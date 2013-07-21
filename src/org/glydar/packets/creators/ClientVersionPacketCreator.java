package org.glydar.packets.creators;

import java.util.ArrayList;

import org.glydar.packets.ClientPacketType;
import org.glydar.packets.IPacketCreator;
import org.glydar.packets.Packet;
import org.glydar.packets.PacketStructure;
import org.glydar.protocol.clientpackets.ClientVersionPacket;

public class ClientVersionPacketCreator implements IPacketCreator {

	@Override
	public Packet createPacket(byte[] data) throws Exception {
		return new ClientVersionPacket(data);
	}

	@Override
	public ArrayList<PacketStructure> getStructures() {
		return ClientVersionPacket.getStructures();
	}

	@Override
	public int getPacketId() {
		return ClientPacketType.ClientVersion.getId();
	}

}
