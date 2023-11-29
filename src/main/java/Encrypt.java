import java.math.BigInteger;

public class Encrypt {
    BigInteger n,e,message;

    public void setPublicKeys(BigInteger n, BigInteger e){
        this.n=n;
        this.e=e;
    }

    public void setMessage(BigInteger message){
        this.message=message;
    }

    public BigInteger getCryptoText(){
        long start = System.nanoTime();
        BigInteger cryptoText = message.modPow(e,n);
        long finish = System.nanoTime();
        System.out.println("Time to encrypt text: " + (finish-start));
        return cryptoText;
    }
    //needs to get public key to generate crypto text




}
