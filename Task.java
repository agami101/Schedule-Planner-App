import java.util.*;

public class Task {
	private static final String COMPLETED = "Completed";
	private static final String PENDING = "Pending";
	private static Integer _index = 1;

	// private static final String _helpContent = "Usage :-\n"
	// + "$ ./task add 2 hello world # Add a new item with priority 2 and text
	// \"hello world\" to the list\n"
	// + "$ ./task ls # Show incomplete priority list items sorted by priority in
	// ascending order\n"
	// + "$ ./task del INDEX # Delete the incomplete item with the given index\n"
	// + "$ ./task done INDEX # Mark the incomplete item with the given index as
	// complete\n"
	// + "$ ./task help # Show usage\n"
	// + "$ ./task report # Statistics";

	private static final String _helpContent = "Usage :-\n" +
			"$ ./task add 2 hello world    # Add a new item with priority 2 and text \"hello world\" to the list\n" +
			"$ ./task ls                   # Show incomplete priority list items sorted by priority in ascending order\n"
			+
			"$ ./task del INDEX            # Delete the incomplete item with the given index\n" +
			"$ ./task done INDEX           # Mark the incomplete item with the given index as complete\n" +
			"$ ./task help                 # Show usage\n" +
			"$ ./task report               # Statistics\n";

	private static Hashtable<Integer, Item> _taskList = new Hashtable<Integer, Item>();

	public static void main(String args[]) {
		Scanner ip = new Scanner(System.in);

		try {
			// System.out.println("Hi");

			while (true) {
				String input = ip.nextLine();
				// System.out.println("You enetered: " + input);

				if (input == "" || input.equals("help")) {
					PrintHelp();
				} else if (input.equals("ls")) {
					PendingTask();
				} else if (input.startsWith("add")) {
					AddTask(input);
				} else if (input.startsWith("del")) {
					DeleteTask(input);
				} else if (input.startsWith("done")) {
					DoneTask(input);
				} else if (input.startsWith("report")) {
					ReportTask();
				}
			}
		} finally {
			ip.close();
		}
	}

	private static void PrintHelp() {
		// System.out.println("PrintHelp Function");
		System.out.println(_helpContent);
	}

	private static void PendingTask() {
		// System.out.println("PendingTask Function");
		Set<Integer> prt = new HashSet<Integer>();
		for (Map.Entry<Integer, Item> entry : _taskList.entrySet()) {
			Item b = entry.getValue();
			prt.add(b.getPriority());
		}

		Iterator<Integer> itr = prt.iterator();
		int indx = 1;
		while (itr.hasNext()) {
			Integer i = itr.next();
			for (Map.Entry<Integer, Item> entry : _taskList.entrySet()) {
				Item b = entry.getValue();
				if (b.getPriority() == i && b.getStatus() != COMPLETED) {
					// [index] [task] [priority]
					System.out.println(indx++ + ". " + b.getTask() + " [" + b.getPriority() + "]");
				}
			}
		}
	}

	private static void AddTask(String input) {
		// System.out.println("AddTask Function - inout = " + input);
		/*
		 * input = add 5 "the thing i need to do"
		 * Added task: "the thing i need to do" with priority 5
		 */
		String[] strArr = input.split(" ");
		Item itm = new Item();
		itm.setIndex(_index);
		itm.setPriority(Integer.parseInt(strArr[1]));
		itm.setStatus(PENDING);

		StringBuilder task = new StringBuilder();
		int i = 2;
		while (i < strArr.length - 1) {
			task.append(strArr[i]);
			task.append(" ");
			i++;
		}
		task.append(strArr[i]);
		itm.setTask(task.toString());
		_taskList.put(_index, itm);
		_index++;

		System.out.println("Added task: " + task.toString() + " with priority " + Integer.parseInt(strArr[1]));
	}

	private static void DoneTask(String input) {

		// System.out.println("DeleteFunction Function - input = " + input);
		/*
		 * $ ./task done 1
		 * Marked item as done.
		 */

		int index = Integer.parseInt(input.split(" ")[1]);
		int indx = GetIndex(index);

		if (_taskList.containsKey(indx)) {
			Item b = _taskList.get(indx);
			b.setStatus(COMPLETED);
			System.out.println("Marked item as done.");
		} else {
			System.out.println("Error: no incomplete item with index " + index + " exists.");
		}
	}

	private static int GetIndex(int input) {
		int result = 0;

		Set<Integer> prt = new HashSet<Integer>();
		for (Map.Entry<Integer, Item> entry : _taskList.entrySet()) {
			Item b = entry.getValue();
			prt.add(b.getPriority());
		}

		Iterator<Integer> itr = prt.iterator();
		int indx = 1;
		while (itr.hasNext()) {
			Integer i = itr.next();
			for (Map.Entry<Integer, Item> entry : _taskList.entrySet()) {
				Item b = entry.getValue();
				if (b.getPriority() == i && b.getStatus() != COMPLETED) {
					if (input == indx++) {
						result = b.getIndex();
						break;
					}
				}
			}
		}

		return result;
	}

	private static void DeleteTask(String input) {
		/*
		 * $ ./task del 3
		 * Deleted item with index 3
		 */

		int index = Integer.parseInt(input.split(" ")[1]);
		int indx = GetIndex(index);

		if (_taskList.containsKey(indx)) {
			_taskList.remove(indx);
			System.out.println("Deleted item with index " + index);
		} else {
			System.out.println("Error: item with index " + index + " does not exist. Nothing deleted.");
		}
	}

	private static void ReportTask() {
		/*
		 * $ ./task report
		 * Pending : 2
		 * 1. this is a pending task [1]
		 * 2. this is a pending task with priority [4]
		 * 
		 * Completed : 3
		 * 1. completed task
		 * 2. another completed task
		 * 3. yet another completed task
		 */

		Integer pendingCount = 0;
		Integer completedCount = 0;

		for (Map.Entry<Integer, Item> entry : _taskList.entrySet()) {
			Item b = entry.getValue();
			if (b.getStatus() == COMPLETED) {
				completedCount++;
			} else if (b.getStatus() == PENDING) {
				pendingCount++;
			}
		}

		System.out.println("Pending : " + pendingCount);
		Integer i = 1;
		for (Map.Entry<Integer, Item> entry : _taskList.entrySet()) {
			Item b = entry.getValue();
			if (b.getStatus().equals(PENDING))
				System.out.println(i++ + ". " + b.getTask() + "[" + b.getPriority() + "]");
		}

		System.out.println("\nCompleted : " + completedCount);
		i = 1;
		for (Map.Entry<Integer, Item> entry : _taskList.entrySet()) {
			Item b = entry.getValue();
			if (b.getStatus().equals(COMPLETED))
				System.out.println(i++ + ". " + b.getTask() + "[" + b.getPriority() + "]");
		}
	}
}
