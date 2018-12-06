package main;

//TODO implement
class CommandBuilder
{
    private StringBuilder command;
    private String[] possible = {"WELCOME", "START", "OK", "NOK", "YOU", "STOP", "END", "BOARD"};


    CommandBuilder()
    {
        command = new StringBuilder();
    }

    void addCommand(String code)
    {
        if (command.toString().isEmpty())
        {
            command.append(code);
        } else
        {
            command.append("@");
            command.append(code);
        }
    }

    void addCommand(String code, String data)
    {
        if (command.toString().isEmpty())
        {
            command.append(code);
            command.append(" ");
            command.append(data);
        } else
        {
            command.append("@");
            command.append(code);
            command.append(" ");
            command.append(data);
        }

    }


    String getCommand()
    {
        return command.toString();
    }
}
