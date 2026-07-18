import java.util.*;

public class GymManager {
    private static List<Member> membersList = new ArrayList<>();
    private static Map<Integer, Member> memberMap = new TreeMap<>();
    private static List<GymClass> gymClasses = new ArrayList<>();
    private static Stack<Action> undoStack = new Stack<>();

    private static Scanner scanner = new Scanner(System.in);
    private static int nextMemberId = 1;

    public static void main(String[] args) {
        try {
            initializeData();

            while (true) {
                System.out.println("\n===== FITNESS GYM MANAGEMENT SYSTEM =====");
                System.out.println("1. Add Member");
                System.out.println("2. Remove Member");
                System.out.println("3. Update Member");
                System.out.println("4. Search Member by ID");
                System.out.println("5. List All Members (sorted by name)");
                System.out.println("6. Create Gym Class");
                System.out.println("7. Enroll Member in Class");
                System.out.println("8. Cancel Enrollment");
                System.out.println("9. View Class Details");
                System.out.println("10. Undo Last Action");
                System.out.println("11. Exit");
                System.out.print("Enter choice: ");

                int choice = readInt();
                switch (choice) {
                    case 1:
                        addMember();
                        break;
                    case 2:
                        removeMember();
                        break;
                    case 3:
                        updateMember();
                        break;
                    case 4:
                        searchMember();
                        break;
                    case 5:
                        listMembers();
                        break;
                    case 6:
                        createClass();
                        break;
                    case 7:
                        enrollMember();
                        break;
                    case 8:
                        cancelEnrollment();
                        break;
                    case 9:
                        viewClass();
                        break;
                    case 10:
                        undo();
                        break;
                    case 11:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initializeData() {
        addMemberInternal("Udara Dilan", "0771234567", "Monthly");

        GymClass yoga = new GymClass("Yoga", "Sarah", 2);
        GymClass spin = new GymClass("Spin", "Mike", 2);
        gymClasses.add(yoga);
        gymClasses.add(spin);

        // Enroll Alice and Bob in Yoga
        yoga.enrollMember(memberMap.get(1));
        yoga.enrollMember(memberMap.get(2));

        // Enroll Alice and Bob in Spin (Carol goes to waitlist)
        spin.enrollMember(memberMap.get(1));
        spin.enrollMember(memberMap.get(2));
        spin.enrollMember(memberMap.get(3)); // Carol -> waitlist

        System.out.println("Sample data loaded successfully.");
    }

    private static void addMemberInternal(String name, String phone, String type) {
        Member m = new Member(nextMemberId++, name, phone, type);
        membersList.add(m);
        memberMap.put(m.getId(), m);
    }

    private static void addMember() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter membership type (Monthly/Yearly): ");
        String type = scanner.nextLine();

        Member m = new Member(nextMemberId++, name, phone, type);
        membersList.add(m);
        memberMap.put(m.getId(), m);
        undoStack.push(new Action(Action.ActionType.ADD_MEMBER, m, null));
        System.out.println("Member added with ID: " + m.getId());
    }

    private static void removeMember() {
        System.out.print("Enter member ID to remove: ");
        int id = readInt();
        Member m = memberMap.remove(id);
        if (m != null) {
            membersList.remove(m);
            for (GymClass gc : gymClasses) {
                gc.getEnrolledMembers().remove(m);
                gc.getWaitlist().remove(m);
            }
            undoStack.push(new Action(Action.ActionType.REMOVE_MEMBER, m, null));
            System.out.println("Member removed.");
        } else {
            System.out.println("Member not found.");
        }
    }

    private static void updateMember() {
        System.out.print("Enter member ID to update: ");
        int id = readInt();
        Member m = memberMap.get(id);
        if (m == null) {
            System.out.println("Member not found.");
            return;
        }
        System.out.print("Enter new name (press Enter to keep): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) m.setName(name);

        System.out.print("Enter new phone (press Enter to keep): ");
        String phone = scanner.nextLine();
        if (!phone.isEmpty()) m.setPhone(phone);

        System.out.print("Enter new membership type (press Enter to keep): ");
        String type = scanner.nextLine();
        if (!type.isEmpty()) m.setMembershipType(type);

        System.out.println("Member updated.");
    }

    private static void searchMember() {
        System.out.print("Enter member ID: ");
        int id = readInt();
        Member m = memberMap.get(id);
        if (m != null) {
            System.out.println(m);
        } else {
            System.out.println("Member not found.");
        }
    }

    private static void listMembers() {
        if (membersList.isEmpty()) {
            System.out.println("No members.");
            return;
        }
        List<Member> sorted = new ArrayList<>(membersList);
        sorted.sort(Comparator.comparing(Member::getName));
        System.out.println("Members (sorted by name):");
        for (Member m : sorted) {
            System.out.println(m);
        }
    }

    private static void createClass() {
        System.out.print("Enter class name: ");
        String name = scanner.nextLine();
        System.out.print("Enter instructor: ");
        String instructor = scanner.nextLine();
        System.out.print("Enter capacity: ");
        int capacity = readInt();

        GymClass gc = new GymClass(name, instructor, capacity);
        gymClasses.add(gc);
        System.out.println("Class created.");
    }

    private static void enrollMember() {
        System.out.print("Enter member ID: ");
        int id = readInt();
        Member m = memberMap.get(id);
        if (m == null) {
            System.out.println("Member not found.");
            return;
        }

        System.out.print("Enter class name: ");
        String className = scanner.nextLine();
        GymClass gc = findClass(className);
        if (gc == null) {
            System.out.println("Class not found.");
            return;
        }

        if (gc.isEnrolled(m)) {
            System.out.println("Member already enrolled.");
            return;
        }

        boolean direct = gc.enrollMember(m);
        if (direct) {
            System.out.println("Member enrolled in class.");
        } else {
            System.out.println("Class full. Member added to waitlist.");
        }
        undoStack.push(new Action(Action.ActionType.ENROLL, m, className));
    }

    private static void cancelEnrollment() {
        System.out.print("Enter member ID: ");
        int id = readInt();
        Member m = memberMap.get(id);
        if (m == null) {
            System.out.println("Member not found.");
            return;
        }

        System.out.print("Enter class name: ");
        String className = scanner.nextLine();
        GymClass gc = findClass(className);
        if (gc == null) {
            System.out.println("Class not found.");
            return;
        }

        boolean removed = gc.cancelEnrollment(m);
        if (removed) {
            System.out.println("Enrollment cancelled.");
            undoStack.push(new Action(Action.ActionType.CANCEL, m, className));
        } else {
            System.out.println("Member not enrolled in this class.");
        }
    }

    private static void viewClass() {
        System.out.print("Enter class name: ");
        String className = scanner.nextLine();
        GymClass gc = findClass(className);
        if (gc == null) {
            System.out.println("Class not found.");
            return;
        }

        System.out.println(gc);
        System.out.println("Enrolled Members:");
        for (Member m : gc.getEnrolledMembers()) {
            System.out.println("  " + m);
        }
        System.out.println("Waitlist:");
        for (Member m : gc.getWaitlist()) {
            System.out.println("  " + m);
        }
    }

    private static void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }

        Action action = undoStack.pop();
        switch (action.getType()) {
            case ADD_MEMBER:
                Member m1 = (Member) action.getData1();
                memberMap.remove(m1.getId());
                membersList.remove(m1);
                for (GymClass gc : gymClasses) {
                    gc.getEnrolledMembers().remove(m1);
                    gc.getWaitlist().remove(m1);
                }
                System.out.println("Undo: Member added - removed.");
                break;

            case REMOVE_MEMBER:
                Member m2 = (Member) action.getData1();
                memberMap.put(m2.getId(), m2);
                membersList.add(m2);
                System.out.println("Undo: Member removed - restored.");
                break;

            case ENROLL:
                Member m3 = (Member) action.getData1();
                String className1 = (String) action.getData2();
                GymClass gc1 = findClass(className1);
                if (gc1 != null) {
                    boolean removed = gc1.cancelEnrollment(m3);
                    if (removed) {
                        System.out.println("Undo: Enrollment cancelled.");
                    } else {
                        if (gc1.getWaitlist().remove(m3)) {
                            System.out.println("Undo: Waitlist entry removed.");
                        } else {
                            System.out.println("Undo failed: member not found in class.");
                        }
                    }
                }
                break;

            case CANCEL:
                Member m4 = (Member) action.getData1();
                String className2 = (String) action.getData2();
                GymClass gc2 = findClass(className2);
                if (gc2 != null) {
                    gc2.enrollMember(m4);
                    System.out.println("Undo: Enrollment restored.");
                }
                break;
        }
    }

    private static GymClass findClass(String name) {
        for (GymClass gc : gymClasses) {
            if (gc.getClassName().equalsIgnoreCase(name)) {
                return gc;
            }
        }
        return null;
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Please enter again: ");
            }
        }
    }
}