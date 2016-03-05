import java.util.LinkedList ;
import java.util.Random ; 

public class ProcessMgmtSLN
{
	public static void main(String args[]) 	
	{
		//#010	Initialization of fields and data structures	///////////////
		int QREADY__T	= 15 ;		
		final int BLOCKIO	= 4 ;		final int BLOCKPAGE	= 3 ;		
		final int INTERRUPT	= 2 ;		final int COMPLETED	= 1 ; 
		
		Random random__X	= new Random();
		CPU_event event		= new CPU_event();
		
		int CPU_runtime ;		int event__X = 0;	int waitK = 0;
  
		LinkedList <PCB> QReady	= new LinkedList<PCB>();	//#005 Create the List for QReady
		LinkedList <PCB> QWait	= new LinkedList<PCB>();	//#006 Create the List for QWaiting
		
		PCB PCB_Running	= null ; 		//#020 Create the field for: PCB_Running 
	
		//#030	
		
		for (int ii = 0; ii < QREADY__T; ii++)
		{
			QReady.add(new PCB());		//#040 Add a new PCB object onto the LL 
		}

		///////////////////////////////////////////////////////////////////////
		//#080	===> end of Initialization 
		
		System.out.println("\n*****\t\t\tReady Queue\t\t\t*****");  
		for (PCB pcbLoop : QReady)		//#090 Loop that executes based on the no. of nodes in the LL
			System.out.println(pcbLoop.showPCB()) ;	//#095 Print the PCB for an LL node
  
		//#0100	Process until the active processes are all completed	///////
		
		while ( (QReady.size() > 0) || (QWait.size() > 0) )	//#120	change to iterate until both QReady and QWait are empty
		{
			if (QReady.isEmpty())
			{
				QReady.addFirst(QWait.removeFirst());
				System.out.printf(">>>>>\t\t I/O event completed: %d \t\t<<<<<\n"
						,QReady.peekFirst().get_ID()
						);
			}
			//#0105	Next process to Run
		
			//#0140 Assign the first node from QReady to Running
			PCB_Running	= QReady.removeFirst() ;

			//#0145 Set the state value to "Running"
			PCB_Running.set_state("Running");
			
			CPU_runtime	= random__X.nextInt(20) + 1 ;	//#0150 Get a random no. between 0 and 20
			
			//#0160 Tally and set the CPU used for the process
			PCB_Running.add_CPU_used(CPU_runtime);

			System.out.printf("\n*****\tRunning Process: %d\t*****\n"
					,PCB_Running.get_ID()
					);
			System.out.println(PCB_Running.showPCB());
	
			for (PCB pcbLoop : QReady)		//#0180 add wait times for all Ready processes
				pcbLoop.add_timeWaiting(CPU_runtime);
			for (PCB pcbLoop : QWait)		//#0181 add wait times for all Waiting processes
				pcbLoop.add_timeWaiting(CPU_runtime);
					
			//#0190 IF statement for termination based on CPU Max
			if	(PCB_Running.get_CPU_used() >= PCB_Running.get_CPU_max())
			{
				System.out.printf("\n\t*****\t\tProcess: %d Completed\t\t*****\n"
						,PCB_Running.get_ID()
						);   	  
				System.out.println(PCB_Running.showPCB());
				continue;	// iterate to the next in the WHILE loop
			}
						
			//#0200 Simulate the type of Block on the Process (I/O Block, Memory Paging Block, Interrupt)
			
			event__X	= event.get_CPU_event() ;
			
			if (event__X == COMPLETED)
			{
				System.out.printf("\n\t*****\t\tProcess: %d Completed\t\t*****CPU event*****\n"
						,PCB_Running.get_ID()
						);
				System.out.println(PCB_Running.showPCB());		
				continue ;
			}
			else
			{	
				switch (event__X)
				{
					case INTERRUPT :
					{
						PCB_Running.set_state("Ready");		//#240 Add to QReady	
						QReady.addFirst(PCB_Running);
						break;
					}				
					case BLOCKPAGE :
					{	
						PCB_Running.set_state("Ready");		//#242 Add to QReady
						QReady.add(QReady.size()/2			//#243 middle of QReady
								,PCB_Running);
						break;
					}
					case BLOCKIO :
					{
						PCB_Running.set_state("Waiting"); 	//#244 Add to QWait	
						QWait.add(PCB_Running) ;
						break;
					}
					default :
					{
						PCB_Running.set_state("Ready"); 	//#246 Add to QReady
						QReady.addLast(PCB_Running);
						break;
					}
				}
			}
			
			System.out.printf("\n\t\t\t*****\tContext Switch\t%d\t*****\n"
					,event__X
					);
			//#300 print out PCB's in both QReady and QWait
			if (!QReady.isEmpty())
				System.out.println("\nReady Queue");
			for (PCB pcbLoop : QReady)		//#090 Loop that executes based on the no. of nodes in the LL
				System.out.println(pcbLoop.showPCB()) ;
			
			if (!QWait.isEmpty())
				System.out.println("\nWait Queue");
			for (PCB pcbLoop : QWait)
				System.out.println(pcbLoop.showPCB()) ;
			
			waitK++;
			if ((waitK%6) == 0)
				if (QWait.size() > 0)
				{
					QReady.addFirst(QWait.removeFirst());
					System.out.printf(">>>>>\t\t I/O event completed: %d \t\t<<<<<\n"
							,QReady.peekFirst().get_ID()
							);
				}
		}		
	}
}