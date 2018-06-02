import java.util.ArrayList;

public class Main {
    //############ COMMENT THOSE LINE BEFORE SUBMISSION!!
    private Message message;

    public Main(Message message) {
        this.message = message;
    }
    //############ THOSE LINES! ^^


    public Main() {
    }

    public static void main(String[] args) {
        Main m1 = new Main();
        m1.solve();
    }


    public void solve() {
        int myInd = message.MyNodeId();
        int non = message.NumberOfNodes();

        Solver solver = new Solver(myInd, non);
        solver.solve();
    }

    private class Solver {
        int myId;
        int numberOfNodes;

        Solver(int myId, int numberOfNode) {
            this.myId = myId;
            this.numberOfNodes = numberOfNode;
        }

        long[][] ranges;
        long numberOfBlocks = baby_blocks.GetNumberOfBlocks();
        long[] rangeSums;

        void solve() {
            ranges = splitRanges(numberOfBlocks, numberOfNodes);
            sendToEveryone(calcMyRangeSum());
            rangeSums = receiveLongFromEveryone();
            sumOverRangeSumToGetSumArray();
            computeRangesToCheck();
            sendToControl(calcResultInRangeAllocatedToMe());
            printSumIfYouAreTheMasterNode();
        }


        private long calcMyRangeSum() {
            long sum = 0;
            long rangeStart = ranges[myId][0];
            long rangeEnd = ranges[myId][1];
            for (long i = rangeStart; i < rangeEnd; i++) {
                sum += baby_blocks.GetBlockWeight(i);
            }
            return sum;
        }


        private void sumOverRangeSumToGetSumArray() {
            long[] rangeSums1 = new long[numberOfNodes + 1];
            for (int i = 0; i < numberOfNodes; i++) {
                rangeSums1[i + 1] = rangeSums[i];
                rangeSums1[i + 1] += rangeSums1[i];
            }
            rangeSums = rangeSums1;
        }

        boolean rangeCollide(int a, int b) {
            long lA, rA, lB, rB;
            long total = rangeSums[rangeSums.length - 1];

            lA = rangeSums[a];
            rA = rangeSums[a + 1];

            lB = total - rangeSums[b + 1];
            rB = total - rangeSums[b];
            return ((lB <= lA && lA <= rB) || (lA <= lB && lB <= rA));
        }

        ArrayList<Integer> rangeLeftCheck = new ArrayList<>();
        ArrayList<Integer> rangeRightCheck = new ArrayList<>();

        private void computeRangesToCheck() {
            for (int i = 0; i < numberOfNodes; i++) {
                for (int j = i; j < numberOfNodes; j++) {
                    if (rangeCollide(i, j)) {
                        rangeLeftCheck.add(i);
                        rangeRightCheck.add(j);
                    }
                }
            }
        }

        long numberOfCollidingElements = 0;

        private long calcResultInRangeAllocatedToMe() {
            for (int i = 0; i < rangeRightCheck.size(); i++) {
                if (i % numberOfNodes == myId) {
                    checkRange(rangeLeftCheck.get(i), rangeRightCheck.get(i));
                }
            }
            return numberOfCollidingElements;
        }

        private void checkRange(int a, int b) {

            long aStartIndex = ranges[a][0];
            long aEndIndex = ranges[a][1];
            long bStartIndex = ranges[b][1] - 1;
            long bEndIndex = ranges[b][0] - 1;

            long total = rangeSums[rangeSums.length - 1];

            if (aStartIndex == aEndIndex || bStartIndex == bEndIndex) return;
            long aSum = rangeSums[a] + baby_blocks.GetBlockWeight(aStartIndex);
            long bSum = total - rangeSums[b + 1] + baby_blocks.GetBlockWeight(bStartIndex);
            while (true) {
                boolean updateA = false;
                boolean updateB = false;
                if (aStartIndex >= bStartIndex) return;
                if (aSum == bSum) {
                    numberOfCollidingElements++;
                    updateA = true;
                    updateB = true;
                } else if (aSum < bSum) {
                    updateA = true;
                } else {
                    updateB = true;
                }
                if (updateA) {
                    aStartIndex++;
                    if (aStartIndex == aEndIndex) return;
                    aSum += baby_blocks.GetBlockWeight(aStartIndex);
                }
                if (updateB) {
                    bStartIndex--;
                    if (bStartIndex == bEndIndex) return;
                    bSum += baby_blocks.GetBlockWeight(bStartIndex);
                }
            }
        }

        private void printSumIfYouAreTheMasterNode() {
            if (myId != 0) return;
            long res = 0;
            for (int i = 0; i < numberOfNodes; i++) {
                message.Receive(i);
                res += message.GetLL(i);
            }
            System.out.println(res);
        }


        //Helper Function
        private long[][] splitRanges(long numberOfElements, int numberOfNodes) {
            long[][] ranges = new long[numberOfNodes][2];
            long jumps = numberOfElements / numberOfNodes;
            for (int i = 0; i < numberOfNodes; i++) {
                ranges[i][0] = i * jumps;
                ranges[i][1] = (i + 1) * jumps;
            }
            ranges[ranges.length - 1][1] = numberOfElements;
            return ranges;
        }

        private long[] receiveLongFromEveryone() {
            long[] res = new long[numberOfNodes];
            for (int i = 0; i < numberOfNodes; i++) {
                message.Receive(i);
                res[i] = message.GetLL(i);
            }
            return res;
        }

        private int[] receiveIntFromEveryone() {
            int[] res = new int[numberOfNodes];
            for (int i = 0; i < numberOfNodes; i++) {
                message.Receive(i);
                res[i] = message.GetInt(i);
            }
            return res;
        }

        private char[] receiveCharFromEveryone() {
            char[] res = new char[numberOfNodes];
            for (int i = 0; i < numberOfNodes; i++) {
                message.Receive(i);
                res[i] = message.GetChar(i);
            }
            return res;
        }

        private void sendToEveryone(long l) {
            for (int i = 0; i < numberOfNodes; i++) {
                message.PutLL(i, l);
                message.Send(i);
            }
        }

        private void sendToEveryone(int l) {
            for (int i = 0; i < numberOfNodes; i++) {
                message.PutInt(i, l);
                message.Send(i);
            }
        }

        private void sendToEveryone(char l) {
            for (int i = 0; i < numberOfNodes; i++) {
                message.PutChar(i, l);
                message.Send(i);
            }
        }

        private void sendToControl(long l) {
            message.PutLL(0, l);
            message.Send(0);
        }

        private void sendToControl(int l) {
            message.PutInt(0, l);
            message.Send(0);
        }

        private void sendToControl(char l) {
            message.PutChar(0, l);
            message.Send(0);
        }

        private long[] reciveLongArray(int from) {
            long[] res;
            message.Receive(from);
            int n = message.GetInt(from);
            res = new long[n];
            for (int i = 0; i < n; i++) {
                res[i] = message.GetLL(from);
            }
            return res;
        }

        private int[] reciveIntArray(int from) {
            int[] res;
            message.Receive(from);
            int n = message.GetInt(from);
            res = new int[n];
            for (int i = 0; i < n; i++) {
                res[i] = message.GetInt(from);
            }
            return res;
        }

        private char[] reciveCharArray(int from) {
            char[] res;
            message.Receive(from);
            int n = message.GetInt(from);
            res = new char[n];
            for (int i = 0; i < n; i++) {
                res[i] = message.GetChar(from);
            }
            return res;
        }

        private void sendArray(long[] arr, int target) {
            message.PutInt(target, arr.length);
            for (long anArr : arr) {
                message.PutLL(target, anArr);
            }
            message.Send(target);
        }

        private void sendArray(int[] arr, int target) {
            message.PutInt(target, arr.length);
            for (int anArr : arr) {
                message.PutInt(target, anArr);
            }
            message.Send(target);
        }

        private void sendArray(char[] arr, int target) {
            message.PutInt(target, arr.length);
            for (char anArr : arr) {
                message.PutChar(target, anArr);
            }
            message.Send(target);
        }
    }

}
