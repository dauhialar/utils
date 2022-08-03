package pb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author Ruslan Dauhiala
 */
public interface Meters
{
	DateFormat kNewFileDateFormat = new SimpleDateFormat("MMM dd, yyyy");

	DateFormat kFileDateFormat = new SimpleDateFormat("MM/dd/yyyy");

	DateFormat kFileNameDateFormat = new SimpleDateFormat("MM-dd-yyyy");

	DateFormat kOutDateFormat = new SimpleDateFormat("MM/dd/yy.E");

	DateFormat aReverceDateFormat = new SimpleDateFormat("yyyy/MM/dd");

	String myAlf = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()";

}
