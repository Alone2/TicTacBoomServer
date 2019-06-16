
package ch.blobber.tictacboom;

import ch.jeda.Data;
import java.util.ArrayList;

/**
 *
 * @author alain.sinzig
 */
public class Server
{
    TcpClass tcp;
    final int port = 6465;
    int playerSize;
    int playerTurn;
    String[] spielfeld;
    boolean[] spaceTaken;
    TicTacBoomServer tac;
    
    private int saidByeToPlayer = 0;
    
    public Server(int playerSize, TicTacBoomServer tac) {
        this.playerSize = playerSize;
        this.tcp = new TcpClass(this, port);
        this.spielfeld = new String[playerSize];
        this.spaceTaken = new boolean[(int) Math.pow(playerSize + 1, 2)];
        this.playerTurn = 0;
        this.tac = tac;
        //this.start();
    }

    Data canPlayReturn(Data d) {
        int he = d.readInt("myself");
        Boolean canPlay =  tcp.players >= playerSize && playerTurn == he;
        d.writeBoolean("canPlay", canPlay);
        
        boolean hasWon = hasWon(he);
        boolean isFinished = isFinished();
        
        d.writeBoolean("hasWon", hasWon);
        d.writeBoolean("isFinished", isFinished);
        
        d.writeStrings("saveStringArr", spielfeld);
        
        if (isFinished)
            end();
        
        return d;
    }

    Data doMoveReturn(Data d) {
        int he = d.readInt("myself");    
        if (tcp.players >= playerSize && playerTurn == he) {
            String move = d.readString("move"); 
            // Do complicated thing of looking if cords alrady crossed
            int[] xy = new int[2];
            xy[0] = Integer.valueOf(String.valueOf(move.charAt(0)));
            xy[1] = Integer.valueOf(String.valueOf(move.charAt(1)));
            int number = xy[0] + xy[1]*(playerSize + 1);
            if (spaceTaken[number]) {
                d.writeStrings("saveStringArr", spielfeld);
                return d;
            }
            spaceTaken[number] = true;
            // then
            if (spielfeld[he] == null)
                spielfeld[he] = "";
            spielfeld[he] += move + "9";
        }
        playerTurn = (playerTurn + 1) % playerSize;
        d.writeStrings("saveStringArr", spielfeld);
        
        return d;
    }
    
    private boolean isFinished() {
        for (int i = 0; i < playerSize; i++) {
            if (hasWon(i))
                return true;
        }
        for (boolean i : spaceTaken) {
            if (!i)
                return false;
        }
        return true;
    }

    private boolean hasWon(int player) {
        if (player >= playerSize) 
            return false;
        String row = spielfeld[player];
        if (row == null || row.equals(""))
            return false;
        String newCode[] = row.split("9");
        int[][] xy = new int[newCode.length][2];
        for (int i = 0; i < newCode.length; i++) {
            xy[i][0] = Integer.valueOf(String.valueOf(newCode[i].charAt(0)));
            xy[i][1] = Integer.valueOf(String.valueOf(newCode[i].charAt(1)));
        }
        //für jede Reihe
        for (int i = 0; i < playerSize+1; i++) {
            ArrayList<Integer> inLineXY = new ArrayList<Integer>();
            ArrayList<Integer> inLineYX = new ArrayList<Integer>();
            for (int[] j : xy) {
                if (j[0] == i) {
                    inLineXY.add(j[1]);
                }
                if (j[1] == i) {
                    inLineYX.add(j[0]);
                }
            }
            
            if (testForLines(inLineXY) || testForLines(inLineYX))
                return true;
        }
        
        //Schrägen
        for (int i = 0; i < playerSize+1; i++) {
            ArrayList<Integer> inLineXX = new ArrayList<Integer>();
            ArrayList<Integer> inLineXY = new ArrayList<Integer>();
            ArrayList<Integer> inLineYX = new ArrayList<Integer>();
            ArrayList<Integer> inLineYY = new ArrayList<Integer>();
            for (int j = 0; j < playerSize+1; j++) {
                //if (!(i + j > playerSize +1)) {
                    inLineXX.add(i + j);
                    inLineXY.add(j);
                    inLineYY.add(j);
                    inLineYX.add(i - j);
                //}
            }
            
            if (inLineXX.size() >= 3) {
                if (testForDiagonal(inLineXX, inLineXY, xy) || testForDiagonal(inLineYX, inLineYY, xy))
                    return true;
            }
            
        }
        
        return false;
    }
        
    private boolean testForDiagonal(ArrayList<Integer> line, ArrayList<Integer> line2, int[][] xy) {
        int streak = 0;
        for (int k = 0; k < line.size(); k++) {
            boolean hasNumber = false;
            for (int j = 0; j < xy.length; j++) {
                if (line.get(k) == xy[j][0] && line2.get(k) == xy[j][1]) {
                    hasNumber = true;
                    break;
                }
            }
            if (hasNumber) {
                streak++;
            } else {
                streak = 0;
            }
        }
        if (streak >= 3) 
            return true;
        return false;
        
    }
    
    private boolean testForLines(ArrayList<Integer> line) {
        int streak = 0;
        for (int j = 0; j < playerSize+1; j++) {
            boolean hasNumber = false;
            for (int k = 0; k < line.size(); k++) {
                if (j == line.get(k)) {
                    hasNumber = true;
                    break;
                }
            }
            if (hasNumber) {
                streak++;
            } else {
                streak = 0;
            }
        }
        if (streak >= 3) 
            return true;
        return false;
    }

    
    private void end() {
        saidByeToPlayer++;
        if (saidByeToPlayer < playerSize)
            return;
        this.spielfeld = new String[playerSize];
        this.spaceTaken = new boolean[(int) Math.pow(playerSize + 1, 2)];
        this.playerTurn = 0;
        this.saidByeToPlayer = 0;
        
        System.out.println("resetting Server");
        //tcp.close();
        //tac.reset();
    }
}
