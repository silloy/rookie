package me.silloy.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

/**
 * @author shaohuasu
 * @date 2018-12-26 14:55
 * @since 1.8
 * <p>
 * 32位编译器：
 * char ：1个字节
 * char*（即指针变量）: 4个字节（32位的寻址空间是2^32, 即32个bit，也就是4个字节。同理64位编译器）
 * short int : 2个字节
 * int： 4个字节
 * unsigned int : 4个字节
 * float: 4个字节
 * double: 8个字节
 * long: 4个字节
 * long long: 8个字节
 * unsigned long: 4个字节
 *
 * <i>64位编译器</i>：
 * char ：1个字节
 * char*(即指针变量): 8个字节
 * short int : 2个字节
 * int： 4个字节
 * unsigned int : 4个字节
 * float: 4个字节
 * double: 8个字节
 * long: 8个字节
 * long long: 8个字节
 * unsigned long: 8个字节
 */
public class ByteBufTest {

    @Test
    public void byteTest() {

        System.out.print("action" + "\t");
        System.out.print("capacity " + "\t");
        System.out.print("maxCapacity " + "\t");
        System.out.print("readerIndex " + "\t");
        System.out.print("readableBytes " + "\t");
        System.out.print("isReadable " + "\t");
        System.out.print("writerIndex " + "\t");
        System.out.print("writableBytes " + "\t");
        System.out.print("isWritable " + "\t");
        System.out.print("maxWritableBytes " + "\t");
        System.out.println();

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(9, 100);
        print("allocate ByteBuf(9, 100)", buf);

        // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写
        buf.writeBytes(new byte[]{1, 2, 3, 4});
        print("writeBytes(1,2,3,4)", buf);

        // 写完 int 类型之后，写指针增加4
        buf.writeInt(12);
        print("writeInt(12)", buf);

        // write 方法改变写指针, 写完之后写指针等于 capacity 的时候，buffer 不可写
        buf.writeBytes(new byte[]{5});
        print("writeByte(5)", buf);

        // write 方法改变写指针，写的时候发现 buffer 不可写则开始扩容，扩容之后 capacity 随即改变
        buf.writeBytes(new byte[]{6});
        print("writeBytes(6)", buf);

        // get 方法不改变读写指针
        System.out.println("getShort(3) return: " + buf.getShort(3));
        System.out.println("getByte(3) return: " + buf.getByte(3));
        System.out.println("getInt(3) return: " + buf.getInt(3));
        print("getByte()", buf);

        // set 方法不改变读写指针
        buf.setByte(buf.readableBytes() + 1, 0);
        print("setByte()", buf);

        // read 方法改变读指针
        byte[] dst = new byte[buf.readableBytes()];
        buf.readBytes(dst);
        print("readBytes(" + dst.length + ")", buf);
    }


    private static void print(String action, ByteBuf buf) {
        System.out.print(action + "\t");
        System.out.print(buf.capacity() + "\t");
        System.out.print(buf.maxCapacity() + "\t");
        System.out.print(buf.readerIndex() + "\t");
        System.out.print(buf.readableBytes() + "\t");
        System.out.print(buf.isReadable() + "\t");
        System.out.print(buf.writerIndex() + "\t");
        System.out.print(buf.writableBytes() + "\t");
        System.out.print(buf.isWritable() + "\t");
        System.out.print(buf.maxWritableBytes() + "\t");
        System.out.println();
    }
}
