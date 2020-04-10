import common.BaseThread;

/**
 * Class Philosopher.
 * Outlines main subrutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		/*
		 * Print statement that philosopher has started eating
		 */
		System.out.println("Philosopher " + this.getTID() + " has started eating");

		/*
 		* Thread yielding
 		*/
		Thread.yield();

		try
		{
			// ...
			Thread.sleep((long)(Math.random() * TIME_TO_WASTE));
			// ...
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}

		/*
		 * Thread yielding
		 */
		Thread.yield();

		/*
		 * Print statement that philosopher has done eating
		 */
		System.out.println("Philosopher " + this.getTID() + " is done eating");
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		/*
		 * Print statement that philosopher has started thinking
		 */

		System.out.println("Philosopher " + this.getTID() + " has started thinking");

		/*
		 * Thread yielding
		 */

		Thread.yield();

		/*
		 * Thread sleeping
		 */
		try
		{
			// ...
			Thread.sleep((long)(Math.random() * TIME_TO_WASTE));
			// ...
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}

		/*
		 * Thread yielding
		 */
		Thread.yield();

		/*
		 * Print statement that philosopher has done thinking
		 */
		System.out.println("Philosopher " + this.getTID() + " is done thinking");
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		/*
		 * Print statement that philosopher has started talking
		 */

		System.out.println("Philosopher " + this.getTID() + " has started talking");

		/*
		 * Thread yielding
		 */

		Thread.yield();

		saySomething();

		/*
		 * Thread yielding
		 */

		Thread.yield();

		/*
		 * Print statement that philosopher has done talking
		 */

		System.out.println("Philosopher " + this.getTID() + " is done talking");
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */

	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			DiningPhilosophers.soMonitor.pickUp(getTID());

			eat();

			DiningPhilosophers.soMonitor.putDown(getTID());

			think();

			/*
			 * Decision is made randomly to have the philosopher speak using Math.random
			 */

			if(Math.random() > 0.5)
			{
				/*
				 * Philosopher requests to talk using monitor procedure
				 */
				DiningPhilosophers.soMonitor.requestTalk(getTID());

				talk();

				/*
				 * Philosopher requests to talk using monitor procedure
				 */
				DiningPhilosophers.soMonitor.endTalk(getTID());
			}

			yield();
		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + ""
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}

// EOF
