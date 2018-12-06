package main;

import shared.*;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server
{
    private ServerSocket serverSocket;
    private List<Player> players;
    private GameMaster gameMaster;
    private CommandBuilder commandBuilder;


    Server(int port) throws Exception
    {
        System.out.println("Uruchamianie serwera...");
        try
        {
            serverSocket = new ServerSocket(port);
        } catch (Exception e)
        {
            throw new Exception("Nie można uzyskać dostępu to portu " + port);
        }
    }

    void createMatch(int playersNum, int botsNum)
    {
        //TODO tymczasowo testowane jest połączenie z jednym klientem
        gameMaster = new GameMaster(new BasicMovementStrategy(), new ClassicBoardFactory());
        commandBuilder = new CommandBuilder();
        players = new ArrayList<>();
        try
        {
            System.out.println("Oczekiwanie na klienta");
            players.add(new RealPlayer(serverSocket.accept(), "R"));
        } catch (Exception e)
        {
            System.out.println("Wystąpił błąd przy próbie połączenia z klientem");
        }

        System.out.println("Połączono z klientem");
        System.out.println("Nasłuchiwanie klienta...");


    }

    private void startMatch() throws Exception
    {
        JumpStatusVerifyCondition jumpStatus = new JumpStatusVerifyCondition();
        PreviousPawnVerifyCondition previousPawn = new PreviousPawnVerifyCondition();
        AdditionalVerifyCondition[] conditions = {jumpStatus, previousPawn};
        Response response;
        int[] coords;
        boolean isOver = false;

        //wysyła wszystkim komunikat start i board
        commandBuilder.addCommand("START");
        commandBuilder.addCommand("BOARD", gameMaster.getBoardAsString());
        sendToAll(commandBuilder.getCommand());

        while (!isOver)
        {
            for (Player player : players)
            {
                if (isOver)
                {
                    break;
                }
                //reset
                jumpStatus = new JumpStatusVerifyCondition();
                previousPawn = new PreviousPawnVerifyCondition();
                player.sendCommand("YOU");
                do
                {

                    try
                    {
                        response = ResponseInterpreter.getResponses(players.get(0).readResponse())[0];
                    } catch (Exception e)
                    {
                        System.out.println("Utracono połączenie z klientem");
                        break;
                    }
                    //otrzymany komunikat == SKIP
                    if (response.getCode().contentEquals("SKIP"))
                    {
                        //tworzy komendę STOP@BOARD i wysła aktywnemu graczowi
                        commandBuilder = new CommandBuilder();
                        commandBuilder.addCommand("STOP");
                        commandBuilder.addCommand("BOARD", gameMaster.getBoardAsString());
                        player.sendCommand(commandBuilder.getCommand());
                        //wysyła wszystkim innym graczom komendę BOARD
                        commandBuilder = new CommandBuilder();
                        commandBuilder.addCommand("BOARD", gameMaster.getBoardAsString());
                        sendToOthers(commandBuilder.getCommand(), player);
                    } else if (response.getCode().contentEquals("MOVE"))
                    {
                        coords = response.getNumbers();
                        //ustaw obecny pionek
                        previousPawn.setCurrentXY(coords[2], coords[3]);
                        jumpStatus.setStatus(gameMaster.verifyMove(coords[0], coords[1], coords[2], coords[3], conditions));
                        switch (jumpStatus.getStatus())
                        {
                            //verify zawiódł , do debug
                            case -1:
                            {
                                throw new RuntimeException();
                            }
                            // niepoprawny ruch, wyslij NOK
                            case 0:
                            {
                                player.sendCommand("NOK");
                                break;
                            }
                            //poprawny króki ruch
                            case 1:
                            {
                                //wykonaj ruch na planszy
                                gameMaster.makeMove(coords[0], coords[1], coords[2], coords[3]);

                                //wyślij graczowi komunika OK STOP BOARD
                                commandBuilder = new CommandBuilder();
                                commandBuilder.addCommand("OK");
                                commandBuilder.addCommand("STOP");
                                commandBuilder.addCommand("BOARD", gameMaster.getBoardAsString());
                                player.sendCommand(commandBuilder.getCommand());
                                //wyślij reszcie graczy BOARD
                                commandBuilder = new CommandBuilder();
                                commandBuilder.addCommand("BOARD", gameMaster.getBoardAsString());
                                sendToOthers(commandBuilder.getCommand(), player);
                                break;
                            }
                            case 2:
                            {

                                //wykonaj ruch na planszy
                                gameMaster.makeMove(coords[0], coords[1], coords[2], coords[3]);
                                //ustaw poprzedni pionek
                                previousPawn.setPreviousXY(coords[2], coords[3]);
                                //wyślij graczowi komunika OK BOARD
                                commandBuilder = new CommandBuilder();
                                commandBuilder.addCommand("OK");
                                commandBuilder.addCommand("BOARD", gameMaster.getBoardAsString());
                                player.sendCommand(commandBuilder.getCommand());
                                //wyślij reszcie graczy BOARD
                                commandBuilder = new CommandBuilder();
                                commandBuilder.addCommand("BOARD", gameMaster.getBoardAsString());
                                sendToOthers(commandBuilder.getCommand(), player);
                                break;
                            }
                            //debug
                            default:
                            {
                                throw new RuntimeException();
                            }
                        }
                        //sprawdzanie wygranej
                        if (gameMaster.isWinner(player.getColor()))
                        {
                            //wysyłamy komunikat o zwycięstwie
                            commandBuilder = new CommandBuilder();
                            commandBuilder.addCommand("WIN", "1");
                            player.sendCommand(commandBuilder.getCommand());

                            commandBuilder = new CommandBuilder();
                            commandBuilder.addCommand("WIN", "2");
                            //ustawiamy koniec
                            isOver = true;
                            //break do while
                            break;
                        }
                    }
                } while (jumpStatus.getStatus() == 2);
            }
        }
    }

    //wysyła komendę wszystkim
    private void sendToAll(String command)
    {
        for (Player temp : players)
        {
            temp.sendCommand(command);
        }
    }

    //wysyła wszystkim oprócz jednego
    private void sendToOthers(String command, Player excluded)
    {
        for (Player temp : players)
        {
            if (temp != excluded)
                temp.sendCommand(command);
        }
    }
}
