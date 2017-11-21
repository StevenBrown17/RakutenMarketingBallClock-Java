import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * This version uses Ball objects instead of String id's
 * 
 * To run this program, you need to pass arguments 1 or 2 arguments in from the command line or IDE.
 * 
 * @author Steven Brown
 *
 */
public class BallsMain {

	
	public static int originalId, twelveCount=0, minuteCount=0;
	public static int dayCount=0;
	static String originalOrder ="";

	public static int numBalls = 80, numberMinutesToRun=0;
	
	public static Stack<Ball> minuteStack = new Stack<Ball>();//minutes stack
	public static Stack<Ball> fiveStack = new Stack<Ball>();//5 min stack
	public static Stack<Ball> hourStack = new Stack<Ball>();//hour stack
	public static Queue<Ball> ballQueue = new LinkedList<Ball>();

	public static void main(String[] args) {
		if(args.length != 1 && args.length != 2) {
			System.out.println("Invalid arguments");
			System.exit(0);
		}
		int arg1, arg2;
		
		try {
		arg1 = Integer.parseInt(args[0]);
		numBalls = arg1;
		if(args.length == 2) {
			arg2 = Integer.parseInt(args[1]);
			numberMinutesToRun = arg2;
		}
		}catch(Exception e) {
			System.out.println("Please enter valid");
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		
		hourStack.push(new Ball()); //this is the fixed ball that will always remain in the hour stack.
		loadQueue(numBalls);
		
		if(args.length == 1) {
			loadStacks();
			originalOrder = getCurrentOrder();
			
			do {
				executeMinute();
				
			}while(!originalOrder.equals(getCurrentOrder()));
			
			System.out.println(numBalls + " balls cycle after "+dayCount+ " days");
			System.exit(0);
			
		}
		
		if(args.length == 2) {
			
			executeMinute(numberMinutesToRun);
			System.out.println(printJson());
			System.exit(0);
		}
		
		
	}//end main
	
	
	public static void loadQueue(int numberOfBalls) {
		
		for(int i=1; i <= numberOfBalls; i++) {
			ballQueue.add(new Ball(i));
		}
		
	}//end loadQueue
	
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
	
	public static void executeMinute() {
		minuteCount++;
		
		if(minuteStack.size() != 4) {
			minuteStack.push(ballQueue.poll());//if there is room in the Minutes Stack, add a ball from the queue.
		}else {
			
			for(int i=0; i<4;i++) {ballQueue.add(minuteStack.pop());}
		
			if(fiveStack.size() != 11) {
				fiveStack.push(ballQueue.poll());//if there is room in the Fives Stack, add ball from the queue.
			}else {
				
				for(int j=0; j<11;j++) {ballQueue.add(fiveStack.pop());}
				
				if(hourStack.size() != 12) {
					hourStack.push(ballQueue.poll());
				}else {
					
					for(int k=0; k<11;k++) {ballQueue.add(hourStack.pop());}

					ballQueue.add(ballQueue.poll());
					
					twelveCount++;
					
					if(twelveCount == 2) {
						dayCount++;
						twelveCount =0;
					}

					
					
					
				}
			}
			
		}
		
		//System.out.println(getTime());
		
	}//end executeMinute
	
	
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
						twelveCount=0;
					}
					
				}
			}
			
		}
		
		minutes--;
		//System.out.println(getTime());
	}
		
	}//end executeMinute(int)
	
	
	public static String getTime() {
		
		String hour = hourStack.size() +"";
		String minutes;
		
		
		if(fiveStack.size() ==1 || fiveStack.size() ==0) {
			minutes = "0"+minuteStack.size();
		}else
			minutes = ((fiveStack.size()*5) + minuteStack.size()) + "";

		return hour + ":" + minutes;	
		
	}//end getTime()
	
	
	public static String getCurrentOrder() {
		String currentOrder="";
		
		Queue<Ball> tempQueue = new LinkedList<Ball>();
		
		
		for(int i=1; i<hourStack.size();i++) {
			currentOrder+=hourStack.get(i).id+"";
		}
		
		if(!fiveStack.isEmpty()) {
			for(int i=0; i<fiveStack.size();i++) {
				
				currentOrder+=fiveStack.get(i).id+"";
			}
		}
		
		if(!minuteStack.isEmpty()) {
			for(int i=0; i<minuteStack.size();i++) {
				currentOrder+=minuteStack.get(i).id+"";
			}
		}
		
		for(int i=0; i < ballQueue.size();i++) {
			Ball tempBall;
			
			tempQueue.add(ballQueue.poll());
			
			tempBall = tempQueue.poll();
			currentOrder+=tempBall.id+"";
			
			ballQueue.add(tempBall);
			
		}
			
		return currentOrder;
	}//end getCurrentOrder
	
	public static String printJson() {
		
		//when using Ball objects, its a little harder because we have to extract the id from the objects in the stacks
		
		List<String> hoursList = new ArrayList<String>();
		List<String> fiveList = new ArrayList<String>();
		List<String> minuteList = new ArrayList<String>();
		List<String> queueList = new ArrayList<String>();
		Ball tempBall;
		
		while(hourStack.size()!=1) {
			tempBall = hourStack.pop();
			hoursList.add(0,tempBall.id+"");
		}
		
		while(fiveStack.size()!=0) {
			tempBall = fiveStack.pop();
			fiveList.add(0,tempBall.id+"");
		}
		
		while(minuteStack.size()!=0) {
			tempBall = minuteStack.pop();
			minuteList.add(0,tempBall.id+"");
		}
		while(ballQueue.size()!=0) {
			tempBall = ballQueue.poll();;
			queueList.add(tempBall.id+"");
		}

		String json="{\"Min\":"+Arrays.toString(minuteList.toArray())+
				",\"FiveMin\":"+Arrays.toString(fiveList.toArray())+
				",\"Hour\":"+Arrays.toString(hoursList.toArray())+
				",\"Main\""+Arrays.toString(queueList.toArray())+"}";
		
		json = json.replaceAll(" ", "");//remove all spaces from json
		return json;
		
	}//end printJson
	
	
}//end BallsMain class
