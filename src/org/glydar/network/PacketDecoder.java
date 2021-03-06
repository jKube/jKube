package org.glydar.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageList;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.glydar.Glydar;
import org.glydar.packets.PacketReader;
import org.glydar.packets.annotations.PacketStruct;
import org.glydar.paraglydar.vectors.Vec3;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf inb, MessageList<Object> out) throws Exception {

        ByteBuf in = inb.order(ByteOrder.LITTLE_ENDIAN);

        if (in.readableBytes() < 4)
            return;

        in.markReaderIndex();

        int id = in.readInt();

//        if (id != 0)
//            Glydar.getServer().getLogger().info("Got Packet ID " + id + "!");

        PacketReader reader = Glydar.getServer().getPacketReaderList().getReaderWithId(id);

        if (reader == null) {

            in.markReaderIndex();

            return;

        }

        int len = 0;

        Class<?> packetClass = reader.getPacketClass();

        for (Field f : packetClass.getDeclaredFields()) {

            if (f.isAnnotationPresent(PacketStruct.class)) {

                PacketStruct struct = f.getAnnotation(PacketStruct.class);

                if (struct.dynamicLength()) {

                    if (in.readableBytes() < len + 4)
                    {

                        in.resetReaderIndex();

                        return;

                    }

                    len += in.readInt();

                } else {

                    int sLen = struct.length();

                    if (sLen == 0) {

                        Class<?> sType = f.getType();

                        if (sType.equals(byte.class)) {
                            len += 1;
                        } else if (sType.equals(short.class)) {
                            len += 2;
                        } else if (sType.equals(int.class)) {
                            len += 4;
                        } else if (sType.equals(float.class)) {
                            len += 4;
                        } else if (sType.equals(long.class)) {
                            len += 8;
                        } else if (sType.equals(Vec3.class)) {
                            len += 12;
                        }

                    } else {
                        len += sLen;
                    }

                }

            }

        }

        if (in.readableBytes() < len)
        {

            in.resetReaderIndex();

            return;

        }



        byte[] data = new byte[len];

        in.readBytes(data);

        out.add(reader.readPacket(ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN)));

//        IPacketCreator creator = Glydar.getServer().getPacketCreatorList().getCreatorWithId(id);
//
//        if (creator == null) {
//
//            in.markReaderIndex(); //Sets new 0 to current position
//
//            return;
//
//        }
//
//        int len = 0;
//
//        for (PacketStructure structure : creator.getStructures()) {
//
//            len += structure.getTotalLength();
//
//            for (PacketDataType pdt : structure.getDataTypes()) {
//                if (pdt.isDynamicLength()) {
//                    len += PacketDataType.IntegerSize;
//                }
//            }
//
//            if (in.readableBytes() < len) {
//
//                in.resetReaderIndex();
//
//                return;
//
//            }
//
//            for (PacketDataType pdt : structure.getDataTypes()) {
//
//                if (pdt.isDynamicLength()) {
//
//				/*
//                 * Read next integer, which *should* contain the size of the dynamic data type. This would, for example, be used for chat messages
//				 */
//                    int dLen = in.readInt();
//
//                    //TODO Change to PROPERLY handle types with mixed data
//                    if (!pdt.getDataType().isAssignableFrom(String.class))
//                        len += dLen - 4;
//                    else
//                        len += (dLen * 2) - 4;
//
//                    //Glydar.getServer().getLogger().info("Discovered length "+len+" dLength "+dLen);
//
//
//
//                }
//
//            }
//
//            if (in.readableBytes() < len) {
//
//                in.resetReaderIndex();
//
//                return;
//
//            }
//
//        }
//
//        if (in.readableBytes() < len) {
//
//            in.resetReaderIndex();
//
//            return;
//
//        }
//
//        byte[] data = new byte[len];
//
//        in.readBytes(data);
//
//        out.add(creator.createPacket(data));

    }

}
