package flycat;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args )
    {
    	String date = "941125";
		int year, month, day;
		short carrier;
		carrier = (short) ((((date.charAt(0) - '0') * 10 + (date.charAt(1) - '0')) << 9) & 0xffff);
		carrier |= ((date.charAt(2) - '0') * 10 + (date.charAt(3) - '0')) << 5;
		carrier |= (date.charAt(4) - '0') * 10 + (date.charAt(5) - '0');
		year = (carrier >> 9) & 0x7f;
		month = ((carrier >> 5) & 0x0f);
		day = (carrier & 0x1f);
	    System.out.println("year:" + year  +"month:"+ month + "day:" + day);



	    int i = 0xff;
	    System.out.println(i);
    }
}
