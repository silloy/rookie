package me.silloy;

import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.constraints.NotNull;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;

public class PaceTest {

    @Test
    public void pace() {
        @NotNull Object o = null;
        Collections.emptyList().add("One");
        int i = Integer.parseInt("hello");
        System.console().readLine();
    }


    /**
     * 求一个字符串包含的char
     */
    public static void containChars(String str) {
        BitSet used = new BitSet();
        for (int i = 0; i < str.length(); i++)
            used.set(str.charAt(i)); // set bit for char
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int size = used.size();
        System.out.println(size);
        for (int i = 0; i < size; i++) {
            if (used.get(i)) {
                sb.append((char) i);
            }
        }
        sb.append("]");
        System.out.println(sb.toString());
    }


    public static void computePrime() {
        BitSet sieve = new BitSet(1024);
        int size = sieve.size();
        for (int i = 2; i < size; i++)
            sieve.set(i);
        int finalBit = (int) Math.sqrt(sieve.size());

        for (int i = 2; i < finalBit; i++)
            if (sieve.get(i))
                for (int j = 2 * i; j < size; j += i)
                    sieve.clear(j);

        int counter = 0;
        for (int i = 1; i < size; i++) {
            if (sieve.get(i)) {
                System.out.printf("%5d", i);
                if (++counter % 15 == 0)
                    System.out.println();
            }
        }
        System.out.println();
    }


    /**
     * 进行数字排序
     */
    public static void sortArray() {
        int[] array = new int[]{423, 700, 9999, 2323, 356, 6400, 1, 2, 3, 2, 2, 2, 2};
        BitSet bitSet = new BitSet(2 << 13);
        // 虽然可以自动扩容，但尽量在构造时指定估算大小,默认为64
        System.out.println("BitSet size: " + bitSet.size());

        for (int i = 0; i < array.length; i++) {
            bitSet.set(array[i]);
        }
        //剔除重复数字后的元素个数
        int bitLen = bitSet.cardinality();

        //进行排序，即把bit为true的元素复制到另一个数组
        int[] orderedArray = new int[bitLen];
        int k = 0;
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            orderedArray[k++] = i;
        }

        System.out.println("After ordering: ");
        for (int i = 0; i < bitLen; i++) {
            System.out.print(orderedArray[i] + "\t");
        }

        System.out.println("iterate over the true bits in a BitSet");
        //或直接迭代BitSet中bit为true的元素iterate over the true bits in a BitSet
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            System.out.print(i + "\t");
        }
        System.out.println("---------------------------");
    }


    /**
     * 将BitSet对象转化为ByteArray
     *
     * @param bitSet
     * @return
     */
    public static byte[] bitSet2ByteArray(BitSet bitSet) {
        byte[] bytes = new byte[bitSet.size() / 8];
        for (int i = 0; i < bitSet.size(); i++) {
            int index = i / 8;
            int offset = 7 - i % 8;
            bytes[index] |= (bitSet.get(i) ? 1 : 0) << offset;
        }
        return bytes;
    }

    /**
     * 将ByteArray对象转化为BitSet
     *
     * @param bytes
     * @return
     */
    public static BitSet byteArray2BitSet(byte[] bytes) {
        BitSet bitSet = new BitSet(bytes.length * 8);
        int index = 0;
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 7; j >= 0; j--) {
                bitSet.set(index++, (bytes[i] & (1 << j)) >> j == 1 ? true
                        : false);
            }
        }
        return bitSet;
    }


    /**
     * 简单使用示例
     */
    public static void simpleExample() {
        String names[] = {"Java", "Source", "and", "Support"};
        BitSet bits = new BitSet();
        for (int i = 0, n = names.length; i < n; i++) {
            if ((names[i].length() % 2) == 0) {
                bits.set(i);
            }
        }

        System.out.println(bits);
        System.out.println("Size : " + bits.size());
        System.out.println("Length: " + bits.length());
        for (int i = 0, n = names.length; i < n; i++) {
            if (!bits.get(i)) {
                System.out.println(names[i] + " is odd");
            }
        }
        BitSet bites = new BitSet();
        bites.set(0);
        bites.set(1);
        bites.set(2);
        bites.set(3);
        bites.andNot(bits);
        System.out.println(bites);
    }


    public static void main(String args[]) {
        //BitSet使用示例
        containChars("How do you do? 你好呀");
        computePrime();
        sortArray();
        simpleExample();


        //BitSet与Byte数组互转示例
        BitSet bitSet = new BitSet();
        bitSet.set(3, true);
        bitSet.set(98, true);
        System.out.println(bitSet.size() + "," + bitSet.cardinality());
        //将BitSet对象转成byte数组
        byte[] bytes = bitSet2ByteArray(bitSet);
        System.out.println(Arrays.toString(bytes));

        //在将byte数组转回来
        bitSet = byteArray2BitSet(bytes);
        System.out.println(bitSet.size() + "," + bitSet.cardinality());
        System.out.println(bitSet.get(3));
        System.out.println(bitSet.get(98));
        for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1)) {
            System.out.print(i + "\t");
        }
    }

    @Test
    public void mybatis() throws
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
        keygen.initialize(512);
        KeyPair kp = keygen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
        System.out.println("KEY:\n" + bytesToHexString(publicKey.getEncoded()) + "\n");
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        System.out.println("RESULT:\n" + bytesToHexString(cipher.doFinal("shaohuasu".getBytes())) + "\n");
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


}
