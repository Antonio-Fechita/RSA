import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class WienerAttack {

    Decrypt decrypt;
    BigInteger n, e, a, b;

    List<BigInteger> alphaList, betaList, qList;

    public WienerAttack(Decrypt decrypt) {
        this.decrypt = decrypt;
        this.e = decrypt.getE();
        this.n = decrypt.getN();

        alphaList = new ArrayList<>();
        betaList = new ArrayList<>();
        qList = new ArrayList<>();

        alphaList.add(BigInteger.ZERO);
        betaList.add(BigInteger.ZERO);
        qList.add(BigInteger.ZERO);

        a = e;
        b = n;
    }

    public void attack() {
        int i = 0;
        BigInteger l;
        BigInteger d;
        do {
            i++;
            l = getAlpha(i);
            d = getBeta(i);
        } while (!checkIntegerSolution(l, d));
        System.out.println("Found d: " + d);
    }

    private BigInteger getAlpha(int i) {
        if (i == 1) {
            if (alphaList.size() < i + 1)
                alphaList.add(i, getQ(1));
            return alphaList.get(i);
        } else if (i == 2) {
            if (alphaList.size() < i + 1)
                alphaList.add(i, getQ(1).multiply(getQ(2)).add(BigInteger.ONE));
            return alphaList.get(i);
        } else {
            if (alphaList.size() < i + 1)
                alphaList.add(i, getQ(i).multiply(getAlpha(i - 1)).add(getAlpha(i - 2)));
            return alphaList.get(i);
        }
    }

    private BigInteger getBeta(int i) {
        if (i == 1) {
            if (betaList.size() < i + 1)
                betaList.add(i, BigInteger.ONE);
            return betaList.get(i);
        } else if (i == 2) {
            if (betaList.size() < i + 1)
                betaList.add(i, getQ(2));
            return betaList.get(i);
        } else {
            if (betaList.size() < i + 1)
                betaList.add(i, getQ(i).multiply(getBeta(i - 1)).add(getBeta(i - 2)));
            return betaList.get(i);
        }
    }

    private BigInteger getQ(int i) {
        if (qList.size() < i + 1) {
            //System.out.println(qList.size());
            BigInteger q = a.divide(b);
            BigInteger r = a.mod(b);

            qList.add(i, q);

            a = b;
            b = r;
        }
        return qList.get(i);
    }

    private boolean checkIntegerSolution(BigInteger l, BigInteger d) {
        if(l.equals(BigInteger.ZERO))
            return false;

        if(!e.multiply(d).subtract(BigInteger.ONE).mod(l).equals(BigInteger.ZERO))
            return false;

        BigInteger t = e.multiply(d).subtract(BigInteger.ONE).divide(l);
        BigInteger b = t.subtract(n).subtract(BigInteger.ONE);
        BigInteger delta = b.pow(2).subtract(n.multiply(BigInteger.valueOf(4)));

        if(delta.compareTo(BigInteger.ZERO) < 0)
            return false;

        if(!delta.sqrt().multiply(delta.sqrt()).equals(delta))
            return false;

        BigInteger x1 = b.multiply(BigInteger.valueOf(-1)).add(delta.sqrt()).divide(BigInteger.TWO);
        BigInteger x2 = b.multiply(BigInteger.valueOf(-1)).subtract(delta.sqrt()).divide(BigInteger.TWO);

        if(!n.mod(x1).equals(BigInteger.ZERO) && !n.mod(x2).equals(BigInteger.ZERO))
            return false;

        return true;
    }

//    public static void main(String[] args) {
//        a = BigInteger.valueOf(4);
//        b = BigInteger.valueOf(11);
//
//        qList = new ArrayList<>();
//        qList.add(BigInteger.ZERO);
//
//        alphaList = new ArrayList<>();
//        alphaList.add(BigInteger.ZERO);
//
//        betaList = new ArrayList<>();
//        betaList.add(BigInteger.ZERO);
//
//        for(int i = 1;i<=4;i++){
//            System.out.println("alpha[" + i + "]= " + getAlpha(i));
//            System.out.println("beta[" + i + "]= " + getBeta(i));
//        }
//    }
}
