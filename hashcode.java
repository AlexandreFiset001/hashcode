import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.Math;
import java.awt.Point;
import java.util.*;

public class hashcode{

    private static PriorityQueue<Path> map[][];
    private static File file = new File("a_example.in");
    private static File file2 = new File("b_should_be_easy.in");
    private static File file3 = new File("c_no_hurry.in");
    private static File file4 = new File("d_metropolis.in");
    private static File file5 = new File("e_high_bonus.in");

    static Scanner scan;

    static {
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static ArrayDeque<Point> stack;

    private static Car arrayOfCars[];

    private static int R;
    private static int C;
    private static int F;
    private static int N;
    private static int B;
    private static int T;
    private static boolean[][] checked = new boolean[R][C];

    public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException {

        //Scanner
        R = scan.nextInt(); //Rows in grid
        C = scan.nextInt(); //Columns in grid
        F = scan.nextInt(); //#Vehicles in the fleet
        N = scan.nextInt(); //Number of rides
        B = scan.nextInt(); //Per-ride bonus for starting ride on time
        T = scan.nextInt(); //Number of steps
        scan.nextLine();
        map = new PriorityQueue[R][C];
        for(int i = 0 ; i<N; i++)
        {
            readLine(i);
            scan.nextLine();
        }
        //Liste des noms de fichiers:
        scan.close();
        ///Fin du scanner
        arrayOfCars = new Car[F];
        for(int i = 0; i<F ; i++){
            arrayOfCars[i]=new Car(0,new ArrayList<Integer>(),0,0,0);
        }

        for(int i = 0; i<F; i++){
            begin(arrayOfCars[i]);
        }

        PrintWriter pw = new PrintWriter("result.out","UTF-8");
        for(int i = 0; i<F; i++){
            Car c = arrayOfCars[i];
            String str = String.valueOf(c.nbOfRides);
            Iterator iter = c.list.iterator();
            while(iter.hasNext()){
                str = str + " " + Integer.parseInt((String) iter.next());
            }
            pw.println(str);
        }
        pw.close();
    }

    public static void begin(Car c){
        Path p = bfs(c);
        if(p!=null){
            if(c.time+p.weight<T){
                c.time = c.time+p.weight;
                c.x = p.endX;
                c.y = p.endY;
                c.nbOfRides++;
                c.list.add(p.number);
                begin(c);
            }
        }
    }

    public static void readLine(int i){
        int a = scan.nextInt();
        int b = scan.nextInt();
        if(map[a][b] == null){
            map[a][b] = new PriorityQueue();
        }
        int x = scan.nextInt();
        int y = scan.nextInt();
        int s = scan.nextInt();
        int f = scan.nextInt();
        map[a][b].add(new Path(i,Math.abs(a-x)+Math.abs(b-y),s,x,y,f));
        return;
    }

    private boolean isFeasable(Path p, Car c){
        return c.time+p.weight>T;
    }

    public static Path bfs(Car current){
        //retourne la coordonnee du point le plus proche

        stack = new ArrayDeque();
        Point start = new Point(current.getX(),current.getY());
        stack.addLast(start);
        int n = 0;
        int max = 0;
        while( !stack.isEmpty()){
            Point point = stack.removeFirst();
            int pointY = (int) point.getX();
            int pointX = (int) point.getY();
            if(map[pointX][pointY]!=null){
                PriorityQueue<Path> temp =  map[pointX][pointY];
                int taille = temp.size();
                int it =0;
                while(it < taille)
                {
                    it ++;
                    Path temps = temp.poll();
                    if(temps.weight+current.time< T)
                    {
                        return temps;
                    }
                    else
                    {
                        temps.weight = Integer.MAX_VALUE;
                        temp.add(temps);
                    }
                    if(temp.isEmpty()){
                        map[pointX][pointY] = null;
                    }
                }
            }
            loop(point);

        }
        return null;

    }

    public static void loop(Point point){

        int pointY = (int) point.getX();
        int pointX = (int) point.getY();


        if(pointX < 0 || pointY < 0 || pointX >= R || pointY >= C){
            if(checked[pointX][pointY]){
              return;
            }
            return;
        }
        checked[pointX][pointY]=true;
        stack.addLast(new Point(pointX+1,pointY));
        stack.addLast(new Point(pointX,pointY+1));
        stack.addLast(new Point(pointX,pointY-1));
        stack.addLast(new Point(pointX-1,pointY));
    }

    public static class Path implements Comparable{
        private int number;
        private int weight;
        private int earliestStart;
        private int endX;
        private int endY;
        private int latestFinish;

        public Path(int number, int w, int es, int eX, int eY, int lf){
            this.number = number;
            this.weight = w;
            this.earliestStart = es;
            this.endY = eX;
            this.endX = eY;
            this.latestFinish = lf;

        }

        @Override
        public int compareTo(Object p){
            if(p instanceof Path){
                Path path = (Path) p;
                return this.earliestStart-path.earliestStart;
            }else{
                return -1;
            }
        }
    }

    public static class Car {
        private int nbOfRides;
        private ArrayList<Integer> list = new ArrayList<Integer>();
        private int time;
        private int x;
        private int y;

        public Car(int nbOfRides, ArrayList<Integer> list, int t, int x, int y){
            this.nbOfRides = nbOfRides;
            this.list = list;
            this.time = t;
            this.y = x;
            this.x = y;
        }

        private int getX(){
            return x;
        }

        private int getY(){
            return y;
        }
    }

}
