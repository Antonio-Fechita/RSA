import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {

        Encrypt encrypt = new Encrypt();
        encrypt.setMessage(new BigInteger("543210098765432100"));
        Decrypt decrypt = new Decrypt(encrypt,false);
        long timeWithoutCRT = decrypt.getPlainText(false);
        long timeWithCRT = decrypt.getPlainText(true);
        System.out.println("Decryption using chinese remainder theorem was " + ((timeWithoutCRT/(float)timeWithCRT) * 100) + "% faster");

        decrypt = new Decrypt(encrypt,true);
        WienerAttack wienerAttack = new WienerAttack(decrypt);
        wienerAttack.attack();

    }
}
