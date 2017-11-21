import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;



/**
 * This version of the ball clock uses string id's as 'balls'.
 * 
 * To run this program, you need to pass arguments 1 or 2 arguments in from the command line or IDE.
 * The first argument will be a number of balls to use
 * The second argument will be the number of minutes to use.
 * 
 * @author Steven Brown
 * 
 * This project was created for an interview coding assessment for Rakuten Marketing
 * Steven Brown 2017
 * https://github.com/StevenBrown17
 * https://www.linkedin.com/in/stevenbrown17/
 * 
 *
 */
public class Main {
	
	public static int twelveCount=0, minuteCount=0, dayCount=0, numBalls = 30, numberMinutesToRun=0;
	static String originalOrder ="",originalId="";

	public static Stack<String> minuteStack = new Stack<String>();//minutes stack
	public static Stack<String> fiveStack = new Stack<String>();//5 min stack
	public static Stack<String> hourStack = new Stack<String>();//hour stack
	public static Queue<String> ballQueue = new LinkedList<String>();

	
	

	public static void main(String[] args) {
		
		List list = new ArrayList();
		
		if(args.length==0) {
			Scanner reader = new Scanner(System.in);  // Reading from System.in
			System.out.println("Enter number of balls: ");
			list.add(reader.nextLine());
			
			Scanner reader1 = new Scanner(System.in);  // Reading from System.in
			System.out.println("Do you want to enter minutes? (y/n) ");
			String input = reader1.nextLine()+"";
			if(input.equalsIgnoreCase("Y")) {
				Scanner reader2 = new Scanner(System.in);  // Reading from System.in
				System.out.println("Minutes: ");
				list.add(reader2.nextLine());
			}
		}else {
			list.add(args[0]);
			if(args.length == 2) {list.add(args[1]);}
		}
		
		
		
		if(list.size() != 1 && list.size() != 2) {
			System.out.println("Invalid arguments");
			System.exit(0);
		}
		int arg1, arg2;
		
		try {
		arg1 = Integer.parseInt(list.get(0)+"");
		numBalls = arg1;
		
		
		if(list.size() == 2) {
			arg2 = Integer.parseInt(list.get(1)+"");
			numberMinutesToRun = arg2;
		}
		
		if(arg1 < 27 || arg1 > 127) {
			System.out.println("Please enter a number between 27 and 127 as your first argument");
			System.exit(0);
		}
		
		}catch(Exception e) {
			System.out.println("Please enter valid");
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		
		hourStack.push("0"); //this is the fixed ball that will always remain in the hour stack.
		loadQueue(numBalls);
		
		if(list.size() == 1) {
			loadStacks();
			originalOrder = getCurrentOrder();
			
			do {
				executeMinute();
				//System.out.println(getTime());
				
			}while(!originalOrder.equals(getCurrentOrder()));
			
			System.out.println(numBalls + " balls cycle after "+dayCount+ " days");
			System.exit(0);
			
		}
		
		if(list.size() == 2) {
			
			executeMinute(numberMinutesToRun);
			System.out.println(printJson());
			System.exit(0);
		}
		

	}//end main
	
	
	/**
	 * loadQueue will place id in the ballQueue. However many id's 
	 * depends on how many the user inputs as the first argument
	 * @param numberOfBalls
	 */
	public static void loadQueue(int numberOfBalls) {
		
		for(int i=1; i <= numberOfBalls; i++) {
			ballQueue.add(i+"");
		}
		
	}//end loadQueue
	
	/**
	 * loadStacks will load the hourStack with 11 id's from the ballQueue,
	 * load the fiveStack with 11 id's from the ballQueue,
	 * and load the minuteStack with 4 id's from the ballQueue.
	 * 
	 * This method will be used when determining hom many days before the balls return to the original positions.
	 */
	public static void loadStacks() {
		
		//hourStack.push(new Ball());
		
		while(hourStack.size() != 12) {
			hourStack.push(ballQueue.poll());

		}
		
		while(fiveStack.size() != 11) {
			fiveStack.push(ballQueue.poll());	
		}
		
		while(minuteStack.size()!= 4) {
			minuteStack.push(ballQueue.poll());
		}		

	}//endLoadStacks
	
	/**
	 * executeMinute will cycle 1 ball through the ball clock.
	 */
	public static void executeMinute() {
		minuteCount++; //minute count used for debugging purposes
		
		if(minuteStack.size() != 4) {
			minuteStack.push(ballQueue.poll());//if there is room in the minuteStack, add a ball from the queue.
			
		}else {
			//this code will execute iff the minuteStack was full
			
			for(int i=0; i<4;i++) {ballQueue.add(minuteStack.pop());}//pop all off the minute stack, add to ballQueue
		
			if(fiveStack.size() != 11) {
				fiveStack.push(ballQueue.poll());//if there is room in the Fives Stack, add ball from the queue.
			}else {
				
				//this code will execute if the fiveStack was full
				
				for(int j=0; j<11;j++) {ballQueue.add(fiveStack.pop());}//pop all off the fiveStack, and add to ballQueue
				
				if(hourStack.size() != 12) {
					hourStack.push(ballQueue.poll());
				}else {

					for(int k=0; k<11;k++) {
						ballQueue.add(hourStack.pop());
					}
					ballQueue.add(ballQueue.poll());
					
					twelveCount++;
					
					if(twelveCount == 2) {
						dayCount++;
						twelveCount =0;
					}
					
					
				}//end hourStack else
				
			}//end fiveStack else
			
		}//end if/else if
		
		//System.out.println(getTime());
		
	}//end executeMinute
	
	
	/**
	 * executeMinute will cycle however many time the user specified in the 2nd argument
	 */
	public static void executeMinute(int minutes) {
	
	while(minutes!=0) {
	
		if(minuteStack.size() != 4) {
			minuteStack.push(ballQueue.poll());//if there is room in the Minutes Stack, add a ball from the queue.
		}else {
			
			//if there is no room in the Minutes queue, pop the Minutes Stack, and add to queue, then poll the queue onto the Five Stack
			//for(int i=0; i<minuteStack.size();i++) {ballQueue.add(minuteStack.pop());}
			for(int i=0; i<4;i++) {ballQueue.add(minuteStack.pop());}
		
			if(fiveStack.size() != 11) {
				fiveStack.push(ballQueue.poll());//if there is room in the Fives Stack, add ball from the queue.
			}else {
				
				//if there is no room in the Minutes queue, pop the Fives Stack, and add to queue, then poll the queue onto the Five Stack
				//for(int j=0; j<fiveStack.size();j++) {ballQueue.add(fiveStack.pop());}
				for(int j=0; j<11;j++) {ballQueue.add(fiveStack.pop());}
				
				if(hourStack.size() != 12) {
					hourStack.push(ballQueue.poll());
				}else {
					
					//if there is no room in the Minutes queue, pop the Fives Stack, and add to queue, then poll the queue onto the Five Stack
					//for(int k=0; k<hourStack.size();k++) {ballQueue.add(hourStack.pop());}
					for(int k=0; k<11;k++) {ballQueue.add(hourStack.pop());}
					//hourStack.push(new Ball());
					ballQueue.add(ballQueue.poll());
					
					twelveCount++;
					
					if(twelveCount == 2) {
						dayCount++;
						twelveCount =0;
					}
					
				}
			}
			
		}
		
		minutes--;
		//System.out.println(getTime());
	}
		
	}//end executeMinute(int)
	
	
	/**
	 * returns the current time on the ball clock
	 * @return current time
	 */
	public static String getTime() {
		
		String hour = hourStack.size() +"";
		String minutes;
		
		
		if(fiveStack.size() ==1 || fiveStack.size() ==0) {
			minutes = "0"+minuteStack.size();
		}else
			minutes = ((fiveStack.size()*5) + minuteStack.size()) + "";
		
		
		
		return hour + ":" + minutes;
		
		
	}//end getTime()
	
	/**
	 * getCurrentOrder will return the current order of id's. hours+fives+minutes+queue.
	 * This method is used to capture the original order of the balls, and the compared when a cycle is performed.
	 * @return current order of id's/balls
	 */
	public static String getCurrentOrder() {
		String currentOrder="";	
		
		for(int i=1; i<hourStack.size();i++) {
			currentOrder+=hourStack.get(i);
		}
		
		if(!fiveStack.isEmpty()) {
			for(int i=0; i<fiveStack.size();i++) {
				
				currentOrder+=fiveStack.get(i);
			}
		}
		
		if(!minuteStack.isEmpty()) {
			for(int i=0; i<minuteStack.size();i++) {
				currentOrder+=minuteStack.get(i);
			}
		}
		
		ArrayList list = new ArrayList(ballQueue);
		for(int i=0;i<list.size();i++) {
			currentOrder+=list.get(i);
		}		
		
		return currentOrder;
	}


	/**
	 * printJson will print the current ball clock in a json format
	 * @return ball clock in json
	 */
	public static String printJson() {
		
		String hours = Arrays.toString(hourStack.toArray());
		
		//when we convert the hour stack to a string, it keeps the fixed ball with index 0. We need to remove that id.
		hours = hours.replaceFirst("0,", "");//if there are id's in the hour, remove  ' 0, '
		hours = hours.replaceFirst("0", "");//if there are no id's in the hour, only 0 will appear without the ','
		
		String json="{\"Min\":"+Arrays.toString(minuteStack.toArray())+
				",\"FiveMin\":"+Arrays.toString(fiveStack.toArray())+
				",\"Hour\":"+hours+
				",\"Main\""+Arrays.toString(ballQueue.toArray())+"}";
		json = json.replaceAll(" ", "");
		return json;
	}
	
	
}//end class
