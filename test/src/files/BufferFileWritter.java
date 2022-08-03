package files;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Ruslan Dauhiala
 */
public class BufferFileWritter
{
	private static final int kBuffSize = 1000;
	private List<List<Integer>> myBuffer;
	private Path myFile;


	public void addRecord(List<Integer> theRecord)
	{
		myBuffer.add(theRecord);
		//if ()
	}

	private void write()
	{

	}
}
