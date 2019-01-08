import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static int bitLength = 8;
    static int n;
    static int t;
    static SecureRandom secureRandom = new SecureRandom();

    public static void main(String[] args) {
        System.out.println("How many shares do you want to have?");
        Scanner s = new Scanner(System.in);
        String pom = s.nextLine();
        n = Integer.parseInt(pom);
        System.out.println("How many shares is needed to recreate secret?");
        pom=s.nextLine();
        t=Integer.parseInt(pom);

        BigInteger secret = BigInteger.valueOf(secureRandom.nextInt(255));
        //BigInteger secret = BigInteger.valueOf(954);
        BigInteger p;
        do  p = BigInteger.probablePrime(bitLength,secureRandom);
                while(p.compareTo(secret) <= 0 && p.compareTo(BigInteger.valueOf(n)) <=0);
        //p = BigInteger.valueOf(1523);
        System.out.println("Generated secret: " + secret);
        System.out.println("Generated p: " + p);


        BigInteger[] a = new BigInteger[t];
        a[0] = secret;
        for(int i=1;i<a.length;i++)
        {
            a[i] = BigInteger.valueOf(secureRandom.nextInt(p.intValue()));
        }
//        a[1] = BigInteger.valueOf(352);
//        a[2] = BigInteger.valueOf(62);
        System.out.println(a.length);
        SecretShare [] ss =splitSecret(secret,a,p);
        System.out.println("Divided sekrety: ");
        for(SecretShare x : ss)
        {
            System.out.println(x);
        }

        SecretShare [] test0 = new SecretShare[] {ss[0],ss[1],ss[2],ss[3]};
        SecretShare [] test1 = new SecretShare[] {ss[0],ss[1],ss[2]};
        SecretShare [] test2 = new SecretShare[] {ss[0],ss[1]};
        SecretShare [] test3 = new SecretShare[] {ss[0]};
        BigInteger result0 = reconstructSecret(test0,p);
        BigInteger result1 = reconstructSecret(test1,p);
        BigInteger result2 = reconstructSecret(test2,p);
        BigInteger result3 = reconstructSecret(test3,p);

        System.out.println("Result with four shares " + result0);
        System.out.println("Result with three shares " + result1);
        System.out.println("Result with two shares " + result2);
        System.out.println("Result with one shares " + result3);

    }

    public static SecretShare [] splitSecret (BigInteger secret, BigInteger[] randoms, BigInteger prime)
    {
        SecretShare [] shares = new SecretShare[n];
        for(int x=1;x<=n;x++)
        {
            BigInteger accum = secret;
            for(int exp=1;exp<t;exp++)
            {
                accum=(accum.add(randoms[exp].multiply(BigInteger.valueOf(x).pow(exp)))).mod(prime);
            }
            shares[x-1]=new SecretShare(x,accum);
        }
        return shares;
    }

    public static  BigInteger reconstructSecret (SecretShare [] shares, BigInteger prime)
    {
        BigInteger accum = BigInteger.ZERO;
        for(int formula=0;formula<shares.length;formula++)
        {
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for(int count =0; count<shares.length;count++)
            {
                if(formula==count)continue;

                int start = shares[formula].getNumber();
                int next =shares[count].getNumber();

                numerator = numerator.multiply(BigInteger.valueOf(next).negate());
                denominator = denominator.multiply(BigInteger.valueOf(start-next));

            }
            BigInteger value = shares[formula].getShare();
            BigInteger pom = value.multiply(numerator).multiply(modInverse(denominator,prime));
            accum=prime.add(accum).add(pom).mod(prime);


        }
        return accum;
    }

    public static BigInteger modInverse(BigInteger pom, BigInteger prime)
    {
        pom=pom.mod(prime);
        BigInteger r = (pom.compareTo(BigInteger.ZERO)==-1)?(gcdD(prime,pom.negate())[2]).negate() : gcdD(prime,pom)[2];
        return prime.add(r).mod(prime);
    }

    private static BigInteger[] gcdD(BigInteger a, BigInteger b)
    {
        if (b.compareTo(BigInteger.ZERO) == 0)
            return new BigInteger[] {a, BigInteger.ONE, BigInteger.ZERO};
        else
        {
            BigInteger n = a.divide(b);
            BigInteger c = a.mod(b);
            BigInteger[] r = gcdD(b, c);
            return new BigInteger[] {r[0], r[2], r[1].subtract(r[2].multiply(n))};
        }
    }
}
