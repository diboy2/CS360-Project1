/**
 * ClueReasoner.java - project skeleton for a propositional reasoner
 * for the game of Clue.  Unimplemented portions have the comment "TO
 * BE IMPLEMENTED AS AN EXERCISE".  The reasoner does not include
 * knowledge of how many cards each player holds.  See
 * http://cs.gettysburg.edu/~tneller/nsf/clue/ for details.
 *
 * @author Todd Neller
 * @version 1.0
 *

Copyright (C) 2005 Todd Neller

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

Information about the GNU General Public License is available online at:
  http://www.gnu.org/licenses/
To receive a copy of the GNU General Public License, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
02111-1307, USA.

 */

import java.io.*;
import java.util.*;

public class ClueReasoner 
{
    private int numPlayers;
    private int playerNum;
    private int numCards;
    private SATSolver solver;    
    private String caseFile = "cf";
    private String[] players = {"sc", "mu", "wh", "gr", "pe", "pl"};
    private String[] suspects = {"mu", "pl", "gr", "pe", "sc", "wh"};
    private String[] weapons = {"kn", "ca", "re", "ro", "pi", "wr"};
    private String[] rooms = {"ha", "lo", "di", "ki", "ba", "co", "bi", "li", "st"};
    private String[] cards;

    public ClueReasoner()
    {
        numPlayers = players.length;

        // Initialize card info
        cards = new String[suspects.length + weapons.length + rooms.length];
        int i = 0;
        for (String card : suspects)
            cards[i++] = card;
        for (String card : weapons)
            cards[i++] = card;
        for (String card : rooms)
            cards[i++] = card;
        numCards = i;

        // Initialize solver
        solver = new SATSolver();
        addInitialClauses();
    }

    private int getPlayerNum(String player) 
    {
        if (player.equals(caseFile))
            return numPlayers;
        for (int i = 0; i < numPlayers; i++)
            if (player.equals(players[i]))
                return i;
        System.out.println("Illegal player: " + player);
        return -1;
    }

    private int getCardNum(String card)
    {
        for (int i = 0; i < numCards; i++)
            if (card.equals(cards[i]))
                return i;
        System.out.println("Illegal card: " + card);
        return -1;
    }

    private int getPairNum(String player, String card) 
    {
        return getPairNum(getPlayerNum(player), getCardNum(card));
    }

    private int getPairNum(int playerNum, int cardNum)
    {
        return playerNum * numCards + cardNum + 1;
    }    

    public void addInitialClauses() 
    {
        // TO BE IMPLEMENTED AS AN EXERCISE
        
        // Each card is in at least one place (including case file).
        for (int c = 0; c < numCards; c++) {
            int[] clause = new int[numPlayers + 1];
            for (int p = 0; p <= numPlayers; p++)
                clause[p] = getPairNum(p, c);
            solver.addClause(clause);
        }    
        
        // If a card is one place, it cannot be in another place.
        for(int p = 0; p <= numPlayers; p++){
            int[] clause = new int[2];
            for(int c = 0; c < numCards; c++){
                for(int q = 0; q <= numPlayers; q++ ){
                    // c1 = the card is one place;
                    // c2 = the card is in this other place;
                    // c1 => ~c2 
                    // ~c1 v ~c2
                    if(q!=p){
                        clause[0] = -getPairNum(p,c);
                        clause[1] = -getPairNum(q,c);
                        solver.addClause(clause);
                    }
                }
            }
        }
        
          
        // At least one card of each category is in the case file.
        
        // Add clauses for suspect category
        int[] clause = new int[suspects.length];
        for (int c = 0; i< suspects.length; c++){
            clause[c] = getPairNum("cf",suspects[c]);
        }    
        solver.addClause(clause);

        // Add clauses for weapons category
        int[] clause = new int[weapons.length];
        for (int c = 0; i< weapons.length; c++){
            clause[c] = getPairNum("cf",weapons[c]);
        }    
        solver.addClause(clause);

        int[] clause = new int[rooms.length];
        for (int c = 0; i< rooms.length; c++){
            clause[c] = getPairNum("cf",rooms[c]);
        }    
        solver.addClause(clause);


        // No two cards in each category can both be in the case file.
        
        // Add clauses for suspects category
        for (String cardInFile : suspects){
            int[] clause = new int[2];
            for(String cardNotInFile : suspects){
                // c1 = the card is with case file
                // c2 = another card is with case file
                // c1 => ~c2
                // ~c1 v ~c2
                if(!cardNotInFile.equals(cardInFile)){
                    clause[0] = -getPairNum("cf",cardInFile);
                    clause[1] = -getPairNum("cf",cardNotInFile);
                    solver.addClause(clause);
                }
            }
        }
        // Add clauses for weapons category
        for (String cardInFile : weapons){
            int[] clause = new int[2];
            for(String cardNotInFile : weapons){
                // c1 = the card is with case file
                // c2 = another card is with case file
                // c1 => ~c2
                // ~c1 v ~c2
                if(!cardNotInFile.equals(cardInFile)){
                    clause[0] = -getPairNum("cf",cardInFile);
                    clause[1] = -getPairNum("cf",cardNotInFile);
                    solver.addClause(clause);
                }
            }
        }
        // Add clauses for rooms category
        for (String cardInFile : rooms){
            int[] clause = new int[2];
            for(String cardNotInFile : rooms){
                // c1 = the card is with case file
                // c2 = another card is with case file
                // c1 => ~c2
                // ~c1 v ~c2
                if(!cardNotInFile.equals(cardInFile)){
                    clause[0] = -getPairNum("cf",cardInFile);
                    clause[1] = -getPairNum("cf",cardNotInFile);
                    solver.addClause(clause);
                }
            }
        }
    }
        
    public void hand(String player, String[] cards) 
    {
        playerNum = getPlayerNum(player);

        // TO BE IMPLEMENTED AS AN EXERCISE
        String playerCards = cards;
        System.out.println("The cards are: " + playerCards );
        int clause;
        for(String card: playerCards)
        {
            clause = -getPairNum("cf",card);
            solver.addClause(clause);
            clause = getPairNum(player,card);
            solver.addClause(clause);
        }

    }

    public void suggest(String suggester, String card1, String card2, 
                        String card3, String refuter, String cardShown) 
    {
        // TO BE IMPLEMENTED AS AN EXERCISE
        int suggesterIndex = java.util.Arrays.binarySearch(players, suggester);
        int refuterIndex; 
        if(refuter!=null){
            refuterIndex = java.util.Arrays.binarySearch(players, refuter);
        }
        
        // when no one refutes suggestion
        if(refuter == null){
           for(String player: players ){
                int clause;
                if(!player.equals(suggester)){
                    clause = -getPairNum(player,card1);
                    solver.addClause(clause);
                    clause = -getPairNum(player,card2);
                    solver.addClause(clause);
                    clause = -getPairNum(player,card3);
                    solver.addClause(clause);
                } 
           } 
        }

        // when someone refutes and shows card
        if(cardShown != null){
           
           //refuter has card
           int clause;
           clause = getPairNum(refuter,cardShown);
           solver.addClause(clause);
           clause = -getPairNum("cf",cardShown);
           solver.addClause(clause);
           
           
           /* Add clauses that players in between refuter and suggester
                do not have cards 1,2,3.
           */
           int i = ++suggesterIndex%numPlayers;

           while(i!=refuterIndex){
                int clause = -getPairNum(players[i],card1);
                solver.addClause(clause);
                clause = -getPairNum(players[i],card2);
                solver.addClause(clause);
                clause = -getPairNum(players[i],card3);
                solver.addClause(clause);
           } 
        }

        // when someone refutes and doesn't show card
        if(cardShown = null && refuter!= null){
            /* Add clauses that players in between refuter and suggester
                do not have cards 1,2,3.
            */
            int i = ++suggesterIndex%numPlayers;
            while(i!=refuterIndex){
                int clause = -getPairNum(players[i],card1);
                solver.addClause(clause);
                clause = -getPairNum(players[i],card2);
                solver.addClause(clause);
                clause = -getPairNum(players[i],card3);
                solver.addClause(clause);
            }

            // refuter has one the cards 
            int[] clause = int[3];
            clause[0] = getPairNum(refuter,card1);
            clause[1] = getPairNum(refuter,card2);
            clause[2] = getPairNum(refuter,card3);
            solver.addClause(clause);

                
        }

    }

    // Adrian: Added code. Just a contains() function for java arrays
    public void contains(String[] array, String element){
        for(String a: array){
            if(a.equals(element)){
                return true;
            }
            return false;
        }
    }



    public void accuse(String accuser, String card1, String card2, 
                       String card3, boolean isCorrect)
    {
        // TO BE IMPLEMENTED AS AN EXERCISE
    }

    public int query(String player, String card) 
    {
        return solver.testLiteral(getPairNum(player, card));
    }

    public String queryString(int returnCode) 
    {
        if (returnCode == SATSolver.TRUE)
            return "Y";
        else if (returnCode == SATSolver.FALSE)
            return "n";
        else
            return "-";
    }
        
    public void printNotepad() 
    {
        PrintStream out = System.out;
        for (String player : players)
            out.print("\t" + player);
        out.println("\t" + caseFile);
        for (String card : cards) {
            out.print(card + "\t");
            for (String player : players) 
                out.print(queryString(query(player, card)) + "\t");
            out.println(queryString(query(caseFile, card)));
        }
    }
        
    public static void main(String[] args) 
    {
        ClueReasoner cr = new ClueReasoner();
        String[] myCards = {"wh", "li", "st"};
        cr.hand("sc", myCards);
        cr.suggest("sc", "sc", "ro", "lo", "mu", "sc");
        cr.suggest("mu", "pe", "pi", "di", "pe", null);
        cr.suggest("wh", "mu", "re", "ba", "pe", null);
        cr.suggest("gr", "wh", "kn", "ba", "pl", null);
        cr.suggest("pe", "gr", "ca", "di", "wh", null);
        cr.suggest("pl", "wh", "wr", "st", "sc", "wh");
        cr.suggest("sc", "pl", "ro", "co", "mu", "pl");
        cr.suggest("mu", "pe", "ro", "ba", "wh", null);
        cr.suggest("wh", "mu", "ca", "st", "gr", null);
        cr.suggest("gr", "pe", "kn", "di", "pe", null);
        cr.suggest("pe", "mu", "pi", "di", "pl", null);
        cr.suggest("pl", "gr", "kn", "co", "wh", null);
        cr.suggest("sc", "pe", "kn", "lo", "mu", "lo");
        cr.suggest("mu", "pe", "kn", "di", "wh", null);
        cr.suggest("wh", "pe", "wr", "ha", "gr", null);
        cr.suggest("gr", "wh", "pi", "co", "pl", null);
        cr.suggest("pe", "sc", "pi", "ha", "mu", null);
        cr.suggest("pl", "pe", "pi", "ba", null, null);
        cr.suggest("sc", "wh", "pi", "ha", "pe", "ha");
        cr.suggest("wh", "pe", "pi", "ha", "pe", null);
        cr.suggest("pe", "pe", "pi", "ha", null, null);
        cr.suggest("sc", "gr", "pi", "st", "wh", "gr");
        cr.suggest("mu", "pe", "pi", "ba", "pl", null);
        cr.suggest("wh", "pe", "pi", "st", "sc", "st");
        cr.suggest("gr", "wh", "pi", "st", "sc", "wh");
        cr.suggest("pe", "wh", "pi", "st", "sc", "wh");
        cr.suggest("pl", "pe", "pi", "ki", "gr", null);
        cr.printNotepad();
        cr.accuse("sc", "pe", "pi", "bi", true);
    }           
}
