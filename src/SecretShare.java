import java.math.BigInteger;

public class SecretShare {
    private final int number;
    private final BigInteger share;

    public SecretShare(final int number, final BigInteger share)
    {
        this.number = number;
        this.share = share;
    }

    public int getNumber()
    {
        return number;
    }

    public BigInteger getShare()
    {
        return share;
    }

    @Override
    public String toString()
    {
        return "S" + number + " = " + share;
    }


}
