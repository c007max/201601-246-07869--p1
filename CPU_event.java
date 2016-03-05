import java.util.Random ; 

public class CPU_event	// this class returns a result from a range by percentage
{
	private float	event ;		
	final static double pct05 = -1.645 ;	final static double pct10 = -1.036 ; 
	final static double pct15 = -0.524 ;	final static double pct20 = 0 ;
	
	public void CPU_event()
	{
	
	}
	
	public int get_CPU_event()
	{
		Random random__X	= new Random();	//#010 declare random object
		
		event = (float) random__X.nextGaussian() ;	
				
		if (event < pct05)
			return 1 ;
		else if (event < pct10)
			return 2 ;
		else if (event < pct15)
			return 3 ;
		else if (event < pct20)
			return 4 ;		
		else
			return 5 ;
	}
}