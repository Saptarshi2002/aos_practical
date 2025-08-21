package distributed_system_os.assignment4;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Raymond's algorithm");
        System.out.print("Enter total node of sites you need: ");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        // Creating all the sites
        ArrayList<Site> sites = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            sites.add(new Site(i));
        }

        // Assigning holder sites for all the sites
        for (int i = 0; i < n; i++) {
            System.out.print("Enter the holder of site " + i + " : ");
            int s = sc.nextInt();
            sites.get(i).holder = sites.get(s);
            if (i == s) {
                sites.get(i).token = true;
            } else {
                sites.get(i).token = false;
            }
        }
        sc.close();
        for (Site s : sites) {
            System.out.println("Holder of Site: " + s.site_id + " is " + s.holder.site_id);
        }
        // Running all the Sites as different threads
        for (Site s : sites) {
            new Thread(s).start();
        }
    }
}
