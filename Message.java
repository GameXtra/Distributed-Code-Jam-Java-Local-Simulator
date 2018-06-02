import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Message {
    public static void main(String[] args) {
        myNumberOfNode = 10;
        for (int i = 0; i < myNumberOfNode; i++) {
            qChar.add(new ArrayList<>());
            qInt.add(new ArrayList<>());
            qLong.add(new ArrayList<>());
        }
        for (int i = 0; i < myNumberOfNode; i++) {
            for (int j = 0; j < myNumberOfNode; j++) {
                qChar.get(i).add(new ConcurrentLinkedQueue<>());
                qInt.get(i).add(new ConcurrentLinkedQueue<>());
                qLong.get(i).add(new ConcurrentLinkedQueue<>());
            }
        }

        System.out.println("Multi thread answer:");
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < myNumberOfNode; i++) {
            final int j = i;
            threads.add(new Thread(() -> new Main(new Message(j)).solve()));
        }
        for (Thread thr : threads) thr.start();
        for (Thread thr : threads) {
            try {
                thr.join();
            } catch (Exception ignored) {
            }
        }
    }


    private static int myNumberOfNode;
    private static ArrayList<ArrayList<Queue<Character>>> qChar = new ArrayList<>();
    private static ArrayList<ArrayList<Queue<Integer>>> qInt = new ArrayList<>();
    private static ArrayList<ArrayList<Queue<Long>>> qLong = new ArrayList<>();

    public Message(int id) {
        this.myId = id;
    }

    public int NumberOfNodes() {
        return myNumberOfNode;
    }

    private int myId;

    public int MyNodeId() {
        return myId;
    }

    public void PutChar(int target, char value) {
        qChar.get(target).get(myId).add(value);
    }

    public void PutInt(int target, int value) {
        qInt.get(target).get(myId).add(value);
    }

    public void PutLL(int target, long value) {
        qLong.get(target).get(myId).add(value);
    }

    public void Send(int target) {
    }

    public int Receive(int source) {
        if (source != -1) return source;
        while (true) {
            for (int i = 0; i < myNumberOfNode; i++) {
                if (!qChar.get(myId).get(i).isEmpty() || !qInt.get(myId).get(i).isEmpty() || !qLong.get(myId).get(i).isEmpty())
                    return i;
            }
        }
    }

    public static final int waitTime = 10;

    public char GetChar(int source) {
        while (qChar.get(myId).get(source).isEmpty())
            try {
                Thread.sleep(waitTime);
            } catch (Exception ignored) {
            }
        return qChar.get(myId).get(source).poll();
    }

    public int GetInt(int source) {
        while (qInt.get(myId).get(source).isEmpty())
            try {
                Thread.sleep(waitTime);
            } catch (Exception ignored) {
            }
        return qInt.get(myId).get(source).poll();
    }

    public long GetLL(int source) {
        while (qLong.get(myId).get(source).isEmpty())
            try {
                Thread.sleep(waitTime);
            } catch (Exception ignored) {
            }
        return qLong.get(myId).get(source).poll();
    }
}