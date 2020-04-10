import java.util.Arrays;

/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{

	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	private enum speakingStatus
	{
		SPEAKING, NOTSPEAKING
	}
	private enum eatingStatus
	{
		THINKING, EATING, HUNGRY
	}
	private enum statusOfChop
	{
		USED, UNUSED
	}
	private speakingStatus philSpeakingState[];
	private eatingStatus philEatingState[];
	private statusOfChop chopState[];
	private int numPhilosophers;

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		/*
		 * Initialization of arrays of the states of philosophers and chopsticks
		 */

		numPhilosophers = piNumberOfPhilosophers;
		chopState = new statusOfChop [numPhilosophers];
		philSpeakingState = new speakingStatus[numPhilosophers];
		philEatingState = new eatingStatus[numPhilosophers];

		for(int i = 0 ; i < piNumberOfPhilosophers ; i++)
		{
			chopState[i] = statusOfChop.UNUSED;
			philSpeakingState[i] = speakingStatus.NOTSPEAKING;
			philEatingState[i] = eatingStatus.THINKING;
		}
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{

		/*
		* Calculates left and right chopstick indexes. Sets eatingStatus to hungry. While eating status is
		* not eating, will loop in order to try to eat. Will wait at the end of while loop to allow for
		* another philosopher to eat.
		*/
		philEatingState[piTID] = eatingStatus.HUNGRY;

		int rightChopstick = piTID;
		int leftChopStick = (piTID + numPhilosophers - 1) % numPhilosophers;

		/*
		TODO: If even philosopher, check right then left chopsticks
		 */
		while(!(philEatingState[piTID] == eatingStatus.EATING)) {

			if (chopState[rightChopstick] == statusOfChop.UNUSED &&
					chopState[leftChopStick] == statusOfChop.UNUSED) {
				chopState[rightChopstick] = statusOfChop.USED;
				chopState[leftChopStick] = statusOfChop.USED;
				philEatingState[piTID] = eatingStatus.EATING;

				System.out.println("Philosopher " + piTID + " picked up chopsticks " + rightChopstick + " and " + leftChopStick);
			}

			if (!(philEatingState[piTID] == eatingStatus.EATING)) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		/*
		 * When done eating, puts down left and right chopsticks,
		 * sets state as thinking, and notifies other blocked threads
		 */

		int rightChopstick = piTID;
		int leftChopStick = (piTID + numPhilosophers - 1) % numPhilosophers;

		chopState[rightChopstick] = statusOfChop.UNUSED;
		chopState[leftChopStick] = statusOfChop.UNUSED;
		philEatingState[piTID] = eatingStatus.THINKING;
		System.out.println("Philosopher " + piTID + " put down chopsticks " + rightChopstick + " and " + leftChopStick);

		notifyAll();
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating). Added piTID parameter.
	 */
	public synchronized void requestTalk(final int piTID)
	{
		/*
		 * If no one is speaking, and if the philosopher is not eating, then the philosopher speaks.
		 * The loop will iterate will the philosopher is not speaking
		 */

		while(philSpeakingState[piTID] == speakingStatus.NOTSPEAKING)
		{
			Boolean someoneSpeaking = false;

			for(speakingStatus speakStatus : philSpeakingState)
			{
				if(speakStatus == speakingStatus.SPEAKING)
				{
					someoneSpeaking = true;
					break;
				}
			}

			if(!(philEatingState[piTID] == eatingStatus.EATING) && !someoneSpeaking)
			{
				philSpeakingState[piTID] = speakingStatus.SPEAKING;
			}

			if(philSpeakingState[piTID] == speakingStatus.NOTSPEAKING)
			{
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking. Added piTID parameter.
	 */
	public synchronized void endTalk(final int piTID)
	{
		/*
		 * The philosopher sets his speaking status to NOTSPEAKING, and notifies the blocked threads
		 */

		philSpeakingState[piTID] = speakingStatus.NOTSPEAKING;

		notifyAll();
	}


}

// EOF
