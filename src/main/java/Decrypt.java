import java.math.BigInteger;
import java.util.Random;

public class Decrypt {

    private BigInteger p, q, n, e, phi, d, encryptedMessage;


    private BigInteger getBigPrimeNumber() {
        BigInteger bigInteger = new BigInteger(1024, new Random());
        bigInteger = bigInteger.nextProbablePrime();
        return bigInteger;
    }

    //needs to generate public and private key and send public key and keep private key to decrypt the message
    public Decrypt(Encrypt encrypt, boolean forWienerAttack) {
        generateKeys(forWienerAttack);
        encrypt.setPublicKeys(n, e);
        encryptedMessage = encrypt.getCryptoText();
        System.out.println("Encrypted message: " + encryptedMessage);
    }

    private void generateKeys(boolean forWienerAttack) {
        p = getBigPrimeNumber();
        q = p.nextProbablePrime();
        n = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        if (!forWienerAttack) {
            e = BigInteger.valueOf(65537); //2^16 + 1 (prim)
            d = e.modInverse(phi);

        } else {
            BigInteger max = n.sqrt().sqrt().divide(BigInteger.valueOf(3));
            //System.out.println(max.bitLength());
            d = new BigInteger(max.bitLength() - 1, new Random());
            d = d.nextProbablePrime();
            System.out.println("Using d: " + d);
            //System.out.println(d.bitLength());
            e = d.modInverse(phi);
        }
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getE() {
        return e;
    }

    public long getPlainText(boolean usingCRT) {
        BigInteger plainText;
        long start, finish;
        if (usingCRT) {
            start = System.nanoTime();
            BigInteger zP = q.modInverse(p);
            BigInteger zQ = p.modInverse(q);
            BigInteger xP = encryptedMessage.mod(p).modPow(d.mod(p.subtract(BigInteger.ONE)), p);
            BigInteger xQ = encryptedMessage.mod(q).modPow(d.mod(q.subtract(BigInteger.ONE)), q);
            plainText = xP.multiply(q).multiply(zP).add(xQ.multiply(p).multiply(zQ)).mod(n);
            finish = System.nanoTime();
        } else {
            start = System.nanoTime();
            plainText = encryptedMessage.modPow(d, n);
            finish = System.nanoTime();
        }
        System.out.println("Plain text: " + plainText);
        System.out.println("Time to decrypt text: " + (finish - start));
        return (finish - start);
    }

}
